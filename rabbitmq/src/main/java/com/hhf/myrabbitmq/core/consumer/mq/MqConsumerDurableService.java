package com.hhf.myrabbitmq.core.consumer.mq;


import com.hhf.myrabbitmq.eo.ConsumerEO;

/**
 * 持久化从mq接受的消费消息
 *
 * @author _g5niusx
 */
public interface MqConsumerDurableService {
    /**
     * 持久化一条mq重发的消费消息
     *
     * @param producerId 生产者id
     * @param queue      队列名称
     */
    ConsumerEO durableMqResendConsumer(String producerId, String queue);

    /**
     * 持久化一条mq非重发的消费者消息
     *
     * @param consumerEO 没有处理结果的消费信息
     */
    int durableMqConsumer(ConsumerEO consumerEO);

    /**
     * 更新已经完成业务处理的消费者信息
     *
     * @param consumerEO 包含处理结果的消费者
     */
    int completeConsumer(ConsumerEO consumerEO);
}
