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

### 通过配置文件解除RedisTemplate的默认序列化

#### 配置RedisTemplate

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // key和hashKey采用String序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        // Value和hashValue使用JSON序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }
}
```

#### 引入json依赖

> 由于上一步配置文件中使用了json序列化，因此需要引入json依赖，否则会报错

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

#### 在测试类中引入

```java
@Resource
private RedisTemplate<String, Object> redisTemplate;
```

#### 存储对象

```java
/**
 * 使用序列化存储对象
 * 在redis中，对象被存储成了json格式
 */
@Test
void saveObject() {
    redisTemplate.opsForValue().set("user:100", new User("卢本伟", new BigDecimal(19)));
    User user1 = (User) redisTemplate.opsForValue().get("user1");
    log.info("user1: {}", user1);
}
```

> 存储的对象格式如下，此方法存储有缺点，就是会存储很多无用信息

```json
{
  "@class": "com.example.pojo.User",
  "name": "卢本伟",
  "age": [
    "java.math.BigDecimal",
    19
  ]
}
```

### StringRedisTemplate

> Spring提供了一个 StringRedisTemplate 类，它的key和value默认使用String字符串的形式，省去了自定义RedisTemplate的过程

#### 使用方法

```java
@Resource
private StringRedisTemplate stringRedisTemplate;
// Spring的序列化工具，用于对象与json的互转
private static final ObjectMapper mapper = new ObjectMapper();


/**
 * 使用字符串序列化写入对象
 */
@Test
void setObjectTest() throws JsonProcessingException {
    User user = new User("张伟", new BigDecimal(30));
    // 序列化对象
    String userString = mapper.writeValueAsString(user);
    stringRedisTemplate.opsForValue().set("user:200", userString);
    String userJson = stringRedisTemplate.opsForValue().get("user:200");
    // 取出之后需要反序列化
    User user1 = mapper.readValue(userJson, User.class);
    log.info("user:200 - {}", user1);
}
```

#### Redis中的对象

```json
{
  "name": "张伟",
  "age": 30
}
```



## Redisson快速入门

### 使用Redisson实现分布式锁

#### 引入依赖

> 值得注意的是，Redisson也有starter依赖，但是会出现与Redis本身的配置会有冲突的可能，因此建议使用redisson，并使用配置类来注入依赖

```xml
<!-- Redis官方工具 redisson  -->
<!-- 在这里不建议使用starter的配置，因为会和Redis配置混淆 -->
<!-- https://mvnrepository.com/artifact/org.redisson/redisson -->
<dependency>
	<groupId>org.redisson</groupId>
	<artifactId>redisson</artifactId>
	<version>3.16.1</version>
</dependency>
```



#### 配置 Config 文件

> 在配置文件的过程中，其实注入的是 `RedissonClient`  这个类。值得注意的是， `Config` 类要引入redisson中的类

```java
package com.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于配置Redisson客户端的配置类
 */

@Configuration
public class RedisConfig {
    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址，也可以使用 config.useClusterServers() 添加集群地址
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(1);
        // 创建客户端
        return Redisson.create(config);
    }
}

```



#### 实际使用

> 实际业务使用可以看 redis-app中 `VoucherOrderServiceImpl`  中的 `seckillVoucherByRedisson` 方法

```java
package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class RedissonTests {
    @Resource
    private RedissonClient redissonClient;

    /**
     * 基于 redisson 实现分布式锁
     * @throws InterruptedException
     */
    @Test
    void testRedisson() throws InterruptedException {
        // 获取锁（可重入），指定锁的名称
        RLock rLock = redissonClient.getLock("testLock");
        // 尝试获取锁，参数分别为：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
        boolean lockFlag = rLock.tryLock(1, 10, TimeUnit.SECONDS);
        if (lockFlag) {
            try {
                log.info("执行业务");
            } finally {
                // 释放锁
                rLock.unlock();
            }
        }
    }
}

```