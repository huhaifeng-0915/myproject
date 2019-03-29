package com.hhf.myrabbitmq.receiver;

import com.hhf.common.utils.JacksonUtils;
import com.hhf.demo.mapper.PersonMapper;
import com.hhf.myrabbitmq.configuration.CommonMqConfiguration;
import com.hhf.myrabbitmq.constants.RabbitMqConstants;
import com.hhf.myrabbitmq.core.MessageResult;
import com.hhf.myrabbitmq.core.consumer.mq.BaseConsumer;
import com.hhf.myrabbitmq.core.consumer.mq.MqConsumerDurableService;
import com.hhf.myrabbitmq.core.message.AddPersonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;


/**
 * @author huhaifeng
 */
@Component
@DependsOn("commonMqConfiguration")
@RabbitListener(queues = RabbitMqConstants.ADD_PERSON)
public class AddPersonReceiver extends BaseConsumer<AddPersonMessage> {

    private final PersonMapper personMapper;
    private static final Logger logger = LoggerFactory.getLogger(AddPersonReceiver.class);

    @Autowired
    public AddPersonReceiver(CommonMqConfiguration rabbitMqConfiguration, MqConsumerDurableService mqConsumerDurableService,
                             PersonMapper personMapper) {
        super(rabbitMqConfiguration, mqConsumerDurableService);
        this.personMapper = personMapper;
    }


    @Override
    public MessageResult handle(AddPersonMessage request) {
        try {
            logger.info("消息发送测试添加用户person，用户名为:{}",request.getPerson().getUserName());
            logger.info("消息发送测试添加用户person，密码为:{}",request.getPerson().getUserPwd());
            int result = personMapper.insert(request.getPerson());
        } catch (Exception e) {
            return MessageResult.fail("消息发送测试添加用户person失败，用户名为:"+ request.getPerson().getUserName()+",密码为:" + request.getPerson().getUserName()+",异常原因为:"+e.getMessage());
        }
        //将 返回 结果持久化
        logger.info("接口调用成功,返回的报文为：{}", JacksonUtils.obj2json(request.getPerson()));
        return MessageResult.success();
    }
}
