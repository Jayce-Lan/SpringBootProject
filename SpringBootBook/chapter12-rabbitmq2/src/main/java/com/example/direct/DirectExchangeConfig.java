package com.example.direct;

import com.example.util.RabbitmqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由模式
 * 创建交换机并设置匹配规则
 * Direct路由转发模式是“先匹配，再发送”，即在绑定时设置一个BindingKey，
 * 当消息的RoutingKey匹配队列绑定的BindingKey时，才会被交换机发送到绑定的队列中
 */

@Configuration
public class DirectExchangeConfig {

    /**
     * 队列1
     * 值得注意的是，这里只是定义了队列名称，名称正好与key一致而已
     * 实际交易中所识别的key得是下方 DirectExchangeQ1() 方法绑定的 key
     * 队列名称可以另起
     * @return
     */
    @Bean
    public Queue directQueueQ1() {
        return new Queue(RabbitmqConst.DIRECT_Q1);
    }

    /**
     * 队列2
     * @return
     */
    @Bean
    public Queue directQueueQ2() {
        return new Queue(RabbitmqConst.DIRECT_Q2);
    }

    /**
     * 队列3
     * @return
     */
    @Bean
    public Queue directQueueQ3() {
        return new Queue(RabbitmqConst.DIRECT_Q3);
    }

    /**
     * 定义交换机 Direct类型
     * @return
     */
    @Bean
    public DirectExchange myDirectExchange() {
        return new DirectExchange(RabbitmqConst.DIRECT_EXCHANGE_NAME);
    }

    /**
     * 队列绑定到交换机，再指定一个路由键
     * @return
     */
    @Bean
    public Binding DirectExchangeQ1() {
        return BindingBuilder.bind(directQueueQ1())
                .to(myDirectExchange()).with(RabbitmqConst.DIRECT_Q1);
    }

    /**
     * 队列绑定到交换机，再指定一个路由键
     * @return
     */
    @Bean
    public Binding DirectExchangeQ2() {
        return BindingBuilder.bind(directQueueQ2())
                .to(myDirectExchange()).with(RabbitmqConst.DIRECT_Q2);
    }

    /**
     * 队列绑定到交换机，再指定一个路由键
     * @return
     */
    @Bean
    public Binding DirectExchangeQ3() {
        return BindingBuilder.bind(directQueueQ3())
                .to(myDirectExchange()).with(RabbitmqConst.DIRECT_Q3);
    }
}
