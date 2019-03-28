package com.hhf.learn.topic.config;

import com.hhf.learn.topic.constant.QueueAndExchangeConstants;
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

        @Bean(name= QueueAndExchangeConstants.TOPIC_MESSAEGE)
        public Queue queueMessage() {
            return new Queue(QueueAndExchangeConstants.TOPIC_MESSAEGE);
        }

        @Bean(name="messages")
        public Queue queueMessages() {
            return new Queue("com.hhf.learn.topic.messages");
        }

        @Bean
        public TopicExchange exchange() {
            return new TopicExchange(QueueAndExchangeConstants.TOPIC_EXCHANGE);
        }

        @Bean
        Binding bindingExchangeMessage(@Qualifier(QueueAndExchangeConstants.TOPIC_MESSAEGE) Queue queueMessage, TopicExchange exchange) {
            return BindingBuilder.bind(queueMessage).to(exchange).with(QueueAndExchangeConstants.TOPIC_EXCHANGE);
        }

        @Bean
        Binding bindingExchangeMessages(@Qualifier("messages") Queue queueMessages, TopicExchange exchange) {
            return BindingBuilder.bind(queueMessages).to(exchange).with("com.hhf.learn.topic.#");//*表示一个词,#表示零个或多个词
        }
}