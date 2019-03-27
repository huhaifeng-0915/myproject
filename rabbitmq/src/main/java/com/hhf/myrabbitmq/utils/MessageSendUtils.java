package com.hhf.myrabbitmq.utils;


import com.hhf.common.utils.SpringContextHolder;
import com.hhf.demo.model.Person;
import com.hhf.myrabbitmq.core.message.AddPersonMessage;
import com.hhf.myrabbitmq.sender.Sender;

/**
 * @author huhaifeng
 */
public class MessageSendUtils {


    /**
     * 接口补偿
     * 核保回写 先发送mq 解耦后 调用外部系统
     * @param message
     */
    public static String send2UwAuditBack(AddPersonMessage message) {
        Sender sender = SpringContextHolder.getBean(Sender.class);
        return sender.send2UwAuditBack(message);
    }

}
