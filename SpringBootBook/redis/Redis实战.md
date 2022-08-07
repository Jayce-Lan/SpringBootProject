# Redis实战

## Jedis

> Jedis是Redis官方的产物，因此使用的命令与Redis原生命令一致

### 使用方法

#### 直连

> 引入依赖

```xml
<!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>3.6.3</version>
</dependency>
```

> 在需要使用到Rdis的地方声明Jedis，并设置连接
> 
> 此处声明在测试类

```java
private Jedis jedis;
@BeforeEach
void setUp() {
    // 设置连接
    jedis = new Jedis("127.0.0.1", 6379);
    // 设置密码
    jedis.auth("");
    // 使用数据库
    jedis.select(0);
}


/**
 * 使用结束后关闭连接
 */
@AfterEach
void tearDown() {
    if (jedis != null) {
        jedis.close();
    }
}
```

> 使用Jedis操作Redis

```java
@Test
void testSet() {
    String num = jedis.set("num", "123123");
    String num1 = jedis.get("num");
    log.info("num: {}", num); // OK
    log.info("num1: {}", num1); // 123123
}
```

#### 连接池连接

Jedis本身是线程不安全的，并且频繁的创建和销毁会损耗性能，因此可以使用Jedis连接池替代直连

> 配置连接池工具类

```java
package com.example.redis1.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis连接池工具类
 */
public class JedisConnectionFactory {
    private static final JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大连接
        jedisPoolConfig.setMaxTotal(8);
        // 最大空闲连接
        jedisPoolConfig.setMaxIdle(8);
        // 最小空闲连接
        jedisPoolConfig.setMinIdle(0);
        // 最大等待时间 ms
        jedisPoolConfig.setMaxWaitMillis(200);
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 1000, "123456");
    }

    // 获取Jedis对象
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
```

> 更改Jedis获取方式

```java
@BeforeEach
void setUp() {
    // 设置连接
    // 使用连接池获取
    jedis = JedisConnectionFactory.getJedis();
    // 设置密码
    jedis.auth("123456");
    // 使用数据库
    jedis.select(0);
}
```

其余使用与上面的直连一致

## SpringDataRedis

项目整合

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-pool2 -->
<!-- 连接池依赖 -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
	<version>2.11.1</version>
</dependency>
```