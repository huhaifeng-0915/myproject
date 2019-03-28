package com.hhf.learn.direct.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 采用的是Direct模式,需要在配置Queue的时候,指定一个键,使其和交换机绑定.
 * @author hhf1311843248
 *
 */
@Configuration
public class DirectSenderConf {
     @Bean(name = "queue")
     public Queue queue() {
          return new Queue("queue");
     }

     @Bean(name = "queueObject")
     public Queue queueObject() {
          return new Queue("queueObject");
     }
}