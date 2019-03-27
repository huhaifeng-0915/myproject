package com.hhf.myrabbitmq.core.producer;

import com.alibaba.fastjson.JSON;
import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.eo.MessageProducerEO;
import com.hhf.myrabbitmq.exception.CommonMqException;
import com.hhf.myrabbitmq.service.MessageProducerService;
import com.hhf.myrabbitmq.utils.SequenceUtil;
import com.hhf.myrabbitmq.utils.SystemHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 生产者实现类
 */
@Slf4j
public class CommonsProducerHandler implements CommonsProducerHandle {
    private final RabbitMessagingTemplate rabbitMessagingTemplate;
    private final MessageProducerService messageProducerService;
    private final boolean                 enableDelayMessage;

    public CommonsProducerHandler(RabbitMessagingTemplate rabbitMessagingTemplate, MessageProducerService messageProducerService, boolean enableDelayMessage) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
        this.messageProducerService = messageProducerService;
        this.enableDelayMessage = enableDelayMessage;
    }

    @Override
    public <T extends QueueMessage> String producerMessage(String exchange, String routingKey, T t) {
        String id = SequenceUtil.getSeqNo();
        t.setProducerId(id);
        String message = JSON.toJSONString(t);
        //判断是否开启持久化
        boolean ifDelay = false;
        if (t.isDurable()) {
            MessageProducerEO messageProducerEO = new MessageProducerEO();
            messageProducerEO.setExchange(exchange);
            messageProducerEO.setRoutingKey(routingKey);
            messageProducerEO.setSendTime(t.getSendTime());
            messageProducerEO.setProducerId(Long.valueOf(id));
            messageProducerEO.setMessage(message);
            messageProducerEO.setBusinessNo(t.getBusinessNo());
            messageProducerEO.setProducerIp(SystemHelper.getSystemLocalIpStr());
            messageProducerEO.setSendTimes(messageProducerEO.getSendTimes() == null ? 1 : messageProducerEO.getSendTimes() + 1);
            this.messageProducerService.save(messageProducerEO);
            //开启延时发送并且当前时间小于发送时间
            if (this.enableDelayMessage && t.getSendTime() != null && (new Date()).compareTo(t.getSendTime()) < 0) {
                ifDelay = true;
                log.info("消息将延时发送,exchange:{},routingKey:{},producerId:{}",exchange,routingKey,id);
            }
        }
        //立即发送
        if(!ifDelay){
            //todo 发送消息需要确保到达mq的队列
            this.rabbitMessagingTemplate.convertAndSend(exchange, routingKey, message);
            log.info("消息发送成功,exchange:{},routingKey:{},id:{}",exchange,routingKey);
        }
        return id;
    }

    @Override
    public boolean resendProducerMessage(String producerId) {
        if (StringUtils.isEmpty(producerId)) {
            throw new CommonMqException("id不能为空!!");
        } else {
            MessageProducerEO messageProducerEO = this.messageProducerService.getById(producerId);
            if (messageProducerEO == null) {
                throw new CommonMqException(MessageFormat.format("id[{0}]没有查询到生产者数据!!!", producerId));
            } else {
                messageProducerEO.setSendTimes(messageProducerEO.getSendTimes() == null ? 1 : messageProducerEO.getSendTimes() + 1);
                HashMap<String, Object> headers = new HashMap<>(2);
                headers.put("isResend", true);
                this.rabbitMessagingTemplate.convertAndSend(messageProducerEO.getExchange(), messageProducerEO.getRoutingKey(), messageProducerEO.getMessage(), headers);
                this.messageProducerService.updateById(messageProducerEO);
                return true;
            }
        }
    }
}
