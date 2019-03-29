package com.hhf.myrabbitmq.sender;

import com.alibaba.fastjson.JSONObject;
import com.hhf.common.exception.BusinessException;
import com.hhf.common.utils.JacksonUtils;
import com.hhf.myrabbitmq.constants.RabbitMqConstants;
import com.hhf.myrabbitmq.core.message.AddPersonMessage;
import com.hhf.myrabbitmq.core.message.MyTestMessage;
import com.hhf.myrabbitmq.core.message.WebSocketMessage;
import com.hhf.myrabbitmq.core.producer.CommonsProducerHandle;
import com.hhf.myrabbitmq.utils.SequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: huhaifeng
 * @Date: 2018/5/2
 * @Time: 14:32
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Component
public class Sender implements RabbitTemplate.ConfirmCallback {

    /**
     * webSocket发送的前缀 固定为exchange
     */
    private static final String EXCHANGE = "/exchange/";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RabbitTemplate rabbitTemplate;
//    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommonsProducerHandle commonsProducerHandle;
    /**
     * 构造方法注入
     */
    @Autowired
    public Sender(RabbitTemplate rabbitTemplate, CommonsProducerHandle commonsProducerHandle) {
        this.rabbitTemplate = rabbitTemplate;
        this.commonsProducerHandle = commonsProducerHandle;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }
   /* @Autowired
    public Sender(RabbitTemplate rabbitTemplate, SimpMessagingTemplate simpMessagingTemplate, CommonsProducerHandle commonsProducerHandle) {
        this.rabbitTemplate = rabbitTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.commonsProducerHandle = commonsProducerHandle;
        rabbitTemplate.setConfirmCallback(this); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }*/

    public String send(String exchange,String routingKey,Object object) {
        CorrelationData correlationData= new CorrelationData();
        correlationData.setId(SequenceUtil.getSeqNo());
        logger.info("exchange:{} ,routingkey:{},context:{},time:{}",exchange,routingKey, JSONObject.toJSON(object),System.currentTimeMillis());
        try {
            this.rabbitTemplate.convertAndSend(exchange,routingKey, JacksonUtils.obj2json(object),correlationData);
            return correlationData.getId();
        }catch (Exception e){
            logger.error("exchange:{} ,routingkey:{},context:{},time:{},exception:{}",exchange,routingKey, JSONObject.toJSON(object),System.currentTimeMillis(),e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("correlationData:{} ,ack:{},cause:{},time:{}",correlationData,ack, cause,System.currentTimeMillis());
    }

    /**
     *  向前端客户端发送消息
     * @param message
     * @return
     */
    /*public String send2Client(WebSocketMessage message) {
        String id = SequenceUtil.getSeqNo();
        message.setProducerId(id);
        //发送消息给rabbitmq
        try {
            simpMessagingTemplate.convertAndSend(EXCHANGE + RabbitMqConstants.EXCHANGE_WEB_SOCKET + "/" + message.getReceiveCode(), JacksonUtils.obj2json(message));
            logger.info("webSocket消息发送成功:{}",message.getMessage());
        } catch (Exception e) {
            logger.error("webSocket消息发送失败:{}",message.getMessage());
            throw new BusinessException("webSocket消息发送失败", e);
        }
        return id;
    }*/


    /**
     * 核保回写发送的消息
     * @param message
     * @return
     */
    public String send2UwAuditBack(AddPersonMessage message) {
        //持久化到数据库中
        message.setDurable(true);
        try {
            return commonsProducerHandle.producerMessage(RabbitMqConstants.EXCHANGE_DIRECT, RabbitMqConstants.ADD_PERSON, message);
        } catch (Exception e) {
            throw new BusinessException("核保回写消息发送失败:"+ e);
        }
    }

    /**
     * 测试webSocket消息
     * @param message
     * @return
     */
    public String sendWebSocketMessage(WebSocketMessage message) {
        //持久化到数据库中
        message.setDurable(true);
        try {
            return commonsProducerHandle.producerMessage(RabbitMqConstants.EXCHANGE_WEB_SOCKET, RabbitMqConstants.WEB_SOCKET_QUEUE, message);
        } catch (Exception e) {
            throw new BusinessException("webSocket消息发送失败:"+ e);
        }
    }

    /**
     * Mytest发送消息
     * @param message
     * @return
     */
    public String myTest(String message) {
        try {
            this.rabbitTemplate.convertAndSend(RabbitMqConstants.EXCHANGE_TEST_DIRECT, RabbitMqConstants.TEST, message);
        } catch (Exception e) {
            throw new BusinessException("测试发送消息失败:"+ e);
        }
        return "发送成功";
    }


    public void resend2UwAuditBack(String producerId) {
        commonsProducerHandle.resendProducerMessage(producerId);
    }
}
