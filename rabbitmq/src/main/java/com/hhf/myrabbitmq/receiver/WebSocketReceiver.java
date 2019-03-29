package com.hhf.myrabbitmq.receiver;

import com.hhf.common.utils.BeanUtils;
import com.hhf.common.utils.JacksonUtils;
import com.hhf.model.WebSocketInfoEO;
import com.hhf.myrabbitmq.configuration.CommonMqConfiguration;
import com.hhf.myrabbitmq.constants.RabbitMqConstants;
import com.hhf.myrabbitmq.core.MessageResult;
import com.hhf.myrabbitmq.core.consumer.mq.BaseConsumer;
import com.hhf.myrabbitmq.core.consumer.mq.MqConsumerDurableService;
import com.hhf.myrabbitmq.core.message.WebSocketMessage;
import com.hhf.service.WebSocketInfoService;
import com.hhf.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author huhaifeng
 */
@Component
@DependsOn("commonMqConfiguration")
@RabbitListener(queues = RabbitMqConstants.WEB_SOCKET_QUEUE)
public class WebSocketReceiver extends BaseConsumer<WebSocketMessage> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketReceiver.class);
    @Autowired
    WebSocketService webSocketService;
    @Autowired
    WebSocketInfoService webSocketInfoService;
    @Autowired
    public WebSocketReceiver(CommonMqConfiguration rabbitMqConfiguration, MqConsumerDurableService mqConsumerDurableService) {
        super(rabbitMqConfiguration, mqConsumerDurableService);
    }


    @Override
    public MessageResult handle(WebSocketMessage request) {
        try {
            logger.info("消息发送类为:{}",JacksonUtils.obj2json(request));
            webSocketInfoService.save(message2EO(request));
            logger.info("--开始发送消息--");
            webSocketService.sendMessage(JacksonUtils.obj2json(request));
            logger.info("--消息发送结束--");
        } catch (Exception e) {
            return MessageResult.fail("消息发送测试添加用户person失败，生产者为:"+ request.getProducerId()+",异常原因为:"+e.getMessage());
        }
        //将 返回 结果持久化
        return MessageResult.success();
    }

    private WebSocketInfoEO message2EO(WebSocketMessage message) {
        WebSocketInfoEO eo = new WebSocketInfoEO();
        BeanUtils.copyPropertiesIgnoreNull(message, eo);
        eo.setId(Long.valueOf(message.getProducerId()));
        eo.setCreatedOn(new Date());
        eo.setCreatedBy("system");
        eo.setStatus("0");
        return eo;
    }
}
