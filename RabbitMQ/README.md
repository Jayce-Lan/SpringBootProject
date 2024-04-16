# Rabbit MQ学习

Rabbit MQ是一个消息中间件，它接受并转发消息。可以把它当做一个快递站点，当需要发送一个包裹时，把包裹放在快递站，快递员最终会把包裹送到收件人处。按照这个逻辑，Rabbit MQ是一个快递站，一个快递员帮助传递快件。Rabbit MQ与快递站的区别是，它不处理快件而是接收、存储和转发消息数据。

## MQ基本概念

### 为什么要使用MQ

> 流量削峰

如果订单系统最多能处理一万次订单，这个处理能力正常时绰绰有余。但是高峰时两万次下单就无法处理，只能限制订单超过一万后不允许用户下单，使用消息队列做缓冲（排队），我们可以取消这个限制，把一秒内下单分散成一段时间处理，这时候有些用户可能需要下单后十几秒才有成功反馈，但总比不能下单的体验要好。

![](/Users/lanjiesi/Documents/MyProject/Java/SpringBootProject/RabbitMQ/img/mq1-llxf.png)

> 

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