package com.hhf.direct.sender;

import com.hhf.demo.model.Person;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 采用AmqpTemplate发送消息
 *
 * @author hhf1311843248
 */
@Component
public class PersonSender {
    @Autowired
    private AmqpTemplate template;

    public void send() {
        Person person = new Person();
//    admin.setUserId(1);
        person.setUserName("rabbitmq测试用户");
        person.setUserPwd("123456");
        person.setCreatedBy("胡海丰");
        person.setCreatedOn(new Date());
        template.convertAndSend("queueObject", person);
    }
}
