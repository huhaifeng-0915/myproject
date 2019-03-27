package com.hhf.myrabbitmq.eo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hhf.common.entity.base.BaseVersionEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

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
@ApiModel(value="ConsumerEO对象,父类", description="")
public class ConsumerEO extends BaseVersionEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CONSUMER_ID", type = IdType.ID_WORKER)
    private Long consumerId;

    @ApiModelProperty(value = "生产者主键")
    @TableField("PRODUCER_ID")
    private String producerId;

    @ApiModelProperty(value = "处理结果")
    @TableField("RESULT")
    private String result;

    @ApiModelProperty(value = "消费次数")
    @TableField("CONSUMER_TIMES")
    private Integer consumerTimes;

    @ApiModelProperty(value = "消费IP")
    @TableField("CONSUMER_IP")
    private String consumerIp;

    @ApiModelProperty(value = "队列代码")
    @TableField("QUEUE")
    private String queue;

    @ApiModelProperty(value = "消费时间")
    @TableField("CONSUMER_TIME")
    private Date consumerTime;

    @ApiModelProperty(value = "并发状态")
    @TableField("CONCURRENCY_STATUS")
    private String concurrencyStatus;

    /**
     * <p>
     *     不存入数据库
     * </p>
     */
    @TableField(exist = false)
    private String resultCode;

    public ConsumerEO() {
    }

    public ConsumerEO(ConsumerEO consumerEO) {
        this.consumerIp = consumerEO.getConsumerIp();
        this.consumerTimes = consumerEO.getConsumerTimes();
        this.consumerId = consumerEO.getConsumerId();
        this.producerId = consumerEO.getProducerId();
        this.queue = consumerEO.getQueue();
        this.result = consumerEO.getResult();
        this.resultCode = consumerEO.getResultCode();
        this.concurrencyStatus = consumerEO.getConcurrencyStatus();
        this.consumerTime = consumerEO.getConsumerTime();
    }
}
