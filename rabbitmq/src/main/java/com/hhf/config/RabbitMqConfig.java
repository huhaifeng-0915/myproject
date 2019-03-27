package com.hhf.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;

//@Configuration
public class RabbitMqConfig {
	
	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, ChannelAwareMessageListener listener) {
	    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	    container.setConnectionFactory(connectionFactory);

	    // 指定消费者
	    container.setMessageListener(listener);
	    // 指定监听的队列
	    container.setQueueNames("queue");

	    // 设置消费者的 ack 模式为手动确认模式
	    container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

	    container.setPrefetchCount(300);

	    return container;
	}
}
