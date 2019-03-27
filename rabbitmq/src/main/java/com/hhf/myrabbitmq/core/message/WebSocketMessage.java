package com.hhf.myrabbitmq.core.message;

import com.hhf.myrabbitmq.core.QueueMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 向前端浏览器发送消息的对象
 * @author c_liwenbiao
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class WebSocketMessage extends QueueMessage {

    private static final long serialVersionUID = -6588288307057092175L;
    @ApiModelProperty(value = "发送人的loginCode",example = "12345")
    private String senderCode;

    @ApiModelProperty(value = "发送人的姓名",example = "张三")
    private String sender;

    @ApiModelProperty(value = "接收人的loginCode",example = "54321")
    private String receiveCode;

    @ApiModelProperty(value = "接收人的姓名",example = "李四")
    private String receive;

    @ApiModelProperty(value = "接收人的分公司code",example = "3010100")
    private String receiveBranchCode;

    @ApiModelProperty(value = "消息内容",example = "消息内容")
    private String message;

    @ApiModelProperty(value = "任务创建时间", example = "任务分配时，新的task创建时间")
    private Date taskCreateTime;
}
