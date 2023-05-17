package com.example;

import com.example.ack.Sender;
import com.example.consts.DictConst;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RabbitmqAckApplicationTests {
	@Resource
	private RabbitTemplate rabbitTemplate;

	@Test
	void contextLoads() {
		rabbitTemplate.convertAndSend(DictConst.ACK_EXCHANGE_NAME, DictConst.ACK_ROUTING_KEY, "hello");
	}

}
