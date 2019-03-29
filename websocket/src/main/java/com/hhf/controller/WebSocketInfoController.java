package com.hhf.controller;


import com.hhf.common.response.pojo.CommonPageResponse;
import com.hhf.common.response.pojo.CommonResponse;
import com.hhf.service.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-28
 */
@Api(value = "/webSocket",description = "webSocket服务管理模块")
@RestController
@RequestMapping("/api/webSocket")
public class WebSocketInfoController {

    @Autowired
    WebSocketService webSocketService;

    @ApiOperation("推送多条消息")
    @RequestMapping(value = "/sendWebSocketMessage", method = RequestMethod.POST)
    public CommonResponse<String> sendWebSocketMessage(@RequestParam("userLoginCode") String userLoginCode) {
        webSocketService.sendWebSocketMessage(userLoginCode);
        return CommonResponse.buildRespose4Success(null,"发送成功");
    }
}

