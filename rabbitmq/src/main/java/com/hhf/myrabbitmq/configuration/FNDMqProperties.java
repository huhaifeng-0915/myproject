package com.hhf.myrabbitmq.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author _g5niusx
 */
@Getter
@Setter
@ConfigurationProperties("com.hhf.rabbitmq")
public class FNDMqProperties {
    /**
     * 是否启用
     */
    private boolean      enabled;
    private RabbitMqTask rabbitMqTask;

    @Getter
    @Setter
    public static class RabbitMqTask {
        /**
         * 是否自动删除已经成功的消息
         */
        private boolean enableDelSuccessMessage  = true;
        /**
         * 自动删除成功消息的时间表达式，默认为凌晨1点运行
         */
        private String  delSuccessCron           = "0 0 1 * * ?";
        /**
         * 删除多少天之前成功的消息
         */
        private Integer delTimeDay               = 30;
        /**
         * 是否开启延迟任务
         */
        private boolean enableResendDelayMessage = false;
        /**
         * 自动发送延迟任务的时间表达式
         */
        private String  resendDelayCron          = "0/10 * * * * ?";
        /**
         * 是否重发失败的消息
         */
        private boolean enableResendFailMessage;
        /**
         * 自动重发失败任务的时间表达式
         */
        private String  resendFailCron           = "0/5 * * * * ?";
    }
}
