package org.example.confirmselect;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import org.example.util.RabbitMQConfigDiction;
import org.example.util.RabbitMQTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认的生产者
 */
public class ConfirmSelectTask {
    public static void main(String[] args) {
        ConfirmSelectTask confirmSelectTask = new ConfirmSelectTask();
        // 单个发布确认
//        confirmSelectTask.testWorkQueueConfirmSelectIndividuallySent();
        // 批量发布确认
//        confirmSelectTask.testWorkQueueConfirmSelectBatchSent();
        // 异步发布确认
//        confirmSelectTask.testWorkQueueConfirmAsyncSent();
        List<String> list = new ArrayList<>();
        // 删除无用的持久化队列
        list.add("509e7cc0-dacd-47b9-ae08-f95ee3a2cfa7");
        list.forEach(RabbitMQTestUtils::deleteDurableQueue);
    }

    /**
     * 单个发布确认
     * Total execution time is [548ms]
     * 队列持久化 - channel.queueDeclare 中的 durable = true
     * 消息持久化 - channel.basicPublish 中的 MessageProperties.PERSISTENT_TEXT_PLAIN 参数
     * 发布确认 - channel.confirmSelect()
     * 单个消息发布确认 - channel.waitForConfirms()
     */
    private void testWorkQueueConfirmSelectIndividuallySent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            // 持久化队列Queue的参数-durable
            boolean durable = true;
            // 随机获取队列名称
            final String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, durable, false, false, null);
            // 开启发布确认
            channel.confirmSelect();
            // 开始时间
            final long beginTime = System.currentTimeMillis();
            // 批量发消息，用于测试耗时
            for (int i = 0; i < RabbitMQConfigDiction.MESSAGE_COUNT; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                // 单个消息发布确认
                if (channel.waitForConfirms()) {
                    System.out.println("message is confirm, success, the message is [" + message + "]");
                }
            }
            final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time is [" + (endTime - beginTime) + "ms]");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量发布确认
     * Total execution time is [66ms]
     * 队列持久化 - channel.queueDeclare 中的 durable = true
     * 消息持久化 - channel.basicPublish 中的 MessageProperties.PERSISTENT_TEXT_PLAIN 参数
     * 发布确认 - channel.confirmSelect()
     * 批量消息发布确认 - 定义批量确认长度: final int confirmCount = 100;/  channel.waitForConfirms()
     */
    private void testWorkQueueConfirmSelectBatchSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            // 持久化队列Queue的参数-durable
            boolean durable = true;
            // 随机获取队列名称
            final String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, durable, false, false, null);
            // 开启发布确认
            channel.confirmSelect();
            // 定义批量确认长度
            final int confirmCount = 100;
            // 开始时间
            final long beginTime = System.currentTimeMillis();
            // 批量发消息，用于测试耗时
            for (int i = 0; i < RabbitMQConfigDiction.MESSAGE_COUNT; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
                // 达到100条批量确认一次
                if ((i + 1) % confirmCount == 0) {
                    if (channel.waitForConfirms()) {
                        System.out.println("message is confirm, success, now count is [" + message + "]");
                    }
                }
            }
            final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time is [" + (endTime - beginTime) + "ms]");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步发布确认
     * Total execution time is [25ms]
     * 队列持久化 - channel.queueDeclare 中的 durable = true
     * 消息持久化 - channel.basicPublish 中的 MessageProperties.PERSISTENT_TEXT_PLAIN 参数
     * 发布确认 - channel.confirmSelect()
     * 监听器 - channel.addConfirmListener(ackCallBack, nackCallBack);
     *       - 成功回调函数：ackCallBack
     *       - 失败回调函数：nackCallBack
     */
    private void testWorkQueueConfirmAsyncSent() {
        try (Channel channel = RabbitMQTestUtils.getChannel()) {
            // 持久化队列Queue的参数-durable
            boolean durable = true;
            // 随机获取队列名称
            final String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, durable, false, false, null);
            // 开启发布确认
            channel.confirmSelect();
            /**
             * 在开启发布确认后，先准备出一个线程安全有序的hash表，适用于高并发的情况
             * 1.可以轻松地让消息与消息关联
             * 2.轻松批量删除条目，只需要key值
             * 3.支持高并发（多线程）
             */
            ConcurrentSkipListMap<Long, Object> outstandingConfirms = new ConcurrentSkipListMap<>();
            // 消息监听器，监听消息发送情况；要在发送消息前优先准备，否则可能监听不到broker返回的信息
            // 成功回调函数（消息标识，是否为批量确认）
            ConfirmCallback ackCallBack = (deliveryTag, multiple) -> {
                // 如果是批量确认，那么则获取到批量信息后批量删除
                if (multiple) {
                    ConcurrentNavigableMap<Long, Object> confirmed = outstandingConfirms.headMap(deliveryTag);
                    confirmed.clear();
                } else {
                    // 单个确认时调用单个删除
                    outstandingConfirms.remove(deliveryTag);
                }
                System.out.println("the success message's key is [" + deliveryTag + "]");
            };
            // 失败回调函数（消息标识，是否为批量确认）
            ConfirmCallback nackCallBack = (deliveryTag, multiple) -> {
                String nackMessage = (String) outstandingConfirms.get(deliveryTag);
                System.out.println("the loss message's key is [" + deliveryTag + "]");
                System.out.println("the loss message is [" + nackMessage + "]");
            };
            channel.addConfirmListener(ackCallBack, nackCallBack);
            // 开始时间
            final long beginTime = System.currentTimeMillis();
            // 批量发消息，用于测试耗时
            for (int i = 0; i < RabbitMQConfigDiction.MESSAGE_COUNT; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
                // 1.记录下所有消息的总和
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            }
            final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time is [" + (endTime - beginTime) + "ms]");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
