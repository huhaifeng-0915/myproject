package com.hhf.myrabbitmq.controller;


import com.hhf.common.response.constant.ResponseCodeConstant;
import com.hhf.common.response.pojo.CommonResponse;
import com.hhf.myrabbitmq.core.producer.CommonsProducerHandle;
import com.hhf.myrabbitmq.eo.MessageProducerEO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-27
 */
@Api("myRabbitmq-producer生产者管理模块")
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
}

