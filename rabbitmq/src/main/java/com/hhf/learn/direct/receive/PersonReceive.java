package com.hhf.learn.direct.receive;

import com.hhf.demo.mapper.PersonMapper;
import com.hhf.demo.model.Person;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonReceive {
    @Autowired
    PersonMapper personMapper;

    @RabbitListener(queues = "queueObject")    //监听器监听指定的Queue
    public void processC(Person person) {
        System.out.println(person);
        int result = personMapper.insert(person);
        System.out.println("queue测试异步添加结果:" + result);
    }
}