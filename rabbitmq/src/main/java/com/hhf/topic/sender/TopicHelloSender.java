package com.hhf.topic.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 采用AmqpTemplate发送消息
 * @author hhf1311843248
 *
 */
@Component
public class TopicHelloSender {
    @Autowired
    private AmqpTemplate template;
    
    public void send() {
    template.convertAndSend("exchange","com.hhf.topic.message","Topic转发模式");
    }
}
