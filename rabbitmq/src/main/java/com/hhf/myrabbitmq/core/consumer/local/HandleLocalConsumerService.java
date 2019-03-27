package com.hhf.myrabbitmq.core.consumer.local;


import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.core.consumer.ConsumerService;
import com.hhf.myrabbitmq.enums.ConsumerType;

/**
 * 处理本地重发逻辑的具服务
 *
 * @author _g5niusx
 */
public interface HandleLocalConsumerService {

    <T extends QueueMessage> void handle(ConsumerType consumerType, ConsumerService<T> consumerService, T t, String queueName, String consumerId);

}
