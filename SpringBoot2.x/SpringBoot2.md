# Spring Boot2.x

## Spring Boot整合Mybatis

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

```



### 配置接口

在DAO层加入以下注解：

- @Mapper 

- @Repository

随后即可编写对应接口的xml文件