# Spring Boot 2

## Spring Boot 基础

### 配置文件的优先级

> properties 的优先级大于 yml

*application.yml* 中有如下属性：

```yml
server:
  port: 8998
  servlet:
    session:
      timeout: 30
  tomcat:
    uri-encoding: UTF-8

# 由于 properties 的优先级大于 yml 因此此处的age不会生效
age: 10
```

*application.properties* 中有如下属性：

```properties
server.port=8999

age=11
```

项目启动时，端口会为8999；而使用@Value属性去获取“age”的时候，也会只能获取到 `age = 11` 这一属性





## Spring MVC