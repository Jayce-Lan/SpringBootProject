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



## Spring Boot 进阶

### 用 *Servlet* 处理请求

- 首先，需要在启动类中配置 `ServletComponentScan` 注解（也可以在注解后配置指定包路径）

- 随后 ，创建一个 *Servlet* 来处理请求
  
  - 需要配置 `@WebServlet` 注解
  
  - 需要继承 `HttpServlet` 类

```java
package com.example.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servletDemo")
@Slf4j
public class ServletDemo extends HttpServlet {
    /**
     * 重写 doGet 方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        log.info("=========== doGet =========");
        resp.getWriter().print("====== ServletDemo ====== ");
    }
}

```