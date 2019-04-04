package com.hhf.learn.topic.sender;

import com.hhf.learn.topic.constant.QueueAndExchangeConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 采用AmqpTemplate发送消息
 * 采用RabbitTemplate发送消息
 * @author hhf1311843248
 *
 */
@Component
public class TopicHelloSender {

//    @Autowired
//    private AmqpTemplate templates;
    @Autowired
    private RabbitTemplate template;
    
    public void send(String str) {
    template.convertAndSend(QueueAndExchangeConstants.TOPIC_EXCHANGE,QueueAndExchangeConstants.TOPIC_MESSAEGE,str);
    }
}
