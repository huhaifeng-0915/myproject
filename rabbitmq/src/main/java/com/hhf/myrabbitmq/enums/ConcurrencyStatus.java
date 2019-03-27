package com.hhf.myrabbitmq.enums;

import lombok.Getter;

/**
 * 一条消息的消费状态
 *
 * @author _g5niusx
 */
@Getter
public enum ConcurrencyStatus {
    /**
     * 可操作状态
     */
    UN_LOCK("0000"),
    /**
     * 正在被操作
     */
    LOCK("9999");
    private String value;

    ConcurrencyStatus(String value) {
        this.value = value;
    }
}
