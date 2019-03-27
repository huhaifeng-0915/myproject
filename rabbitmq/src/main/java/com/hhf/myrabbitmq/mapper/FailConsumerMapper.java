package com.hhf.myrabbitmq.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhf.myrabbitmq.dto.ConsumerDto;
import com.hhf.myrabbitmq.eo.FailConsumerEO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@Mapper
public interface FailConsumerMapper extends BaseMapper<FailConsumerEO> {

    /**
     * 新开事务获取行锁
     *
     * @return 更新数量
     */
    int lockConsumer(String consumerId);

    IPage<FailConsumerEO> selectConsumers(IPage<FailConsumerEO> pageRequest, @Param("dto") ConsumerDto consumerMessageDto);

    int unlockConsumer(String consumerId);
}
