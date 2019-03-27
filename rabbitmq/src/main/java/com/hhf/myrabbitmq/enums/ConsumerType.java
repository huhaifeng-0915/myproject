package com.hhf.myrabbitmq.enums;

/**
 * 消费者类型
 */
public enum ConsumerType {
    /**
     * 下发中
     */
    HANDLING,
    /**
     * 处理失败的消息
     */
    FAIL,
    /**
     * 处理成功的消息
     */
    SUCCESS
}
