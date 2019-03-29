package com.hhf.myrabbitmq.configuration;

import com.hhf.myrabbitmq.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: PGL
 * @Date: 2018/5/2
 * @Time: 13:52
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Configuration
public class ExchangeAndQueueConfig {


    /**
     * web_socket交换机
     * @return
     */
    @Bean(RabbitMqConstants.EXCHANGE_WEB_SOCKET)
    TopicExchange fanoutExchange() {
        return new TopicExchange(RabbitMqConstants.EXCHANGE_WEB_SOCKET);
    }

    /**
     * direct 测试的交换机  用来绑定一一对应的队列
     * @return
     */
    @Bean(RabbitMqConstants.EXCHANGE_TEST_DIRECT)
    DirectExchange testExchange() {
        return new DirectExchange(RabbitMqConstants.EXCHANGE_TEST_DIRECT);
    }


    /**
     * direct 交换机  用来绑定一一对应的队列
     * @return
     */
    @Bean(RabbitMqConstants.EXCHANGE_DIRECT)
    DirectExchange directExchange() {
        return new DirectExchange(RabbitMqConstants.EXCHANGE_DIRECT);
    }
    /**
     * webSocket消息队列
     * <p>
     *     用户产生消息后，除了向web_socket交换机发送外，还向direct交换机发送，根据路由发送到 webSocket队列
     * </p>
     * @return
     */
    @Bean(RabbitMqConstants.WEB_SOCKET_QUEUE)
    Queue promptInfoQueue() {
        return new Queue(RabbitMqConstants.WEB_SOCKET_QUEUE);
    }

    /**
     * 核保回写队列
     * <p>
     *     当需要调用外部系统时，向direct交换机发送消息，根据路由发送到 uw_audit_back队列
     * </p>
     * @return
     */
    @Bean(RabbitMqConstants.ADD_PERSON)
    Queue uwAuditBackQueue() {
        return new Queue(RabbitMqConstants.ADD_PERSON);
    }

    /**
     * 核保回写队列
     * <p>
     *     当需要调用外部系统时，向direct交换机发送消息，根据路由发送到 uw_audit_back队列
     * </p>
     * @return
     */
    @Bean(RabbitMqConstants.TEST)
    Queue testQueue() {
        return new Queue(RabbitMqConstants.TEST);
    }

    /**
     * 绑定  direct交换机和web_socket_queue队列
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    Binding bindingPromptInfo(@Qualifier(RabbitMqConstants.EXCHANGE_WEB_SOCKET) TopicExchange exchange,
                              @Qualifier(RabbitMqConstants.WEB_SOCKET_QUEUE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.WEB_SOCKET_QUEUE);
    }

    /**
     * 绑定  direct交换机和 uw_audit_back队列
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    Binding bindingUwAuditBack(@Qualifier(RabbitMqConstants.EXCHANGE_DIRECT) DirectExchange exchange,
                               @Qualifier(RabbitMqConstants.ADD_PERSON) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.ADD_PERSON);
    }

    /**
     * 绑定  direct_test交换机和 TEST队列
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    Binding bindingTest(@Qualifier(RabbitMqConstants.EXCHANGE_TEST_DIRECT) DirectExchange exchange,
                               @Qualifier(RabbitMqConstants.TEST) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.TEST);
    }


}
