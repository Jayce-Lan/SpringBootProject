package com.example.tut5;

import com.example.myconst.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 基于 topic 模式下的配置类
 */
@Profile({"tut5", "topics"})
@Configuration
@Slf4j
public class Tut5Config {
    /**
     * TopicExchange
     * 前面所使用的 DirectExchange 虽然改进了我们的系统，但它仍然有局限性——它不能基于多个标准进行路由。
     * 在我们的消息传递系统中，我们可能不仅希望根据路由键订阅队列，还希望根据产生消息的源订阅队列。
     * 我们可能只需要监听来自'cron'的严重错误，也可以监听来自'kern'的所有日志。
     * 为了在我们的日志系统中实现这种灵活性，我们需要了解更复杂的 TopicExchange。
     * @return TopicExchange - 主题交换（路由交换的进阶）
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitMQConst.TOPIC_EXCHANGE);
    }

    @Profile("receiver")
    private static class ReceiverConfig {
        @Bean
        public Queue autoDeleteQueue1() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue2() {
            return new AnonymousQueue();
        }

        @Bean
        public Tut5Receiver receiver() {
            return new Tut5Receiver();
        }

        /**
         * 发送到TopicExchange的消息不能有任意的routing_key -它必须是一个由点分隔的单词列表
         * 这些词可以是任何东西，但通常它们指定了与消息相关的一些特征。
         * 路由键中可以有任意多的字，最大限制为255字节。
         * 绑定键有两个重要的特殊情况:
         *   *(星号)可以代替一个单词。
         *   #(警号)可以代替零个或多个单词
         *   当无法匹配时，生产者生产当数据将丢失，无法被消费
         * 当我们使用"#"作为routingKey时，它将接收所有消息，而不管路由键是什么，就像fanoutExchange一样
         * 当绑定中不使用特殊字符“*”(星号)和“#”(警号)时（只使用字符串），主题交换的行为就像directExchange一样
         * @param topicExchange
         * @param autoDeleteQueue1
         * @return
         */
        @Bean
        public Binding binding1a(TopicExchange topicExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1)
                    .to(topicExchange)
                    .with("*.orange.*");
        }

        @Bean
        public Binding binding1b(TopicExchange topicExchange, Queue autoDeleteQueue1) {
            return BindingBuilder.bind(autoDeleteQueue1)
                    .to(topicExchange)
                    .with("*.*.rabbit");
        }

        @Bean
        public Binding binding2a(TopicExchange topicExchange, Queue autoDeleteQueue2) {
            return BindingBuilder.bind(autoDeleteQueue2)
                    .to(topicExchange)
                    .with("lazy.#");
        }
    }

    @Profile("sender")
    @Bean
    public Tut5Sender sender() {
        return new Tut5Sender();
    }
}
