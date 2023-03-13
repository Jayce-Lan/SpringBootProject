# Rabbit MQ官方文档学习

## 开发前的准备

> 在创建 `Spring Boot` 项目之后，引入rabbitmq的依赖，即引入amqp的协议依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit-test</artifactId>
    <scope>test</scope>
</dependency>
```

> 配置文件当中配置好rabbitmq的信息

```properties
# MQ
spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.port=5672
```

---

## Rabbit MQ简介

- RabbitMQ 是一个消息代理：它接受并转发消息。 你可以把它想象成一个邮局：当你把你想要邮寄的邮件放在邮箱里时， 您可以确定，邮递员最终会将邮件递送给您的收件人。 在这个类比中，RabbitMQ是一个邮政信箱，一个邮局和一个信件载体

- RabbitMQ和邮局的主要区别在于它不处理纸张， 相反，它接受、存储和转发数据的二进制 blob *- 消息*

- RabbitMQ 和一般的消息传递使用的专有名词：
  
  - *生产*只意味着发送。 发送消息的程序是*生产者*
  
  - *队列*是 RabbitMQ 中邮箱的名称。 尽管消息流经 RabbitMQ 和您的应用程序，但它们只能存储在*队列*中。 *队列*仅受主机内存和磁盘限制的约束，它本质上是一个大型消息缓冲区。 许多*生产者*可以发送到一个队列的消息，许多*使用者*可以尝试从一个*队列*接收数据。 
  
  - *消费*与接受的含义相似。 *使用者*是一个主要等待接收消息的程序

- 请注意，生产者、使用者和代理不必驻留在同一个主机上;事实上，在大多数应用程序中，它们不会在同一主机。 应用程序也可以是生产者和使用者。

---

## 基于AMQP的协议实现

> AMQP，即Advanced Message Queuing Protocol，一个提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同的开发语言等条件的限制。Erlang中的实现有RabbitMQ等。




