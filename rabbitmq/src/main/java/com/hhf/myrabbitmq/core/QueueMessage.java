package com.hhf.myrabbitmq.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author _g5niusx
 */
@Getter
@Setter
@ToString
public class QueueMessage implements Serializable {

    private static final long serialVersionUID = 1396808961265296074L;
    /**
     * 生产者id
     */
    private String  producerId;
    /**
     * 业务流水号
     */
    private String  businessNo;
    /**
     * 发送时间,如果为空，则为立即发送，小于当前时间，该条消息将不会被处理
     */
    private Date    sendTime;
    /**
     * 是否持久化到数据库
     */
    private boolean durable;
}
