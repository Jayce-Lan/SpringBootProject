# Rabbit MQ学习

Rabbit MQ是一个消息中间件，它接受并转发消息。可以把它当做一个快递站点，当需要发送一个包裹时，把包裹放在快递站，快递员最终会把包裹送到收件人处。按照这个逻辑，Rabbit MQ是一个快递站，一个快递员帮助传递快件。Rabbit MQ与快递站的区别是，它不处理快件而是接收、存储和转发消息数据。

## MQ基本概念

### 为什么要使用MQ

> 流量削峰

如果订单系统最多能处理一万次订单，这个处理能力正常时绰绰有余。但是高峰时两万次下单就无法处理，只能限制订单超过一万后不允许用户下单，使用消息队列做缓冲（排队），我们可以取消这个限制，把一秒内下单分散成一段时间处理，这时候有些用户可能需要下单后十几秒才有成功反馈，但总比不能下单的体验要好。

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq1-llxf.png)

> 降低藕合调用给用户带来的不流畅体验

 假如一个系统有订单系统、库存系统、物流系统、支付系统。用户创建订单后如果耦合调用库存系统、物流系统、支付系统，任何一个系统出故障订单都会异常。当转变为基于队列的方式后，系统间调用的问题会少很多。假如物流系统发生故障需要几分钟修复，在这几分钟里物流系统要处理的内存被缓存在消息队列中，用户的下单可以正常完成。当物流系统恢复后，继续处理订单信息即可，中单用户不会感受到系统故障，提升系统的可用性。

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq2-yyjo.png)

> 异步处理

有些服务间调用是异步的，例如A调用B，B需要花很长时间执行，但是A需要知道B什么时候可以完成执行。

以前一般有两种形式：

- A过一段时间调用B的API查询

- A提供一个CALL_BACK的API，B执行接收函调用API通知A服务

使用MQ可以使用消息总线很方便的解决这个问题：

A调用B服务后，只需要监听B处理完成的信息，当B处理完成后，会发送一条消息给MQ，MQ会将此消息转发给A服务。这样，A服务既不用循环调用B的查询API，也不用提供CALL_BACK API。同样B服务也不需要这些操作。A服务还能及时得到异步处理成功的消息。

<img title="" src="file:///Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq3-ybcl.png" alt="" width="288">

### MQ分类与对比

| MQ类型     | 优点                                                                                                                                                                                                                                           | 缺点                                                                                                                      |
| -------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| ActiveMQ | 单机吞吐量万级，时效性ms级，可用性高，基于主从框架实现高可用性，消息可靠性较低的概率丢失数据                                                                                                                                                                                              | 官方社区现在对Active MQ 5.x的维护越来越少，高吞吐量场景较少使用                                                                                  |
| Kafka    | 性能卓越，单机写入TPS约在百万条/秒，最大的优点就是吞吐量高。时效性ms级可用性非常高。Kafka是分布式的，一个数据多个副本，少数机器宕机不会丢失数据，不会导致不可用。消费者采用Pull方式获取消息，消息有序，通过控制能够保证所有消息被消费且仅被消费一次，有优秀的第三方Kafka Web管理界面Kafka-Manager；在日志领域比较成熟，被多家公司和多个开源项目使用。功能支持：功能较为简单，主要支持简单的MQ功能，在大数据领域的实时计算以及日志采集被大规模使用 | Kafka单机超过64个队列/分区，Load会发生明显的飙高现象，队列越多，load越高，发送消息响应时间变长，使用短轮询方式，实时性取决于轮询时间间隔，消费失败不支持重试；支持消息顺序，但是一台代理宕机后，就会产生消息乱序，社区更新较慢 |
| RocketMQ | 单机吞吐量十万级，可用性非常高，分布式架构，消息可以做到0丢失，MQ功能较为晚上，还是分布式的，扩展性好，支持10亿级别的消息堆积，不会因为堆积导致性能下降，源码是Java，因此便于Java开发者阅读源码，定制自己的MQ                                                                                                                               | 支持的客户端语言不多，目前是Java、C++，其中C++不成熟；社区活跃度一般，没有在MQ核心中去实现JMS等接口，有些系统要迁移需要修改大量代码                                               |
| RabbitMQ | 由于erlang语言的高并发特征，性能较好；吞吐量打到万级，MQ功能比较完备，健壮、文档、易用、跨平台、支持多语言（如Python、Ruby、.NET、Java、JMS、C、PHP、ActionScript、XMPP、STOMP等），支持AJAX文档齐全；开源提供的管理界面简洁实用，社区活跃度高；更新频率相当高                                                                                 | 商业版需要收费，学习成本较高                                                                                                          |

> *tips1：Kafka被称为大数据的杀手锏，谈到大数据领域内的消息传输则绕不开Kafka，这款为大数据而生的消息中间件以其百万级TPS的吞吐量名声大噪，迅速成为大数据领域的宠儿，在数据采集、传输、存储的过程中发挥着举足轻重的作用。目前已经被LinkedIn、Uber、Twitter、Netflix等大公司采纳。* 
> 
> *tips2：RocketMQ出自阿里巴巴的开源产品，用Java语言实现，在设计时参考了Kafka，并做出了自己的一些改进。被阿里巴巴广泛应用在订单、交易、充值、流计算、消息推送、日志流式处理、binglog分发等场景。*
> 
> *tips3：RabbitMQ在2007年发布，是一个在AMQP（高级消息队列协议）基础上完成的，可复用的企业消息系统，是当前最主流的消息中间件之一。*

### Rabbit MQ四大核心概念

> 生产者

产生数据发送信息的程序是生产者

> 交换机

交换机是Rabbit MQ非常重要的一个部件，一方面它接收来自生产者的消息，另一方面它将信息推送到队列中。交换机必须确切知道如何处理它接收到的消息，是将这些消息推送到特定队列还是推送到多个队列，亦或是把消息丢弃，这个得交换机决定

> 队列

队列是RabbitMQ内部使用的一种数据结构，尽管消息流经Rabbit MQ和应用程序，但它们只能存储在队列中。队列仅受主机的内存和磁盘限制的约束，本质上是一个打的消息缓冲区。许多生产者可以将消息发送到一个队列，许多消费者可以尝试从一个队列接收数据。这就是我们使用队列的方式

（一个消费者对应一个队列；但是一个生产者可以对应多个队列）

> 消费者

消费与接收举要相似的含义。消费者大多时候是一个等待接收消息的程序。请注意，生产者、消费者和消息中间件很多事实并不在同一机器上。同一个应用程序既可以是生产者，又可以是消费者

### Rabbit MQ核心部分（六大模式）

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq4-hxms.png)

简单模式（Hello World）

工作模式（Work queues）

发布-订阅模式（Publish/Subscribe）

路由模式（Routing）

主题模式（Topics）

发布确认模式（Publisher Confirms）

*RPC模式* 

### RabbitMQ工作原理

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq5-gzyl.png)

**Broker**：接收和分发消息的应用，Rabbit MQ Server就是Message Broker

**Virtual host**：出于多租户和安全因素设计的，把AMQP的基本组件划分到一个虚拟的分组中，类似于网络中的namespace概念。当多个不同的用户使用同一个Rabbit MQ Server提供的服务时，可以划分出多个vhost，每个用户在自己的vhost创建Exchange/Queue等（等同于SQL中的数据库database，在管理界面中的Admin-右侧可以看到）

**Connection**： publisher/consumer和Broker之间的TPC连接

**Channel**：如果每一次访问Rabbit MQ都建立一个Connection，在消息量打的时候建立TPC Connection的开销将是巨大的，效率也低。Channel是在Connection内部建立的逻辑连接，如果应用程序支持多线程，通常每个thread创建单独的Channel进行通讯，AMQP method包含了Channel id帮助客户端和Message Broker识别Channel，所以Channel之间是完全隔离的。Channel作为轻量级的Connection极大减少了操作系统建立TPC Connection的开销

**Exchange**：Message到达Broker的第一站，根据分发规则，匹配查询表中的routing key，分发消息到Queue中去。常用的类型有：direct（point-to-point）、topic（Publish-subscribe）、fanout（multicast）

**Queue**：消息最终被送到这里等待Consumer取走

**Binding**：Exchange和Queue之间的虚拟链接，binding中可以包含routing key，binding信息被保存到Exchange中的查询表中，用于Message的分发依据

---

## Rabbit MQ核心部分

> 封装Channel部分代码

直接将信道封装，往下每次调用不需要再重新连接信道，直接调用方法即可

```java
/**
 * 直接获取信道
 * @return 返回MQ设置好的的信道
 */
public static Channel getChannel() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(RabbitMQConfigDiction.MQ_HOST);
    factory.setVirtualHost(RabbitMQConfigDiction.VIRTUAL_HOST);
    factory.setUsername(RabbitMQConfigDiction.USER_NAME);
    factory.setPassword(RabbitMQConfigDiction.PASSWORD);
    return factory.newConnection().createChannel();
}
```

> 所需依赖

```xml
<dependencies>
    <!-- rabbitmq客户端依赖 -->
    <!-- https://mvnrepository.com/artifact/com.rabbitmq/amqp-client -->
    <dependency>
        <groupId>com.rabbitmq</groupId>
        <artifactId>amqp-client</artifactId>
        <version>5.20.0</version>
    </dependency>
    <!-- 操作文件流 -->
    <!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.11.0</version>
    </dependency>
</dependencies>
```

### Hello World

<img src="file:///Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq6-hello.png" title="" alt="" width="276">

> 代码实现

简单的生产者（hello/Producer）

```java
/**
 * HelloWorld-简单模式，发送方（生产者）
 */
private void testHelloWorldSent() {
    // 生产者创建连接-获取信道（Channel）
    RabbitMQTestUtils rabbitMQTestUtils = new RabbitMQTestUtils();
    try (Connection connection = rabbitMQTestUtils.getConnectionFactory().newConnection();
         Channel channel = connection.createChannel()){
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里消息是否需要持久化（写入磁盘），默认存在内存中，即false
         * 3.该队列是否只供一个消费者进行消费共享，true为可以多个消费者消费，false只能一个消费者消费
         * 4.是否自动删除，最后一个消费者断开连接后该队列是否执行自动删除-true自动删除，false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello Rabbit MQ";
        /**
         * 进行消息发送
         * 1.目标交换机
         * 2.队列名称（routingKey）
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("[X] Sent '" + message + "'");
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
    } catch (TimeoutException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
    }
}
```

简单的消费者（hello/Consumer）

```java
/**
 * HelloWorld-简单模式，接收方（消费者）
 * @throws IOException
 * @throws TimeoutException
 */
private void testHelloWorldReceived() throws IOException, TimeoutException {
    RabbitMQTestUtils rabbitMQTestUtils = new RabbitMQTestUtils();
    Connection connection = rabbitMQTestUtils.getConnectionFactory().newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println("[*] Waiting for messages. To exit press CTRL+C");
    // 额外的 DeliverCallback 接口，用于缓冲服务器推送给我们的信息。
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("[X] Received '" + message + "'");
    };
    /**
     * 消费者消费信息
     * 1.消费队列名称
     * 2.消费成功后是否自动应答，true标识自动，false手动
     * 3.消费者成功消费的回调
     * 4.消费者取消消息的回调（消费被中断）
     */
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        System.out.println("Received is stop!");
    });
}
```

---

### Work Queues

工作队列（任务队列），主要思想是避免立即执行资源密集型任务，而不得不等待它完成。工作队列（又名：任务队列）背后的主要理念是避免立即执行资源密集型任务并等待其完成。相反，我们将任务安排在稍后完成。我们将任务封装为消息，并将其发送到队列。在后台运行的 Worker 进程会弹出任务并最终执行作业。当运行多个 Worker 时，任务将在它们之间共享。  

这一概念在网络应用程序中尤其有用，因为在短时间的 HTTP 请求窗口中不可能处理复杂的任务。安排任务在之后执行。

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq7-workQueues.png)

#### 消费者声明队列

- 消费者与生产者**都应当使用`channel.queueDeclare` 方法声明队列**

- 消费者如果不声明队列，那么如果首次创建对象，生产者未声明队列前消费者就启动，会触发NOT_FOUND异常

- 有一个疑问点：优先创建了消费者，并且声明了QueueDeclare，为什么此时的消费者无法消费后起的生产者生产的消息？

#### 轮训模式

当一个生产者生产消息时，多个消费者同时消费，此时消费者会轮流消费消息。例如生产者生产了`AA/BB/CC/DD` 四条消息，有两个消费者 `Worker01` 和 `Worker02` 此时01就会消费`AA/CC` ，02就会消费`BB/DD` 如此轮流依次消费

> 代码实现

生产者Task（workqueues/Task01）

```java
/**
 * 简单的工作队列发送方
 */
private void testWorkQueueSent() {
    try (Channel channel = RabbitMQTestUtils.getChannel()) {
        // 声明队列
        channel.queueDeclare(RabbitMQConfigDiction.TASK_QUEUE, false, false, false, null);
        // 从控制台接收信息
        System.out.println("please input the message");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", RabbitMQConfigDiction.TASK_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("be sent successfully! the message is : [" + message + "]");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (TimeoutException e) {
        e.printStackTrace();
    }
}
```

用于轮训的消费者Worker（启动时启动多个即可，代码使用同一套）（workqueues/Worker01）

```java
/**
 * 简单的工作队列模式接收方
 * @throws IOException
 * @throws TimeoutException
 */
private void testWorkQueuesReceived() throws IOException, TimeoutException {
    Channel channel = RabbitMQTestUtils.getChannel();
    // 声明队列-消费者如果不声明队列，那么如果首次创建对象，生产者未声明队列前消费者就启动，会触发NOT_FOUND异常
    channel.queueDeclare(RabbitMQConfigDiction.TASK_QUEUE, false, false, false, null);
    // 额外的 DeliverCallback 接口，接收消息成功时执行。
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("[X] Received '" + message + "'");
    };
    // 消息接收取消后的接口
    CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
    System.out.println("Work01 is waiting");
    // 消息接收
    channel.basicConsume(RabbitMQConfigDiction.TASK_QUEUE, true, deliverCallback, cancelCallback);
}
```

#### 消息应答

消费者完成一个任务可能需要一段时间，如果其中一个消费者处理一个长的任务并仅完成了部分突然就挂掉，那么该消息就无法被接收。**Rabbit MQ一旦向消费者传递了一条消息，便立即将该消息标记为删除。** 在这种情况下突然消费者挂掉，丢失了正在处理的消息以及后续轮训发送给该消费者的消息，因为消费者无法接收。

为了保证消息在发送过程中不丢失，Rabbit MQ引入了消息应答机制。即，**消费者在接收到消息并处理该消息后，返回告知Rabbit MQ已经处理成功，Rabbit MQ此时可以删除消息。** 

##### 自动应答

消息发送后立即被认为已经传送成功，这种模式需要在**高吞吐量和数据传输安全性方面做权衡**，因为这种模式如果消息在接收到之前，消费者就出现连接或者Channel关闭，那么消息就丢失了，当然另一方面这种模式消费者也可以传递过载的消息，**没有对传递的消息数量进行限制**，当然这样有可能使得消费者由于接收太多还来不及处理的消息导致消息积压 ，最终使得内存耗尽，这些消费者线程被系统杀死。

因此该模式**仅适用于消费者可以高效并以某种速率能够处理这些消息情况下使用**。

##### 手动应答

###### 手动应答的方法

> `Channel.basicAck()` 

确定消息，适用于消息肯定被处理后的放回，告知Rabbit MQ消息已经被接收并且处理，可以丢弃该消息

> `Channel.basicNack()` 

不确定消息，表示不处理该消息直接拒绝，提示Rabbit MQ可以将消息丢弃。

> `Channel.basicReject()` 

不确定消息，表示不处理该消息直接拒绝，提示Rabbit MQ可以将消息丢弃。

与上述的 `Channel.basicAck()/Channel.basicNack()` 相比少了一个参数 `Multiple` （批量处理）。

###### Multiple批量处理

手动应答的好处就是可以批量应答并且减少网络拥堵（如下方例子，第二个参数“true”即为multiple参数）

```java
channel.basicAck(deliceryTag, true);
```

> multiple参数的`true` 和`false` 

- true代表批量应答Channel上未应答的消息
  
  - 比如Channel上有传送tag的消息5、6、7、8，当前tag是8，那么此时5-8这些还未应答的消息都会被确收到消息应答

- false只会应答最后一条消息（日常开发建议使用）
  
  - 只会应答tag为8的消息（之应答当前），5、6、7这三个消息依然不会被确认收到消息应答

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq8-ack.png)

###### 消息自动重新入队

如果消费者由于某些原因失去连接（其通道已关闭，连接已关闭或TCP连接丢失），导致消息未发送ACK确认，Rabbit MQ将了解到消息未处理，并对其重新排队。如果此时其他消费者可以处理，它将很快将其重新分发给另一个消费者。这样即使某个消费者偶尔死亡，也可以确保消息不会丢失。

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq9-ack.png)

由上图可见消息手动应答有两个特性：

- 消息在手动应答时是不丢失的

- 消息在未得到应答时，应当重新放回队列被重新消费

> 代码实现

生产者部分（workqueues/Task01）

```java
/**
 * 消息手动应答发送方
 * 本次主要测试消费者，因此生产者代码与上一致，只是使用了不同的消息队列名称
 */
private void testWorkQueueAckSent() {
    try (Channel channel = RabbitMQTestUtils.getChannel()) {
        // 声明队列
        channel.queueDeclare(RabbitMQConfigDiction.TASK_ACK_QUEUE, false, false, false, null);
        // 从控制台接收信息
        System.out.println("please input the message");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", RabbitMQConfigDiction.TASK_ACK_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("be sent successfully! the message is : [" + message + "]");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (TimeoutException e) {
        e.printStackTrace();
    }
}
```

消费者部分（workqueues/Worker01）

```java
/**
 * 手动应答时不丢失，放回队列重新消费
 * @throws IOException
 * @throws TimeoutException
 */
private void testWorkQueuesAckReceived01() throws IOException, TimeoutException {
    final Channel channel = RabbitMQTestUtils.getChannel();
    channel.queueDeclare(RabbitMQConfigDiction.TASK_ACK_QUEUE, false, false, false, null);
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        // 模拟事务处理，本例为较快，因此沉睡1秒
        RabbitMQTestUtils.getSleep(1);
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("[X] Received '" + message + "'");
        /**
         * 手动应答
         * 1.消息的标记，long类型参数（tag）
         * 2.是否批量应答（multiple）
         */
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };
    // 消息接收取消后的接口
    CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
    System.out.println("Work01 is waiting...fast");
    // 采用手动应答
    boolean autoAck = false;
    channel.basicConsume(RabbitMQConfigDiction.TASK_ACK_QUEUE, autoAck, deliverCallback, cancelCallback);
}
```

消费者部分（workqueues/Worker02）

*上述代码中 `RabbitMQTestUtils.getSleep(30);` 模拟工作时间改为30秒*

- 经过测试，两台接受者服务器均正常的情况下，消息遵循轮训原则被消费，只是Worker02处理较慢。

- 但是在Worker02漫长的等待过程中，消息未被消费到就切断服务器程序，此时Worker01则会消费丢失的信息。

- 如果再次启动Worker02服务器，会再次遵循轮训原则，并且Worker02不会去重复消费之前的丢失消息。

#### 消息的持久化

##### 队列持久化

我们已经学习了消息应答来保证即使消费者死亡，消息也不会丢失。但是，如果 RabbitMQ 服务器停止，我们的任务仍然会丢失。  

当 RabbitMQ 退出或崩溃时，它会忘记队列和消息，除非您告诉它不要这样做。要确保消息不会丢失，需要做两件事：我们需要将队列和消息都标记为持久化。

> 队列持久化实现

之前我们创建的队列都是非持久化的，Rabbit MQ一旦重启就会被删除。如果需要进行队列持久化，我们需要把参数 `durable` 设为true。当做了该项设置后，及时Rabbit MQ重启，队列也将存在，而不会被删除。

**官网说明** *This `queueDeclare` change needs to be applied to both the producer and consumer code.——对 queueDeclare 的更改需要同时应用于生产者和消费者代码。*

经过测试，下述代码段如果消费者没有，则不会消费消息。

```java
boolean durable = true;
channel.queueDeclare("queueName", durable, false, false, null);
```

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq10-Durable.png)

*Tips：当一个队列名称在对应的Virtual host已经存在，并且为非持久化时，创建同名的持久化队列会报创建错误。*

##### 消息持久化

如果仅仅按照上述进行队列持久化，那么，重启后队列会存在，但是未被消费的消息将会丢失，因此需要进行消息持久化。

> 消息持久化实现

队列的持久化应该是有生产者生产出消息时，就应当注明消息是否持久化，即 `channel.basicPublish()` 中的第三个参数，规定为 `PERSISTENT_TEXT_PLAIN` 持久性文本。

该方法不能完全保证不会丢失消息；会尽量要求Rabbit MQ将消息保存到磁盘，因为在Rabbit MQ将消息准备存储到设备时，未存储完毕这个时间节点未写入，则会导致持久化失败。这种情况则需要后续学习到的更加强有力的持久化策略。

```java
import com.rabbitmq.client.MessageProperties;

channel.basicPublish("", "queueName",
            MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.getBytes());
```

> 代码实例

生产者部分（workqueues/Task01）

```java
/**
 * 队列持久化
 * 消息持久化
 */
private void testWorkQueueDurable() {
    try (Channel channel = RabbitMQTestUtils.getChannel();) {
        // 持久化队列Queue的参数-durable
        boolean durable = true;
        channel.queueDeclare(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, durable, false, false, null);
        // 从控制台接收信息
        System.out.println("please input the message");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", RabbitMQConfigDiction.TASK_DURABLE_QUEUE,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, // 消息持久化
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println("be sent successfully! the message is : [" + message + "]");
        }
    } catch (IOException | TimeoutException e) {
        e.printStackTrace();
    }
}
```

消费者部分（workqueues/Worker01）

```java
/**
 * 持久化测试接收方
 * @throws IOException
 * @throws TimeoutException
 */
private void testWorkQueuesDurableReceived() throws IOException, TimeoutException {
    Channel channel = RabbitMQTestUtils.getChannel();
    // 同样声明持久化队列
    channel.queueDeclare(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, true, false, false, null);
    // 额外的 DeliverCallback 接口，接收消息成功时执行。
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("[X] Received '" + message + "'");
    };
    // 消息接收取消后的接口
    CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
    System.out.println("Work01 is waiting");
    // 消息接收
    channel.basicConsume(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, true, deliverCallback, cancelCallback);
}
```

#### 公平调度

其实是**非轮训分发**，能者多劳。

调度工作仍然不能完全按照我们的要求进行。例如，在有两个 Worker 的情况下，当所有奇数消息都很重，而偶数消息都很轻时，一个 Worker 将一直处于忙碌状态，而另一个 Worker 几乎不做任何工作。但是，RabbitMQ 对此一无所知，它仍然会均匀地分派消息。  

出现这种情况是因为 RabbitMQ 只会在消息进入队列时分派消息。它不会查看消费者未确认消息的数量。它只是盲目地将每 n 条消息分派给第 n 个消费者。

为了解决这个问题，我们可以在消费者使用带有 prefetchCount = 1 设置的 basicQos 方法。这样，RabbitMQ 就不会一次向 Worker 发送多于一条消息。或者换句话说，在处理并确认前一条消息之前，不要向 Worker 发送新消息。相反，它会将消息分派给下一个不忙的 Worker。

> 消费者模块代码

```java
int prefetchCount = 1; // 轮训分发默认为0
channel.basicQos(prefetchCount);
```

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq11-FairDispatch.png)

> 代码实例

消费者部分（workqueues/Worker01）

```java
/**
 * 公平调度
 * @throws IOException
 * @throws TimeoutException
 */
private void testWorkQueuesFairDispatchReceived01() throws IOException, TimeoutException {
    final Channel channel = RabbitMQTestUtils.getChannel();
    channel.queueDeclare(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, true, false, false, null);
    // 公平调度
    channel.basicQos(1);
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        // 模拟事务处理，本例为较慢，因此沉睡1秒
        RabbitMQTestUtils.getSleep(1);
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        System.out.println("[X] Received '" + message + "'");
        /**
         * 手动应答
         * 1.消息的标记，long类型参数（tag）
         * 2.是否批量应答（multiple）
         */
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    };
    // 消息接收取消后的接口
    CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "Received is cancel!");
    System.out.println("Work01 is waiting...fast");
    // 采用手动应答
    boolean autoAck = false;
    channel.basicConsume(RabbitMQConfigDiction.TASK_DURABLE_QUEUE, autoAck, deliverCallback, cancelCallback);
}
```

消费者部分（workqueues/Worker02）

*上述代码中 `RabbitMQTestUtils.getSleep(2);` 模拟工作时间改为2秒*

---