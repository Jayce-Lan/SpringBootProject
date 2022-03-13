package com.example.config;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 让这个类成为切面类
 */
@Aspect
/**
 * 将类注入到IoC容器中
 */
@Component
@Slf4j
public class AopLog {
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.example..*.*(..))")
    public void aopWebLog() {

    }

    @Before("aopWebLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("url: " + request.getRequestURL().toString());
        log.info("HTTP方法: " + request.getMethod());
        log.info("IP地址: " + request.getRemoteAddr());
        log.info("类的方法: " + joinPoint.getSignature().getDeclaringTypeName() + " . "
                + joinPoint.getSignature().getName());
        log.info("参数: " + request.getQueryString());
    }

    @AfterReturning(pointcut = "aopWebLog()", returning = "returnObject")
    public void doAfterReturning(Object returnObject) throws Throwable {
        // 请求完毕，返回内容
        log.info("应答值：" + returnObject);
        log.info("用时：" + (System.currentTimeMillis() - startTime.get()));
    }

    @AfterThrowing(pointcut = "aopWebLog()", throwing = "exception")
    public void addAfterThrowingLogger(JoinPoint joinPoint, Exception exception) {
        log.error("执行异常：" + exception);
    }
}
