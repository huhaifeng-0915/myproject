package com.hhf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author huyongxing
 * @QQ 2739001263
 * @date 2019/2/18 16:39
 */
@Configuration
public class WebSocketConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        log.info("start webSocket config");
        return new ServerEndpointExporter();
    }
}