package com.hhf.myrabbitmq.core.consumer.local;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.core.consumer.ConsumerService;
import com.hhf.myrabbitmq.core.consumer.mq.BaseConsumer;
import com.hhf.myrabbitmq.enums.ConsumerType;
import com.hhf.myrabbitmq.eo.*;
import com.hhf.myrabbitmq.service.BaseConsumerEOService;
import com.hhf.myrabbitmq.service.MessageProducerService;
import com.hhf.myrabbitmq.service.impl.FailConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.HandlingConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.SuccessConsumerServiceImpl;
import com.hhf.myrabbitmq.utils.GenericsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 调用本地消费者的服务
 *
 * @author _g5niusx
 */
@Slf4j
public class LocalConsumerServiceImpl implements LocalConsumerService {

    private final FailConsumerServiceImpl failConsumerService;
    private final SuccessConsumerServiceImpl successConsumerService;
    private final HandlingConsumerServiceImpl handlingConsumerService;
    private final MessageProducerService messageProducerService;
    private final HandleLocalConsumerService    handleLocalConsumerService;

    public LocalConsumerServiceImpl(FailConsumerServiceImpl failConsumerService, SuccessConsumerServiceImpl successConsumerService, HandlingConsumerServiceImpl handlingConsumerService,MessageProducerService messageProducerService,HandleLocalConsumerService handleLocalConsumerService) {
        this.failConsumerService = failConsumerService;
        this.successConsumerService = successConsumerService;
        this.handlingConsumerService = handlingConsumerService;
        this.messageProducerService = messageProducerService;
        this.handleLocalConsumerService = handleLocalConsumerService;
    }

    @Override
    public void reConsume(String consumerId, ConsumerType consumerType) {
        if (StringUtils.isEmpty(consumerId)) {
            log.error("消费者id为空，无法从本地重发消费者");
            return;
        }
        MessageProducerEO producerEO;
        ConsumerEO consumerEO;
        if (ConsumerType.HANDLING.equals(consumerType)) {
            /*查询出对应的数据，修改状态然后再处理*/
            consumerEO = handlingConsumerService.selectByPrimaryKey(consumerId);
            if (consumerEO == null) {
                log.error("消费者id[{}],消费类型[{}]没有数据", consumerId, consumerType);
                return;
            }
            producerEO = getProducerEO(handlingConsumerService, new HandlingConsumerEO(consumerEO));
            if (producerEO == null) {
                log.error("生产者id[{}]没有数据", consumerEO.getProducerId());
                return;
            }
        } else if (ConsumerType.SUCCESS.equals(consumerType)) {
            consumerEO = successConsumerService.selectByPrimaryKey(consumerId);
            if (consumerEO == null) {
                log.error("消费者id[{}],消费类型[{}]没有数据", consumerId, consumerType);
                return;
            }
            producerEO = getProducerEO(successConsumerService, new SuccessConsumerEO(consumerEO));
            if (producerEO == null) {
                log.error("生产者id[{}]没有数据", consumerEO.getProducerId());
                return;
            }
        } else {
            consumerEO = failConsumerService.selectByPrimaryKey(consumerId);
            if (consumerEO == null) {
                log.error("消费者id[{}],消费类型[{}]没有数据", consumerId, consumerType);
                return;
            }
            producerEO = getProducerEO(failConsumerService, new FailConsumerEO(consumerEO));
            if ((producerEO == null)) {
                log.error("生产者id[{}]没有数据", consumerEO.getProducerId());
                return;
            }
        }
        /*从缓存的bean中取出对应的消费处理类*/
        ConsumerService<QueueMessage> queueConsumerService = CacheConsumerProcessor.CACHE_CONSUMER_MAP.get(consumerEO.getQueue());
        QueueMessage                  queueMessage         = getMessageType(queueConsumerService, producerEO.getMessage());
        /*交给实现类去做处理*/
        handleLocalConsumerService.handle(consumerType, queueConsumerService, queueMessage, consumerEO.getQueue(), consumerId);
    }

    /**
     * 获取生产者
     *
     * @param consumerEOService 操作对应消息的service
     * @param c                 消息子类
     * @return 生产者
     */
    private <C extends ConsumerEO> MessageProducerEO getProducerEO(BaseConsumerEOService<C> consumerEOService, C c) {
        MessageProducerEO messageProducerEO = null;
        int        i                 = consumerEOService.lockConsumer(String.valueOf(c.getConsumerId()));
        if (i != 0) {
            messageProducerEO = messageProducerService.getById(c.getProducerId());
            if (messageProducerEO == null) {
                expireConsumer(consumerEOService, String.valueOf(c.getConsumerId()));
            }
        } else {
            log.warn("消费者id[{}],队列[{}]正在处理中，请稍后重发....", c.getConsumerId(), c.getQueue());
        }
        return messageProducerEO;
    }

    /**
     * 如果没有找到对应的生产者将该条消息的消费次数修改为9次，结果修改为没有找到生产者
     *
     * @param consumerEOService
     * @param id
     * @param <C>
     */
    @Transactional
    public <C extends ConsumerEO> void expireConsumer(BaseConsumerEOService<C> consumerEOService, String id) {
        consumerEOService.unlockConsumer(id);
        C c = consumerEOService.selectByPrimaryKey(id);
        c.setConsumerTimes(9);
        c.setResult("没有找到对应的生产者，程序自动将次数更新为9次!!!!");
        consumerEOService.updateConsumer(c);
    }

    /**
     * 消息转化为对象
     *
     * @param consumerService 缓存的对象bean
     * @param message         具体的消息
     * @return 真正使用的消息对象
     */
    private static QueueMessage getMessageType(ConsumerService<QueueMessage> consumerService, String message) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }
        Class<?> type = GenericsUtils.getSuperClassGenericsType(consumerService.getClass());
        if (type.getSuperclass().equals(QueueMessage.class)) {
            Object parseObject = JSON.parseObject(message, type);
            if (parseObject instanceof QueueMessage) {
                return (QueueMessage) parseObject;
            }
        }
        return null;
    }
}
