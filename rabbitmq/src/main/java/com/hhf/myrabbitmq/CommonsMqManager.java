package com.hhf.myrabbitmq;


import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.core.consumer.local.LocalConsumerService;
import com.hhf.myrabbitmq.core.producer.CommonsProducerHandle;
import com.hhf.myrabbitmq.enums.ConsumerType;

/**
 * rabbitmq的管理类
 *
 * @author _g5niusx
 */
public class CommonsMqManager {
    /**
     * 发送消息的服务
     */
    private final CommonsProducerHandle commonsProducerHandle;
    /**
     * 本地调用bean重发消息的服务
     */
    private final LocalConsumerService localConsumerService;

    public CommonsMqManager(CommonsProducerHandle commonsProducerHandle, LocalConsumerService localConsumerService) {
        this.commonsProducerHandle = commonsProducerHandle;
        this.localConsumerService = localConsumerService;
    }

    /**
     * 重发生产者消息
     *
     * @param producerId 生产者id
     * @return 是否重发成功
     */
    public boolean resendProducer(String producerId) {
        return commonsProducerHandle.resendProducerMessage(producerId);
    }

    /**
     * 重发消费者消息
     *
     * @param consumerId   消费者id
     * @param consumerType 消费者类型
     */
    public void resendConsumer(String consumerId, ConsumerType consumerType) {
        localConsumerService.reConsume(consumerId, consumerType);
    }

    /**
     * 发送一条消息
     *
     * @param exchange   路由
     * @param routingKey 路由关键字
     * @param t          消息实体
     * @param <T>        范型
     * @return 生产者的id
     */
    public <T extends QueueMessage> String producerMessage(String exchange, String routingKey, T t) {
        return commonsProducerHandle.producerMessage(exchange, routingKey, t);
    }
}
