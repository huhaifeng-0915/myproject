package com.hhf.myrabbitmq;

/**
 * 本组件中使用的所有的自定义bean名称
 *
 * @author _g5niusx
 */
public final class BeanConstants {
    /**
     * 处理失败消息的bean
     */
    public static final String BEAN_FAIL_CONSUMER_EO_SERVICE      = "failConsumerService";
    /**
     * 处理下发中消息的bean
     */
    public static final String BEAN_HANDLING_CONSUMER_EO_SERVICE  = "handlingConsumerService";
    /**
     * 处理成功消息的bean
     */
    public static final String BEAN_SUCCESS_CONSUMER_EO_SERVICE   = "successConsumerService";
    /**
     * 生产者的bean
     */
    public static final String BEAN_PRODUCER_EO_SERVICE           = "messageProducerService";
    /**
     * 处理mq消费者的服务
     */
    public static final String BEAN_MQ_CONSUMER_DURABLE_SERVICE   = "mqConsumerDurableService";
    /**
     * 处理本地消费者的服务
     */
    public static final String BEAN_LOCAL_CONSUMER_SERVICE        = "localConsumerService";
    /**
     * 重发本地消费者的处理服务
     */
    public static final String BEAN_HANDLE_LOCAL_CONSUMER_SERVICE = "handleLocalConsumerService";

}
