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
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_mq_success_consumer")
@ApiModel(value="SuccessConsumerEO对象", description="")
public class SuccessConsumerEO extends ConsumerEO {
    public SuccessConsumerEO(ConsumerEO consumerEO) {
        super(consumerEO);
    }
}
