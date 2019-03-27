package com.hhf.myrabbitmq.core.consumer.local;

import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.core.consumer.ConsumerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 缓存service和队列关系的map
 *
 * @author _g5niusx
 */
public class CacheConsumerProcessor implements BeanPostProcessor {
    public static final Map<String, ConsumerService<QueueMessage>> CACHE_CONSUMER_MAP = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?>       targetClass    = AopUtils.getTargetClass(bean);
        RabbitListener rabbitListener = AnnotationUtils.findAnnotation(targetClass, RabbitListener.class);
        if (rabbitListener != null) {
            Class<?>[] interfaces = targetClass.getSuperclass().getInterfaces();
            Stream.of(interfaces).filter(aClass -> aClass.getName().equals(ConsumerService.class.getName())).forEach(aClass -> {
                String[] queues = rabbitListener.queues();
                Stream.of(queues).forEach(queue -> CACHE_CONSUMER_MAP.putIfAbsent(queue, (ConsumerService) bean));
            });
        }
        return bean;
    }
}
