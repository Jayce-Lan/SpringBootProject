package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 此处的示例配置了Session的缓存时间。
 * maxInactiveIntervalInSeconds用于设置Session的失效时间，
 * 使用Redis共享Session之后，原Spring Boot的server.session.timeout属性不再有效
 *
 * 经过此处的配置后，Session调用就会自动去Redis上存取。
 * 另外，想要达到Session共享的目的，只需要在其他系统上进行同样的配置即可
 */

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400 * 30)
public class SessionConfig {
}
