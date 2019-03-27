package com.hhf.myrabbitmq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hhf.common.response.pojo.CommonPageResponse;
import com.hhf.common.utils.MybatisPlusPageUtils;
import com.hhf.myrabbitmq.dto.ConsumerDto;
import com.hhf.myrabbitmq.eo.SuccessConsumerEO;
import com.hhf.myrabbitmq.mapper.SuccessConsumerMapper;
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
public class SuccessConsumerServiceImpl implements BaseConsumerEOService<SuccessConsumerEO> {

    @Autowired
    SuccessConsumerMapper successConsumerMapper;

    @Override
    public int insertConsumer(SuccessConsumerEO successConsumerEO) {
        return successConsumerMapper.insert(successConsumerEO);
    }

    @Override
    public int updateConsumer(SuccessConsumerEO successConsumerEO) {
        return successConsumerMapper.updateById(successConsumerEO);
    }

    @Override
    public int deleteConsumer(String id) {
        return successConsumerMapper.deleteById(Long.valueOf(id));
    }

    @Override
    public CommonPageResponse<SuccessConsumerEO> selectConsumers(ConsumerDto consumerMessageDto) {
        IPage<SuccessConsumerEO> iPage = successConsumerMapper.selectConsumers(new Page<SuccessConsumerEO>(consumerMessageDto.getPageNum(), consumerMessageDto.getPageSize()), consumerMessageDto);
        return MybatisPlusPageUtils.newCommonPageResponse(iPage);
    }

    @Override
    public SuccessConsumerEO selectByPrimaryKey(String id) {
        return successConsumerMapper.selectById(Long.valueOf(id));
    }

    @Override
    public SuccessConsumerEO selectConsumerByProducerId(String producerId, String queue) {
        QueryWrapper<SuccessConsumerEO> queryWrapper = new QueryWrapper<SuccessConsumerEO>();
        queryWrapper.eq("PRODUCER_ID", producerId);
        queryWrapper.eq("QUEUE", queue);
        return successConsumerMapper.selectOne(queryWrapper);
    }

    @Override
    public int lockConsumer(String consumerId) {
        return successConsumerMapper.lockConsumer(consumerId);
    }

    @Override
    public int unlockConsumer(String consumerId) {
        return successConsumerMapper.unlockConsumer(consumerId);
    }
}
