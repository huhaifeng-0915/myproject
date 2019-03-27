package com.hhf.myrabbitmq.core.consumer.local;

import com.hhf.myrabbitmq.core.MessageResult;
import com.hhf.myrabbitmq.core.QueueMessage;
import com.hhf.myrabbitmq.core.constant.MessageConstant;
import com.hhf.myrabbitmq.core.consumer.ConsumerService;
import com.hhf.myrabbitmq.enums.ConcurrencyStatus;
import com.hhf.myrabbitmq.enums.ConsumerType;
import com.hhf.myrabbitmq.eo.ConsumerEO;
import com.hhf.myrabbitmq.eo.FailConsumerEO;
import com.hhf.myrabbitmq.eo.HandlingConsumerEO;
import com.hhf.myrabbitmq.eo.SuccessConsumerEO;
import com.hhf.myrabbitmq.service.impl.FailConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.HandlingConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.SuccessConsumerServiceImpl;
import com.hhf.myrabbitmq.utils.ExceptionUtil;
import com.hhf.myrabbitmq.utils.SystemHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 处理本地的重发消费者
 *
 * @author _g5niusx
 */
@Slf4j
public class HandleLocalConsumerServiceImpl implements HandleLocalConsumerService {
    private final FailConsumerServiceImpl failConsumerService;
    private final SuccessConsumerServiceImpl successConsumerService;
    private final HandlingConsumerServiceImpl handlingConsumerService;

    public HandleLocalConsumerServiceImpl(FailConsumerServiceImpl failConsumerService, SuccessConsumerServiceImpl successConsumerService, HandlingConsumerServiceImpl handlingConsumerService) {
        this.failConsumerService = failConsumerService;
        this.successConsumerService = successConsumerService;
        this.handlingConsumerService = handlingConsumerService;
    }

    @Override
    public <T extends QueueMessage> void handle(ConsumerType consumerType, ConsumerService<T> consumerService, T t, String queueName, String consumerId) {
        log.info("重发任务,消费id为[{}],队列为[{}],类型为[{}],消息为[{}]", consumerId, queueName, consumerType, t.toString());
        ConsumerEO consumerEO = getConsumerEO(consumerType, consumerId);
        consumerEO.setConsumerIp(SystemHelper.getSystemLocalIpStr());
        consumerEO.setConcurrencyStatus(ConcurrencyStatus.LOCK.getValue());
        consumerEO.setConsumerTimes(consumerEO.getConsumerTimes() + 1);
        consumerEO.setConsumerTime(new Date());
        HandlingConsumerEO handlingConsumerEO = new HandlingConsumerEO(consumerEO);
        MessageResult messageResult      = null;
        try {
            insertHandlingConsumer(consumerType, handlingConsumerEO);
            messageResult = consumerService.handle(t);
        } catch (Exception e) {
            messageResult = MessageResult.fail(ExceptionUtil.getErrorMsg(e));
            log.error("重发任务[{}]处理失败", consumerService.getClass().toString(), e);
        } finally {
            if (messageResult == null) {
                messageResult = MessageResult.fail("失败任务返回为空" + consumerService.getClass().toString());
            }
            /*消费完毕以后更新表信息和状态*/
            updateResendConsumerMessage(handlingConsumerEO, messageResult);
        }
    }

    /**
     * 根据类型获取对应的消费消息
     *
     * @param consumerType 消费类型
     * @param consumerId   消费id
     * @return 消费¬实体
     */
    private ConsumerEO getConsumerEO(ConsumerType consumerType, String consumerId) {
        if (ConsumerType.HANDLING.equals(consumerType)) {
            return handlingConsumerService.selectByPrimaryKey(consumerId);
        } else if (ConsumerType.FAIL.equals(consumerType)) {
            return failConsumerService.selectByPrimaryKey(consumerId);
        } else {
            return successConsumerService.selectByPrimaryKey(consumerId);
        }
    }

    @Transactional
    public void insertHandlingConsumer(ConsumerType consumerType, HandlingConsumerEO consumerToEO) {
        if (ConsumerType.HANDLING.equals(consumerType)) {
            /*下发中的信息重发直接更新次数和消费时间*/
            handlingConsumerService.updateConsumer(consumerToEO);
            return;
        } else if (ConsumerType.SUCCESS.equals(consumerType)) {
            /*成功表重发的时候删除旧表数据*/
            successConsumerService.deleteConsumer(String.valueOf(consumerToEO.getConsumerId()));
        } else {
            /*失败表重发的时候删除旧表数据*/
            failConsumerService.deleteConsumer(String.valueOf(consumerToEO.getConsumerId()));
        }
        handlingConsumerService.insertConsumer(consumerToEO);
    }

    @Transactional
    public void updateResendConsumerMessage(HandlingConsumerEO consumerToEO, MessageResult result) {
        consumerToEO.setResultCode(result.getResultCode());
        consumerToEO.setResult(result.getResultMsg());
        consumerToEO.setConcurrencyStatus(ConcurrencyStatus.UN_LOCK.getValue());
        if (MessageConstant.SUCCESS.equals(result.getResultCode())) {
            successConsumerService.insertConsumer(new SuccessConsumerEO(consumerToEO));
        } else {
            failConsumerService.insertConsumer(new FailConsumerEO(consumerToEO));
        }
        handlingConsumerService.deleteConsumer(String.valueOf(consumerToEO.getConsumerId()));
    }
}
