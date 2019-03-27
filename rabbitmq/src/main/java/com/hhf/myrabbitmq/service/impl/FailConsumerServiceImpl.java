package com.hhf.myrabbitmq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hhf.common.response.pojo.CommonPageResponse;
import com.hhf.common.utils.MybatisPlusPageUtils;
import com.hhf.myrabbitmq.dto.ConsumerDto;
import com.hhf.myrabbitmq.eo.FailConsumerEO;
import com.hhf.myrabbitmq.mapper.FailConsumerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhf.myrabbitmq.service.BaseConsumerEOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@Service
public class FailConsumerServiceImpl implements BaseConsumerEOService<FailConsumerEO> {

    private final FailConsumerMapper failConsumerMapper;

    public FailConsumerServiceImpl(FailConsumerMapper failConsumerMapper) {
        this.failConsumerMapper = failConsumerMapper;
    }
    @Override
    public int insertConsumer(FailConsumerEO failConsumerEO) {
        return failConsumerMapper.insert(failConsumerEO);
    }

    @Override
    public int updateConsumer(FailConsumerEO failConsumerEO) {
        return failConsumerMapper.updateById(failConsumerEO);
    }

    @Override
    public int deleteConsumer(String id) {
        return failConsumerMapper.deleteById(Long.valueOf(id));
    }

    @Override
    public CommonPageResponse<FailConsumerEO> selectConsumers(ConsumerDto consumerMessageDto) {
        IPage<FailConsumerEO> iPage = failConsumerMapper.selectConsumers(new Page<FailConsumerEO>(consumerMessageDto.getPageNum(), consumerMessageDto.getPageSize()), consumerMessageDto);
        return MybatisPlusPageUtils.newCommonPageResponse(iPage);
    }

    @Override
    public FailConsumerEO selectByPrimaryKey(String id) {
        return failConsumerMapper.selectById(Long.valueOf(id));
    }

    @Override
    public FailConsumerEO selectConsumerByProducerId(String producerId, String queue) {
        QueryWrapper<FailConsumerEO> queryWrapper = new QueryWrapper<FailConsumerEO>();
        queryWrapper.eq("PRODUCER_ID", producerId);
        queryWrapper.eq("QUEUE", queue);
        return failConsumerMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public int lockConsumer(String consumerId) {
        return failConsumerMapper.lockConsumer(consumerId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public int unlockConsumer(String consumerId) {
        return failConsumerMapper.unlockConsumer(consumerId);
    }
}
