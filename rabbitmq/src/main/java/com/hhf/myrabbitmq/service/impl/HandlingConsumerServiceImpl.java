package com.hhf.myrabbitmq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hhf.common.response.pojo.CommonPageResponse;
import com.hhf.common.utils.MybatisPlusPageUtils;
import com.hhf.myrabbitmq.dto.ConsumerDto;
import com.hhf.myrabbitmq.eo.HandlingConsumerEO;
import com.hhf.myrabbitmq.mapper.HandlingConsumerMapper;
import com.hhf.myrabbitmq.service.BaseConsumerEOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@Service
public class HandlingConsumerServiceImpl implements BaseConsumerEOService<HandlingConsumerEO> {

    @Autowired
    HandlingConsumerMapper handlingConsumerMapper;

    @Override
    public int insertConsumer(HandlingConsumerEO handlingConsumerEO) {
        return handlingConsumerMapper.insert(handlingConsumerEO);
    }

    @Override
    public int updateConsumer(HandlingConsumerEO handlingConsumerEO) {
        return handlingConsumerMapper.updateById(handlingConsumerEO);
    }

    @Override
    public int deleteConsumer(String id) {
        return handlingConsumerMapper.deleteById(Long.valueOf(id));
    }

    @Override
    public CommonPageResponse<HandlingConsumerEO> selectConsumers(ConsumerDto consumerMessageDto) {
        IPage<HandlingConsumerEO> iPage = handlingConsumerMapper.selectConsumerMessageList(new Page<HandlingConsumerEO>(consumerMessageDto.getPageNum(), consumerMessageDto.getPageSize()), consumerMessageDto);
        return MybatisPlusPageUtils.newCommonPageResponse(iPage);
    }

    @Override
    public HandlingConsumerEO selectByPrimaryKey(String id) {
        return handlingConsumerMapper.selectById(Long.valueOf(id));
    }

    @Override
    public HandlingConsumerEO selectConsumerByProducerId(String producerId, String queue) {
        QueryWrapper<HandlingConsumerEO> queryWrapper = new QueryWrapper<HandlingConsumerEO>();
        queryWrapper.eq("PRODUCER_ID", producerId);
        queryWrapper.eq("QUEUE", queue);
        return handlingConsumerMapper.selectOne(queryWrapper);
    }

    @Override
    public int lockConsumer(String consumerId) {
        return handlingConsumerMapper.lockConsumer(consumerId);
    }

    @Override
    public int unlockConsumer(String consumerId) {
        return handlingConsumerMapper.unlockConsumer(consumerId);
    }
}
