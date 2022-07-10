# Spring Boot2.x

## Spring Boot整合MyBatis

### 引入依赖

- spring-boot-starter-jdbc

- mybatis-spring-boot-starter

- mysql-connector-java

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.1</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

### 配置文件

*application.properties*

```properties
server.port=8999

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.url=jdbc:mysql://localhost:3306/chapter3?serverTimezone=UTC&userUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false

# 整合mybatis
# 配置可以简写的包
mybatis.type-aliases-package=com.example.pojo
# 配置Mapper对应的xml的文件夹
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
# 控制台打印SQL
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

### 配置接口

在DAO层加入以下注解：

- @Mapper 

- @Repository

随后即可编写对应接口的xml文件

## Spring Boot整合MyBatis配置多数据源

#### 引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.2.1</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### 配置文件

```yml
server:
  port: 8999

spring:
  datasource:
    drds:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: root
      url: jdbc:mysql://localhost:3306/chapter3?serverTimezone=UTC&userUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false
      mapper-locations: classpath:mybatis/mapper/*DRDSMapper.xml
    adb:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: root
      url: jdbc:mysql://localhost:3306/chapter4?serverTimezone=UTC&userUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false
      mapper-locations: classpath:mybatis/mapper/*ADBMapper.xml
```

#### 在启动类中屏蔽默认数据源

> 由于是多数据源，因此需要屏蔽掉启动类中的默认数据源

***@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})***

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Chapter5MybatisMoreDataSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter5MybatisMoreDataSourceApplication.class, args);
    }

}
```

#### 配置数据源工具类

> 工具类中会确定Dao的位置以及映射Mapper的位置

##### 首要数据源

> 首要数据源需要加  *@Primary* ，Primary可以理解为默认优先选择,同时不可以同时设置多个

```java
package com.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.dao.drds", sqlSessionTemplateRef = "drdsSessionTemplate")
public class DrdsDataSourceConfig {
    @Value("${spring.datasource.drds.url}")
    private String url;
    @Value("${spring.datasource.drds.username}")
    private String username;
    @Value("${spring.datasource.drds.password}")
    private String password;
    @Value("${spring.datasource.drds.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.drds.mapper-locations}")
    private String mapperLocations;

    /**
     * 数据源
     * @return 数据源
     */
    @Bean(name = "drdsDataSource")
    @Primary
    public DataSource getDrdsDataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
        return dataSource;
    }

    @Bean(name = "drdsSqlSessionFactory")
    @Primary
    public SqlSessionFactory drdsSqlSessionFactory(@Qualifier("drdsDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        return bean.getObject();
    }

    @Bean(name = "drdsTransactionManager")
    @Primary
    public DataSourceTransactionManager drdsTransactionManager(@Qualifier("drdsDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "drdsSessionTemplate")
    @Primary
    public SqlSessionTemplate drdsSessionTemplate(@Qualifier("drdsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

##### 次要数据源

```java
package com.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.dao.adb", sqlSessionTemplateRef = "adbSqlSessionTemplate")
public class AdbDataSourceConfig {
    @Value("${spring.datasource.adb.url}")
    private String url;
    @Value("${spring.datasource.adb.username}")
    private String username;
    @Value("${spring.datasource.adb.password}")
    private String password;
    @Value("${spring.datasource.adb.driver-class-name}")
    private String driverClassName;
    private final static String MAPPER_LOCATION = "classpath:mybatis/mapper/*ADBMapper.xml";

    /**
     * 创建数据源
     * @return 数据源
     */
    @Bean(name = "adbDataSource")
    public DataSource getADBDataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
        return dataSource;
    }

    /**
     * 创建 SqlSessionFactory
     * @param dataSource 数据源
     * @return SqlSessionFactory
     * @throws Exception
     */
    @Bean(name = "adbSqlSessionFactory")
    public SqlSessionFactory adbSqlSessionFactory(@Qualifier("adbDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置mapper配置文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }

    /**
     * 事务管理器
     * @param dataSource
     * @return
     */
    @Bean(name = "adbTransactionManager")
    public DataSourceTransactionManager adbTransactionManager(@Qualifier("adbDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "adbSqlSessionTemplate")
    public SqlSessionTemplate adbSqlSessionTemplate(@Qualifier("adbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```

随后根据需求调用Dao层即可，详情参考[ *chapter5-mybatis-moreDataSource*](https://github.com/Jayce-Lan/SpringBootProject/tree/master/SpringBoot2.x/chapter5-mybatis-moreDataSource)

## Spring Boot 缓存

### Spring Cache

> 使用注解整合缓存

首先，需要配置控制台执行SQL时打印SQL语句

> 配置后，如果查询时调用的非缓存，控制台会打印执行的SQL语句；如果调用了缓存，那么则会不执行SQL

```properties
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

其次，Spring Boot启动类中配置开启缓存注解 `@EnableCaching`

```java
package com.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
// 开启缓存
@EnableCaching
public class Chapter6CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter6CacheApplication.class, args);
    }

}
```

其次，在Controller层，加入注解

> 在执行第二次同id查询时，会发现控制台不再打印SQL，而是从缓存调取

```java
@RequestMapping("/queryUserById")
@Cacheable(value = "user", key = "#userADTO")
public UserADTO queryUserById(UserADTO userADTO) {
    return userService.queryUserById(userADTO);
}
```

#### 注解详解

- `@Cacheable` 用于标记缓存，也就是对使用@Cacheable注解的位置进行缓存

- `@CachePut` 只是用于将标记该注解的方法的返回值放入缓存中，无论缓存中是否包含当前缓存，只是以键值的形式将执行结果放入缓存中。在使用方面，@CachePut注解和@Cacheable注解一致

- `@CacheEvict` Spring Cache提供了@CacheEvict注解用于清除缓存数据，与@Cacheable类似，不过@CacheEvict用于方法时清除当前方法的缓存，用于类时清除当前类所有方法的缓存;@CacheEvict除了提供与@Cacheable一致的3个属性外，还提供了一个常用的属性allEntries，这个属性的默认值为false，如果指定属性值为true，就会清除当前value值的所有缓存

### Spring Boot整合Redis

#### 引入依赖

> Spring Boot中的主要依赖为：spring-boot-starter-data-redis

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 配置文件

```properties
spring.redis.host=localhost
spring.redis.port=6379
```

#### `RedisTemplate` 的使用

> 在Redis中，使用的是`RedisTemplate` 类对Redis进行读写删改的操作

- 给Redis读写数据（在测试用例当中）

```java
@Resource
private RedisTemplate redisTemplate;


/**
 * Redis写入数据
 */
@Test
public void redisString() {
    redisTemplate.opsForValue().set("num", 123);
    redisTemplate.opsForValue().set("string", "some string");
    Object num = redisTemplate.opsForValue().get("num");
    Object string = redisTemplate.opsForValue().get("string");
    System.err.println(num);
    System.err.println(string);
}
```

- 使用构造方法set进行缓存定时删除

```java
/**
 * 设置三秒后失效
 */
@Test
public void invaliIn3s() {
	redisTemplate.opsForValue().set("num", "123XYZ", 3, TimeUnit.SECONDS);
	try {
		Object num1 = redisTemplate.opsForVlue().get("num");
		log.info(num1.toString()); // 123XYZ
		Thread.sleep(2000);
		Object num2 = redisTemplate.opsForValue().get("num");
		log.info(num2.toString()); // 123XYZ
		Thread.sleep(2000);
		Object num3 = redisTemplate.opsForValue().get("num");
		log.info(num3 == null ? "null" : num3.toString()); // null
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}
```