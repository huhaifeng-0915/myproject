package com.hhf.topic.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Topic转发模式
 * @author hhf1311843248
 *首先我们看发送端,我们需要配置队列Queue,再配置交换机(Exchange),再把队列按照相应的规则绑定到交换机上
 */
@Configuration
public class TopicSenderConf {

        @Bean(name="message")
        public Queue queueMessage() {
            return new Queue("com.hhf.topic.message");
        }

        @Bean(name="messages")
        public Queue queueMessages() {
            return new Queue("com.hhf.topic.messages");
        }

        @Bean
        public TopicExchange exchange() {
            return new TopicExchange("exchange");
        }

        @Bean
        Binding bindingExchangeMessage(@Qualifier("message") Queue queueMessage, TopicExchange exchange) {
            return BindingBuilder.bind(queueMessage).to(exchange).with("com.hhf.topic.message");
        }

        @Bean
        Binding bindingExchangeMessages(@Qualifier("messages") Queue queueMessages, TopicExchange exchange) {
            return BindingBuilder.bind(queueMessages).to(exchange).with("com.hhf.topic.#");//*表示一个词,#表示零个或多个词
        }
}