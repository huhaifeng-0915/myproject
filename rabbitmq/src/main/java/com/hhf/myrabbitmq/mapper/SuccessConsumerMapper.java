package com.hhf.myrabbitmq.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhf.myrabbitmq.dto.ConsumerDto;
import com.hhf.myrabbitmq.eo.SuccessConsumerEO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@Mapper
public interface SuccessConsumerMapper extends BaseMapper<SuccessConsumerEO> {

    IPage<SuccessConsumerEO> selectConsumers(IPage<SuccessConsumerEO> pageRequest, @Param("dto") ConsumerDto consumerMessageDto);

    int lockConsumer(@Param("consumerId") String consumerId);

    int unlockConsumer(@Param("consumerId") String consumerId);
}
