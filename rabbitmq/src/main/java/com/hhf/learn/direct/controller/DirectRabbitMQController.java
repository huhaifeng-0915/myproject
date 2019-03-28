package com.hhf.learn.direct.controller;

import com.hhf.learn.direct.receive.DirectHelloReceive;
import com.hhf.learn.direct.receive.PersonReceive;
import com.hhf.learn.direct.sender.PersonSender;
import com.hhf.learn.direct.sender.DirectHelloSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-19
 */
@Api(value = "/rabbitmq/direct", description = "Rabbitmq-direct管理模块")
@RestController
@RequestMapping(value= "/rabbitmq/direct")
public class DirectRabbitMQController {

	@Autowired
    private DirectHelloSender helloSender;
	@Autowired
	private DirectHelloReceive helloReceive;
	@Autowired
	private PersonSender personSender;
	@Autowired
	private PersonReceive personReceive;


	@ApiOperation("direct发送消息")
	/**
	 * send发送端
	 */
	@RequestMapping(value = "/send",method = RequestMethod.POST)
	public void send() {
		helloSender.send(1);
	}

	@ApiOperation("direct发送10000消息")
	/**
	 * send发送端
	 */
	@RequestMapping(value = "/sendsendManyMessage",method = RequestMethod.POST)
	public void sendManyMessage() {
		for(int i = 0;i <=10000;i++) {
			helloSender.send(i);
		}
	}

	@ApiOperation("direct接收消息")
	/**
	 * receive接收端
	 */
	@RequestMapping(value = "/receive",method = RequestMethod.POST)
	public void receive() {
		 helloReceive.processC("queue");
	}


	@ApiOperation("direct发送消息(以对象形式)")
	/**
	 * sendObject发送端
	 */
	@RequestMapping(value = "/sendObject",method = RequestMethod.POST)
	public void sendObject() {
		personSender.send();
	}
	
}
