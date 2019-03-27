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
     * direct 交换机  用来绑定一一对应的队列
     * @return
     */
    @Bean(RabbitMqConstants.EXCHANGE_DIRECT)
    DirectExchange directExchange() {
        return new DirectExchange(RabbitMqConstants.EXCHANGE_DIRECT);
    }

    /**
     * prompt_info队列
     * <p>
     *     用户产生消息后，除了向web_socket交换机发送外，还向direct交换机发送，根据路由发送到 prompt_info队列
     * </p>
     * @return
     */
    @Bean(RabbitMqConstants.PROMPT_INFO)
    Queue promptInfoQueue() {
        return new Queue(RabbitMqConstants.PROMPT_INFO);
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
     * 绑定  direct交换机和 prompt_info队列
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    Binding bindingPromptInfo(@Qualifier(RabbitMqConstants.EXCHANGE_DIRECT) DirectExchange exchange,
                              @Qualifier(RabbitMqConstants.PROMPT_INFO) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstants.PROMPT_INFO);
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


}
