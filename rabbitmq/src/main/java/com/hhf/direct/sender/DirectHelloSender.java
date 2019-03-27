package com.hhf.direct.sender;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 采用AmqpTemplate发送消息
 * @author hhf1311843248
 *
 */
@Component
public class DirectHelloSender {
    @Autowired
    private AmqpTemplate template;
    
    public void send(int str) {
    template.convertAndSend("queue","你好啊"+str);
    }
}
