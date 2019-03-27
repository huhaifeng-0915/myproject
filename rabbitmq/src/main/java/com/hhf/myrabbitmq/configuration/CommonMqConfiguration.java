package com.hhf.myrabbitmq.configuration;

import com.hhf.common.idworker.snowflake.RandomIdWorkerAutoConfig;
import com.hhf.myrabbitmq.CommonsMqManager;
import com.hhf.myrabbitmq.core.consumer.local.LocalConsumerService;
import com.hhf.myrabbitmq.core.producer.CommonsProducerHandle;
import com.hhf.myrabbitmq.core.producer.CommonsProducerHandler;
import com.hhf.myrabbitmq.service.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.hhf.myrabbitmq.BeanConstants.BEAN_PRODUCER_EO_SERVICE;

/**
 * rabbitmq的自动化配置
 *
 * @author _g5niusx
 */
@EnableConfigurationProperties({FNDMqProperties.class, RabbitProperties.class})
@ConditionalOnProperty(name = "com.hhf.rabbitmq.enabled", havingValue = "true")
@AutoConfigureAfter({RabbitAutoConfiguration.class, RandomIdWorkerAutoConfig.class})
@Configuration("commonMqConfiguration")
@Slf4j
@Import({RabbitAutoConfiguration.class, RandomIdWorkerAutoConfig.class, ComponentScanConfiguration.class})
public class CommonMqConfiguration {
    private final boolean isManual;

    private final FNDMqProperties rabbitMqProperties;

    @Autowired
    public CommonMqConfiguration(RabbitProperties rabbitProperties, FNDMqProperties rabbitMqProperties) {
        RabbitProperties.Listener listener = rabbitProperties.getListener();
        if (RabbitProperties.ContainerType.SIMPLE.equals(listener.getType())) {
            isManual = listener.getSimple().getAcknowledgeMode().isManual();
        } else {
            isManual = listener.getDirect().getAcknowledgeMode().isManual();
        }
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    @ConditionalOnMissingBean(CommonsProducerHandle.class)
    public CommonsProducerHandle commonsSendMessageHandle(RabbitMessagingTemplate rabbitMessagingTemplate, @Qualifier(BEAN_PRODUCER_EO_SERVICE) MessageProducerService messageProducerService) {
        boolean enableDelayMessage = false;
        if (rabbitMqProperties.getRabbitMqTask() != null && rabbitMqProperties.getRabbitMqTask().isEnableResendDelayMessage()) {
            enableDelayMessage = true;
        }
        return new CommonsProducerHandler(rabbitMessagingTemplate, messageProducerService, enableDelayMessage);
    }

    @Bean
    public CommonsMqManager commonsRabbitMqManager(@Autowired(required = false) LocalConsumerService localConsumerService,
                                                   CommonsProducerHandle commonsProducerHandle) {
        log.info("-- 加载mq组件的通用管理类 --");
        return new CommonsMqManager(commonsProducerHandle, localConsumerService);
    }

    public boolean isManual() {
        return isManual;
    }
}
