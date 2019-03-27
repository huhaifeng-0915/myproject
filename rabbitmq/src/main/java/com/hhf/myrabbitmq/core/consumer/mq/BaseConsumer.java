package com.hhf.myrabbitmq.core.consumer.mq;

import com.alibaba.fastjson.JSON;
import com.hhf.myrabbitmq.configuration.CommonMqConfiguration;
import com.hhf.myrabbitmq.core.MessageResult;
import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.core.consumer.ConsumerService;
import com.hhf.myrabbitmq.enums.ConcurrencyStatus;
import com.hhf.myrabbitmq.eo.ConsumerEO;
import com.hhf.myrabbitmq.exception.CommonMqException;
import com.hhf.myrabbitmq.utils.ExceptionUtil;
import com.hhf.myrabbitmq.utils.GenericsUtils;
import com.hhf.myrabbitmq.utils.SequenceUtil;
import com.hhf.myrabbitmq.utils.SystemHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j(topic = "BaseConsumer")
public abstract class BaseConsumer<MESSAGE extends QueueMessage> implements ReceiveService, ConsumerService<MESSAGE> {
    /**
     * 缓存class的map
     */
    private static final Map<Class<? extends ConsumerService>, Class<?>> CACHE_CLASS_MAP = new ConcurrentHashMap<>();
    private              boolean                                         manual;

    private final CommonMqConfiguration rabbitMqConfiguration;
    private final MqConsumerDurableService mqConsumerDurableService;

    @Autowired
    public BaseConsumer(CommonMqConfiguration rabbitMqConfiguration, MqConsumerDurableService mqConsumerDurableService) {
        this.rabbitMqConfiguration = rabbitMqConfiguration;
        this.mqConsumerDurableService = mqConsumerDurableService;
    }

    @PostConstruct
    public void init() {
        CACHE_CLASS_MAP.put(getClass(), GenericsUtils.getSuperClassGenericsType(getClass()));
        manual = rabbitMqConfiguration.isManual();
    }

    @Override
    public void onMessage(String receivedMessage, Message message, Channel channel) throws Exception {
        MESSAGE       consumerMessage = null;
        MessageResult result          = MessageResult.success();
        String        queue           = message.getMessageProperties().getConsumerQueue();
        ConsumerEO consumerEO      = new ConsumerEO();
        try {
            /*转化对象*/
            try {
                Object parseObject = JSON.parseObject(receivedMessage, CACHE_CLASS_MAP.get(getClass()));
                consumerMessage = (MESSAGE) parseObject;
            } catch (Exception e) {
                log.error("JSON转对象异常", e);
                result = MessageResult.fail("JSON转对象异常" + ExceptionUtil.getErrorMsg(e));
                return;
            }
            if (consumerMessage == null) {
                result = MessageResult.fail("获取到消息为空");
                throw new CommonMqException("获取到消息为空!");
            }
            /*处理数据持久化*/
            try {
                if (consumerMessage.isDurable()) {
                    //是否为重发消息
                    boolean isResend = (boolean) message.getMessageProperties().getHeaders().getOrDefault("isResend", false);
                    if (isResend) {
                        consumerEO = mqConsumerDurableService.durableMqResendConsumer(consumerMessage.getProducerId(), queue);
                    } else {
                        consumerEO.setConsumerIp(SystemHelper.getSystemLocalIpStr());
                        consumerEO.setProducerId(consumerMessage.getProducerId());
                        consumerEO.setQueue(queue);
                        consumerEO.setConsumerTime(new Date());
                        consumerEO.setConcurrencyStatus(ConcurrencyStatus.LOCK.getValue());
                        consumerEO.setConsumerId(Long.valueOf(SequenceUtil.getSeqNo()));
                        consumerEO.setConsumerTimes(consumerEO.getConsumerTimes() == null ? 1 : consumerEO.getConsumerTimes() + 1);
                        mqConsumerDurableService.durableMqConsumer(consumerEO);
                    }
                }
            } catch (Exception e) {
                log.error("持久化消息异常", e);
                result = MessageResult.fail("持久化消息异常" + ExceptionUtil.getErrorMsg(e));
                return;
            }
            /*业务逻辑交给实现类*/
            try {
                result = handle(consumerMessage);
                if (result == null) {
                    result = MessageResult.fail("子类业务处理返回为空");
                }
            } catch (Exception e) {
                result = MessageResult.fail("处理子类异常" + ExceptionUtil.getErrorMsg(e));
                log.error("处理子类异常", e);
            }
        } finally {
            try {
                /*在消息指定持久化的情况下。才会去更新消息状态*/
                if (consumerMessage != null && consumerMessage.isDurable()) {
                    if(result != null){
                        consumerEO.setResultCode(result.getResultCode());
                        consumerEO.setResult(result.getResultMsg());
                    }
                    mqConsumerDurableService.completeConsumer(consumerEO);
                }
            } catch (Exception e) {
                log.error("更新消费者消息失败", e);
            } finally {
                if (manual) {
                    /*确认消费消息*/
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                }
            }
        }
    }
}
