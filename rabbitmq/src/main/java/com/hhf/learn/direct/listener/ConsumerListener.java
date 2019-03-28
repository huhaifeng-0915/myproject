package com.hhf.learn.direct.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.RabbitUtils;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

//@Component
public class ConsumerListener implements ChannelAwareMessageListener {

	private static final Logger log = LoggerFactory.getLogger(ConsumerListener.class);

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		try {
			log.info("consumer--:{},message.getBody():{}",message.getMessageProperties(),new String(message.getBody()));
			// deliveryTag是消息传送的次数，我这里是为了让消息队列的第一个消息到达的时候抛出异常，
			//处理异常让消息重新回到队列，然后再次抛出异常，处理异常拒绝让消息重回队列
			log.info("消息传送次数：{}",message.getMessageProperties().getDeliveryTag());
			/*if (message.getMessageProperties().getDeliveryTag() == 1
					|| message.getMessageProperties().getDeliveryTag() == 2) {
				throw new Exception();
			}*/
			// false只确认当前一个消息收到，true确认所有consumer获得的消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); 
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Redelivered:{}",message.getMessageProperties().getRedelivered());
			if (message.getMessageProperties().getRedelivered()) {
				log.info("消息已重复处理失败,拒绝再次接收...");
				//TODO 处理重复消费消息失败的逻辑，如将失败的消息记录到数据库中等
				channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); // 拒绝消息
			} else {
				log.info("消息即将再次返回队列处理...");
				//requeue为是否重新回到队列
				//消费者再次消费消息
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);  
				
			}
		}
	}
}
