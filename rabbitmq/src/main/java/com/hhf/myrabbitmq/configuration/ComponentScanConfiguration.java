package com.hhf.myrabbitmq.configuration;

import com.hhf.myrabbitmq.core.consumer.local.*;
import com.hhf.myrabbitmq.core.consumer.mq.MqConsumerDurableService;
import com.hhf.myrabbitmq.core.consumer.mq.MqConsumerDurableServiceImpl;
import com.hhf.myrabbitmq.mapper.FailConsumerMapper;
import com.hhf.myrabbitmq.service.MessageProducerService;
import com.hhf.myrabbitmq.service.impl.FailConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.HandlingConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.MessageProducerServiceImpl;
import com.hhf.myrabbitmq.service.impl.SuccessConsumerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import static com.hhf.myrabbitmq.BeanConstants.*;


/**
 * 配置jar里面的自动配置
 *
 * @author _g5niusx
 */
@ComponentScan({"com.hhf.myrabbitmq", "com.hhf.myrabbitmq.core"})
@MapperScan("com.hhf.myrabbitmq.mapper")
@Configuration
@Slf4j
public class ComponentScanConfiguration {

    @Bean
    public CacheConsumerProcessor cacheConsumerProcessor() {
        return new CacheConsumerProcessor();
    }

    /**
     * 操作正在消费表的bean
     *
     * @return 实例bean
     */
    @Bean(BEAN_HANDLING_CONSUMER_EO_SERVICE)
    @Transactional
    public HandlingConsumerServiceImpl handlingConsumerEOService() {
        return new HandlingConsumerServiceImpl();
    }

    /**
     * 操作消费失败表的bean
     *
     * @return 实例bean
     */
    @Bean(BEAN_FAIL_CONSUMER_EO_SERVICE)
    @Transactional
    public FailConsumerServiceImpl failConsumerEOService(FailConsumerMapper failConsumerMapper) {
        return new FailConsumerServiceImpl(failConsumerMapper);
    }

    /**
     * 操作消费成功表的bean
     *
     * @return 实例bean
     */
    @Bean(BEAN_SUCCESS_CONSUMER_EO_SERVICE)
    @Transactional
    public SuccessConsumerServiceImpl successConsumerEOService() {
        return new SuccessConsumerServiceImpl();
    }

    /**
     * 操作生产表的bean
     *
     * @return 实例bean
     */
    @Bean(BEAN_PRODUCER_EO_SERVICE)
    @Transactional
    public MessageProducerServiceImpl producerEOService() {
        return new MessageProducerServiceImpl();
    }

    /**
     * 加载接受到mq消息的持久化bean
     *
     * @return 实例bean
     */
    @Bean(BEAN_MQ_CONSUMER_DURABLE_SERVICE)
    @Transactional
    @ConditionalOnMissingBean(MqConsumerDurableService.class)
    public MqConsumerDurableService mqConsumerDurableService(HandlingConsumerServiceImpl handlingConsumerService,
                                                             SuccessConsumerServiceImpl successConsumerService,
                                                             FailConsumerServiceImpl failConsumerService) {
        return new MqConsumerDurableServiceImpl(failConsumerService, successConsumerService, handlingConsumerService);
    }

    /**
     * 重发本地消费者的服务
     * @param handlingConsumerService
     * @param failConsumerService
     * @param successConsumerService
     * @param messageProducerService
     * @param handleLocalConsumerService
     * @return
     */
    @Bean(BEAN_LOCAL_CONSUMER_SERVICE)
    @Transactional
    @ConditionalOnMissingBean(LocalConsumerService.class)
    public LocalConsumerService localConsumerService(HandlingConsumerServiceImpl handlingConsumerService,
                                                     FailConsumerServiceImpl failConsumerService,
                                                     SuccessConsumerServiceImpl successConsumerService,
                                                     MessageProducerService messageProducerService,
                                                     HandleLocalConsumerService handleLocalConsumerService) {
        return new LocalConsumerServiceImpl(failConsumerService, successConsumerService, handlingConsumerService, messageProducerService, handleLocalConsumerService);
    }

    @Bean(BEAN_HANDLE_LOCAL_CONSUMER_SERVICE)
    @ConditionalOnMissingBean(HandleLocalConsumerService.class)
    public HandleLocalConsumerService handleLocalConsumerService(HandlingConsumerServiceImpl handlingConsumerService,
                                                                 FailConsumerServiceImpl failConsumerService,
                                                                 SuccessConsumerServiceImpl successConsumerService) {
        return new HandleLocalConsumerServiceImpl(failConsumerService, successConsumerService, handlingConsumerService);
    }
}
