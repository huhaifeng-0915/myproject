package com.hhf.service;

/**
 * webSocket服务类
 * @author huhaifeng
 */
public interface WebSocketService {

    /**
     * 发送消息
     */
    public void sendMessage(String message);

    /**
     * 推送多条消息
     */
    public void sendWebSocketMessage(String userLoginCode);
}
