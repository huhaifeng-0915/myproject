package com.hhf.web;


import com.hhf.socket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huyongxing
 * @QQ 2739001263
 * @date 2019/2/18 16:46
 */
@Controller
public class DemoController {

    @Autowired
    private WebSocket webSocket;

    @RequestMapping("/exec")
    @ResponseBody
    public Object exec() {
        webSocket.sendMessage("服务器发消息咯!");
        return "发过来了，看看有反应没";
    }

    @RequestMapping("/notice")
    public String page(){
        return "notice";
    }
}
