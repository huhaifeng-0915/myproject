package com.hhf.myrabbitmq.core.consumer;

import com.hhf.myrabbitmq.core.MessageResult;
import com.hhf.myrabbitmq.core.QueueMessage;

/**
 * 消费消息的逻辑处理类
 * @param <T>
 */
public interface ConsumerService<T extends QueueMessage> {
    MessageResult handle(T request);
}
