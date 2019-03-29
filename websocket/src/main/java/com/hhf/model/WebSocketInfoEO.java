package com.hhf.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hhf.common.entity.base.BaseVersionEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_web_socket_info")
@ApiModel(value="WebSocketInfoEO对象", description="")
public class WebSocketInfoEO extends BaseVersionEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "消息主体")
    @TableField("MESSAGE")
    private String message;

    @ApiModelProperty(value = "消息发送人")
    @TableField("SENDER")
    private String sender;

    @ApiModelProperty(value = "消息发送人Code")
    @TableField("SENDER_CODE")
    private String senderCode;

    @ApiModelProperty(value = "消息接收人")
    @TableField("RECEIVE")
    private String receive;

    @ApiModelProperty(value = "消息接收人分公司")
    @TableField("RECEIVE_BRANCH_CODE")
    private String receiveBranchCode;

    @ApiModelProperty(value = "消息接收人Code")
    @TableField("RECEIVE_CODE")
    private String receiveCode;

    @ApiModelProperty(value = "状态 0未读 1已读")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "任务创建时间")
    @TableField("TASK_CREATE_TIME")
    private Date taskCreateTime;


}
