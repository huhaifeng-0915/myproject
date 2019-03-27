package com.hhf.myrabbitmq.core;

import com.hhf.myrabbitmq.core.constant.MessageConstant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * mq消息消费返回的实体
 *
 * @author _g5niusx
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageResult {

    private             String resultCode;
    private             String resultMsg;

    /**
     * 消息消费成功
     *
     * @return 处理结果
     */
    public static MessageResult success() {
        return success("业务处理成功");
    }

    /**
     * 消息消费成功
     *
     * @param result 处理结果
     * @return 处理结果
     */
    public static MessageResult success(String result) {
        MessageResult messageResult = new MessageResult();
        messageResult.setResultCode(MessageConstant.SUCCESS);
        messageResult.setResultMsg(result);
        return messageResult;
    }

    /**
     * 消息消费失败
     *
     * @return 处理结果
     */
    public static MessageResult fail() {
        return fail("业务处理失败");
    }

    /**
     * 消息消费失败
     *
     * @param result 结果消息
     * @return 处理结果
     */
    public static MessageResult fail(String result) {
        MessageResult messageResult = new MessageResult();
        messageResult.setResultMsg(result);
        messageResult.setResultCode(MessageConstant.FAIL);
        return messageResult;
    }
}
