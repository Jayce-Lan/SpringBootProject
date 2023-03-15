package com.example.tut3;

import com.example.myconst.RabbitMQConst;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 发布-订阅模式
 * 发布者发布消息，消费者如果不及时消费，那么消息就会消失
 * 期间，交换机与队列的绑定关系也是临时的
 * AnonymousQueue会生成随机明明的队列
 */
@Profile({"tut3", "pub-sub", "publish-subscribe"})
@Configuration
public class Tut3Config {
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitMQConst.PUB_SUB_EX_CHANGE);
    }

    @Profile("receiver")
    private static class ReceiverConfig {
        /**
         * 当我们连接到Rabbit时，我们需要一个新的空队列。
         * 为此，我们可以用一个随机名称创建一个队列，
         * 或者更好的方法是让服务器为我们选择一个随机队列名称
         * AnonymousQueue - 非持久、排他、自动删除队列
         * 一旦断开使用者连接，队列就会被自动删除
         * 这会使得队列具有随机的队列名称
         * @return
         */
        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        /**
         * 绑定交换机
         * 此处的绑定，由于队列为临时队列，当生产者结束生产
         * @param fanoutExchange
         * @param autoDeleteQueue1
         * @return
         */
        @Bean
        public Binding binding1(FanoutExchange fanoutExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1).to(fanoutExchange);
        }

        @Bean
        public Binding binding2(FanoutExchange fanoutExchange, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2).to(fanoutExchange);
        }

        @Bean
        public Tut3Receiver receiver() {
            return new Tut3Receiver();
        }
    }

    @Profile("sender")
    @Bean
    public Tut3Sender sender() {
        return new Tut3Sender();
    }
}
