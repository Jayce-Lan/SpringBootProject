package com.start.webflux;

import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 使用Bean直接启动，而不需要Controller
 */
@Configuration
public class HelloRouter {
    @Bean
    public RouterFunction<ServerResponse> routeHello(HelloHandle helloHandle) {
        System.err.println("我被执行了");
        return RouterFunctions
                .route(RequestPredicates.GET("/hello")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), helloHandle::hello);
    }
}
