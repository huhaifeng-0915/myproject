package com.hhf.myrabbitmq.core.consumer.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

/**
 * 接受mq消费的接口
 *
 * @author _g5niusx
 */
public interface ReceiveService {

    @RabbitHandler
    void onMessage(String receivedMessage, Message message, Channel channel) throws Exception;
}
