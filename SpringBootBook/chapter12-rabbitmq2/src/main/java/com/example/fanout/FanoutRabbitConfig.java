package com.example.fanout;

import com.example.util.RabbitmqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fanout 广播模式（订阅模式）
 * 每个发送到Fanout类型交换机的消息都会分到所有绑定的队列上。
 * Fanout交换机不处理路由键，只是简单地将队列绑定到交换机上，
 * 每个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上
 * Fanout模式很像子网广播，每台子网内的主机都获得了一份复制的消息。
 * Fanout类型转发消息是最快的
 *
 * 先创建Fanout规则配置类FanoutRabbitConfig
 * 再创建对应的Exchange、Queue，并将队列绑定到交换机上
 */

@Configuration
public class FanoutRabbitConfig {
    /**
     * 定义队列
     * @return 队列1
     */
    @Bean
    public Queue Q1Message() {
        return new Queue(RabbitmqConst.FANOUT_Q1);
    }

    /**
     * 定义队列
     * @return 队列2
     */
    @Bean
    public Queue Q2Message() {
        return new Queue(RabbitmqConst.FANOUT_Q2);
    }

    /**
     * 定义队列
     * @return 队列3
     */
    @Bean
    public Queue Q3Message() {
        return new Queue(RabbitmqConst.FANOUT_Q3);
    }

    /**
     * 定义交换机
     * 首先定义了交换机fanoutExchange，
     * 然后分别定义了Q1、Q2、Q3三个队列，
     * 最后将三个队列绑定到Fanout交换机上
     * @return 广播模式交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitmqConst.FANOUT_EXCHANGE);
    }

    /**
     * 队列与交换机绑定
     * @param Q1Message 队列
     * @param fanoutExchange 交换机
     * @return
     */
    @Bean
    public Binding bindingExchangeQ1(Queue Q1Message, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(Q1Message)
                .to(fanoutExchange);
    }

    /**
     * 队列与交换机绑定
     * @param Q2Message 队列
     * @param fanoutExchange 交换机
     * @return
     */
    @Bean
    public Binding bindingExchangeQ2(Queue Q2Message, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(Q2Message)
                .to(fanoutExchange);
    }

    /**
     * 队列与交换机绑定
     * @return
     */
    @Bean
    public Binding bindingExchangeQ3() {
        return BindingBuilder.bind(Q3Message())
                .to(fanoutExchange());
    }
}
