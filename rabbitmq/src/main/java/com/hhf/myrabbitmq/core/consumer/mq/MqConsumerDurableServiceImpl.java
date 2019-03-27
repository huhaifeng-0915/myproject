package com.hhf.myrabbitmq.core.consumer.mq;

import com.hhf.myrabbitmq.core.constant.MessageConstant;
import com.hhf.myrabbitmq.enums.ConcurrencyStatus;
import com.hhf.myrabbitmq.eo.ConsumerEO;
import com.hhf.myrabbitmq.eo.FailConsumerEO;
import com.hhf.myrabbitmq.eo.HandlingConsumerEO;
import com.hhf.myrabbitmq.eo.SuccessConsumerEO;
import com.hhf.myrabbitmq.exception.CommonMqException;
import com.hhf.myrabbitmq.service.impl.FailConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.HandlingConsumerServiceImpl;
import com.hhf.myrabbitmq.service.impl.SuccessConsumerServiceImpl;
import com.hhf.myrabbitmq.utils.SystemHelper;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;

/**
 * @author huhaifeng
 * @description 持久化服务类
 */
public class MqConsumerDurableServiceImpl implements MqConsumerDurableService {

    private FailConsumerServiceImpl failConsumerService;
    private SuccessConsumerServiceImpl successConsumerService;
    private HandlingConsumerServiceImpl handlingConsumerService;

    public MqConsumerDurableServiceImpl(FailConsumerServiceImpl failConsumerService,
                                        SuccessConsumerServiceImpl successConsumerService,
                                        HandlingConsumerServiceImpl handlingConsumerService) {
        this.failConsumerService = failConsumerService;
        this.successConsumerService = successConsumerService;
        this.handlingConsumerService = handlingConsumerService;
    }

    /**
     * 持久化一条mq重发的消费消息
     *
     * @param producerId 生产者id
     * @param queue      队列名称
     */
    @Override
    public ConsumerEO durableMqResendConsumer(String producerId, String queue) {
        FailConsumerEO failConsumerEO     = failConsumerService.selectConsumerByProducerId(producerId, queue);
        SuccessConsumerEO successConsumerEO  = successConsumerService.selectConsumerByProducerId(producerId, queue);
        HandlingConsumerEO handlingConsumerEO = handlingConsumerService.selectConsumerByProducerId(producerId, queue);
        if (failConsumerEO != null) {
            /*表数据从失败表转移到下发中的表*/
            ConsumerEO consumerEO = fillConsumerEO(failConsumerEO);
            failConsumerService.deleteConsumer(String.valueOf(failConsumerEO.getConsumerId()));
            handlingConsumerService.insertConsumer(new HandlingConsumerEO(failConsumerEO));
            return consumerEO;
        } else if (successConsumerEO != null) {
            /*表数据从成功表转移到下发中的表*/
            ConsumerEO consumerEO = fillConsumerEO(successConsumerEO);
            successConsumerService.deleteConsumer(String.valueOf(successConsumerEO.getConsumerId()));
            handlingConsumerService.insertConsumer(new HandlingConsumerEO(successConsumerEO));
            return consumerEO;
        } else if (handlingConsumerEO != null) {
            /*更新下发中的表数据*/
            ConsumerEO consumerEO = fillConsumerEO(handlingConsumerEO);
            consumerEO.setUpdatedOn(new Date());
            handlingConsumerService.updateConsumer(new HandlingConsumerEO(consumerEO));
            return consumerEO;
        } else {
            throw new CommonMqException(MessageFormat.format("消息处理表中不存在生产者ID[{0}],队列[{1}]的数据!!", producerId, queue));
        }
    }

    @Override
    public int durableMqConsumer(ConsumerEO consumerEO) {
        if (StringUtils.isEmpty(consumerEO.getConsumerId())) {
            throw new CommonMqException("消费信息的id不能为空！！！");
        }
        return handlingConsumerService.insertConsumer(new HandlingConsumerEO(consumerEO));
    }

    @Override
    public int completeConsumer(ConsumerEO consumerEO) {
        consumerEO.setResult(subResult(consumerEO.getResult()));
        consumerEO.setConcurrencyStatus(ConcurrencyStatus.UN_LOCK.getValue());
        if (MessageConstant.SUCCESS.equals(consumerEO.getResultCode())) {
            successConsumerService.insertConsumer(new SuccessConsumerEO(consumerEO));
        } else {
            failConsumerService.insertConsumer(new FailConsumerEO(consumerEO));
        }
        return handlingConsumerService.deleteConsumer(String.valueOf(consumerEO.getConsumerId()));
    }

    /**
     * 截取处理结果
     *
     * @param result 处理结果
     * @return 截取后的字符串
     */
    private String subResult(String result) {
        if (StringUtils.isEmpty(result)) {
            return result;
        }
        if (result.length() > 200) {
            result = result.substring(0, 200);
        }
        return result;
    }

    /**
     * 重发消息的时候填充一些更新的值
     *
     * @param consumerEO 消费记录
     */
    private ConsumerEO fillConsumerEO(ConsumerEO consumerEO) {
        if(consumerEO != null){
        consumerEO.setConsumerIp(SystemHelper.getSystemLocalIpStr());
        consumerEO.setConsumerTimes(consumerEO.getConsumerTimes() == null ? 1 : consumerEO.getConsumerTimes() + 1);
        consumerEO.setConsumerTime(new Date());
        consumerEO.setConcurrencyStatus(ConcurrencyStatus.LOCK.getValue());
            consumerEO.setResult("");
        }
        return consumerEO;
    }
}
