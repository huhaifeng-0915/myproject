package com.hhf.topic.receive;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收端,我们配置两个监听器,分别监听不同的队列
 * @author hhf1311843248
 *
 */
@Component
public class TopicHelloReceive {

	@RabbitListener(queues="com.hhf.topic.message")    //监听器监听指定的Queue
    public void process1(String str) {    
        System.out.println("message:"+str);
    }
    @RabbitListener(queues="com.hhf.topic.messages")    //监听器监听指定的Queue
    public void process2(String str) {
        System.out.println("messages:"+str);
    }
}