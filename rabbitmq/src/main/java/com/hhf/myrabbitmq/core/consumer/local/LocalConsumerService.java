package com.hhf.myrabbitmq.core.consumer.local;

import com.hhf.myrabbitmq.enums.ConsumerType;

/**
 * 本地通过调用bean来实现不依赖于mq的消费者接口
 *
 * @author _g5niusx
 */
public interface LocalConsumerService {
    /**
     * @param consumerId   消费记录id
     * @param consumerType 消费记录类型
     */
    void reConsume(String consumerId, ConsumerType consumerType);
}
