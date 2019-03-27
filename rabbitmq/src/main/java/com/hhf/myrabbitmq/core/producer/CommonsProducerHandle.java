package com.hhf.myrabbitmq.core.producer;


import com.hhf.myrabbitmq.core.QueueMessage;

/**
 * 发送消息的service
 *
 * @author _g5niusx
 */
public interface CommonsProducerHandle {
    /**
     * 生产一条消息
     *
     * @param exchange   路由
     * @param routingKey 路由关键字
     * @param t          消息实体
     * @param <T>        范型
     * @return 生产者id
     */
    <T extends QueueMessage> String producerMessage(String exchange, String routingKey, T t);

    /**
     * 重发一条生产者的消息
     *
     * @param producerId 生产者的id
     * @return 处理结果
     */
    boolean resendProducerMessage(String producerId);
}
