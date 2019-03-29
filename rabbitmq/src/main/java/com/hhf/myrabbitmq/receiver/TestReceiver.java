package com.hhf.myrabbitmq.receiver;

import com.hhf.myrabbitmq.constants.RabbitMqConstants;
import com.hhf.myrabbitmq.core.MessageResult;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author huhaifeng
 */
@Component
public class TestReceiver {

    private static final Logger logger = LoggerFactory.getLogger(TestReceiver.class);



    @RabbitListener(queues = RabbitMqConstants.TEST)
    @RabbitHandler
    public MessageResult handle(Message message, Channel channel) {

            logger.info("--TestReceiver开始--");
            logger.info("收到消息啦，消息内容为：{}",new String(message.getBody()));
        try {
            //true为所有消费者都需确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            return MessageResult.fail("消息接收失败，异常为：{}"+e.getMessage());
        }
        //将 返回 结果持久化
        return MessageResult.success();
    }
}
