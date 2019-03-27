package com.hhf.myrabbitmq.service;

import com.hhf.common.response.pojo.CommonPageResponse;
import com.hhf.myrabbitmq.dto.ConsumerDto;
import com.hhf.myrabbitmq.eo.ConsumerEO;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface BaseConsumerEOService<T extends ConsumerEO> {

    /**
     * 保存一条需要持久化的消息
     * <b>null的属性不会保存，会使用数据库默认值</b>
     *
     * @param t mq持久化的消息
     */
    int insertConsumer(T t);

    /**
     * 根据主键更新一条已经持久化的消息
     * <b>更新属性不为null的值</b>
     *
     * @param t mq持久化的消息
     */
    int updateConsumer(T t);

    /**
     * 根据主键id删除持久化的消息
     *
     * @param id 消息的主键
     * @return 删除的消息的数量
     */
    int deleteConsumer(String id);

    /**
     * 前台页面查询条件
     *
     * @param consumerMessageDto 查询条件
     * @return 结果集
     */
    CommonPageResponse<T> selectConsumers(ConsumerDto consumerMessageDto);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 实体对象
     */
    T selectByPrimaryKey(String id);

    T selectConsumerByProducerId(String producerId, String queue);

    /**
     * 新开事务获取行锁
     *
     * @return 更新数量
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    int lockConsumer(String consumerId);

    /**
     * 新开事务释放行锁
     *
     * @param consumerId 消费者id
     * @return 更新数量
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    int unlockConsumer(String consumerId);
}
