package com.hhf.learn.topic.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hhf.learn.topic.sender.TopicHelloSender;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author huhaifeng
 * @since 2019-03-19
 */
@Api(value = "/rabbitmq/topic", description = "Rabbitmq-learn-topic管理模块")
@RestController
@RequestMapping(value= "/rabbitmq/topic")
public class TopicRabbitMQController {

	@Autowired
    private TopicHelloSender helloSender;

	/**
	 * send发送端
	 */
	@ApiOperation("topic生产者发送消息")
	@RequestMapping(value = "/send",method = RequestMethod.POST)
	public void send(@RequestParam("message") String message) {
		helloSender.send(message);
	}

	/**
	 * send发送端
	 */
	@ApiOperation("topic生产者发送消息(10000条)")
	@RequestMapping(value = "/sendManyMessage",method = RequestMethod.POST)
	public void sendManyMessage(@RequestParam("message") String message) {
		for (int i=0;i<10000;i++) {
			helloSender.send(message);
		}
	}
}
