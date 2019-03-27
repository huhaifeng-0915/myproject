package com.hhf.myrabbitmq.eo;

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
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_mq_message_producer")
@ApiModel(value="MessageProducerEO对象", description="")
public class MessageProducerEO extends BaseVersionEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "PRODUCER_ID", type = IdType.ID_WORKER)
    private Long producerId;

    @ApiModelProperty(value = "业务流水号")
    @TableField("BUSINESS_NO")
    private String businessNo;

    @ApiModelProperty(value = "交换机名称")
    @TableField("EXCHANGE")
    private String exchange;

    @ApiModelProperty(value = "路由key")
    @TableField("ROUTING_KEY")
    private String routingKey;

    @ApiModelProperty(value = "生产者IP")
    @TableField("PRODUCER_IP")
    private String producerIp;

    @ApiModelProperty(value = "发送的具体消息")
    @TableField("MESSAGE")
    private String message;

    @ApiModelProperty(value = "该条消息指定的发送时间,如果为null或者小于当前时间则表示马上发送")
    @TableField("SEND_TIME")
    private Date sendTime;

    @ApiModelProperty(value = "发送次数")
    @TableField("SEND_TIMES")
    private Integer sendTimes;


}
