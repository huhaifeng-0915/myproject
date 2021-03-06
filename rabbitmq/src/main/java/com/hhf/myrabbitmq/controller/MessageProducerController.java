package com.hhf.myrabbitmq.controller;


import com.hhf.common.response.constant.ResponseCodeConstant;
import com.hhf.common.response.pojo.CommonResponse;
import com.hhf.myrabbitmq.core.message.MyTestMessage;
import com.hhf.myrabbitmq.core.producer.CommonsProducerHandle;
import com.hhf.myrabbitmq.eo.MessageProducerEO;
import com.hhf.myrabbitmq.utils.MessageSendUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@Api(value = "myRabbitmq", description = "myRabbitmq-producer生产者管理模块")
@RestController
@RequestMapping("/myRabbitmq/producer")
public class MessageProducerController {

    @Autowired
    CommonsProducerHandle commonsProducerHandle;

    @ApiOperation("重发消息")
    @RequestMapping(value = "/reSendMessage", method = RequestMethod.POST)
    public CommonResponse<String> reSendProducerMessage(@RequestBody MessageProducerEO messageProducerEO) {
        boolean result = commonsProducerHandle.resendProducerMessage(String.valueOf(messageProducerEO.getProducerId()));
        if (!result) {
            return CommonResponse.buildRespose4Fail(ResponseCodeConstant.ERROR, "重发失败");
        }
        return CommonResponse.buildRespose4Success(null, "重发成功");
    }

    @ApiOperation("测试一条消息")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public CommonResponse<String> test(@RequestParam("message") String message) {
        MessageSendUtils.sendTestMessage(message);
        return CommonResponse.buildRespose4Success(null, "测试成功");
    }
}

