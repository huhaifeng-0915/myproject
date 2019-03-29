package com.hhf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hhf.common.utils.JacksonUtils;
import com.hhf.model.WebSocketInfoEO;
import com.hhf.service.WebSocketInfoService;
import com.hhf.service.WebSocketService;
import com.hhf.socket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private WebSocket webSocket;
    @Autowired
    WebSocketInfoService webSocketInfoService;
    @Override
    public void sendMessage(String message) {
        webSocket.sendMessage(message);
    }

    @Override
    public void sendWebSocketMessage(String userLoginCode) {
        QueryWrapper<WebSocketInfoEO> queryWrapper = new QueryWrapper<WebSocketInfoEO>();
        queryWrapper.eq("RECEIVE",userLoginCode);
        List<WebSocketInfoEO> list = webSocketInfoService.list(queryWrapper);
        webSocket.sendMessage(JacksonUtils.obj2json(list));
    }
}
