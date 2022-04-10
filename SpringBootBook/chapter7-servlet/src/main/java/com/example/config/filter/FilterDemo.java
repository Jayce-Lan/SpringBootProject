package com.example.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 拦截器
 */
// 如果有多个拦截器，那么序号越小越被优先执行
@Order(1)
// URL 过滤配置
@WebFilter(filterName = "FilterDemo", urlPatterns = "/*")
@Slf4j
public class FilterDemo implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("=======FilterDemo 拦截器=======");
//        servletResponse.getWriter().print("拦截处理后");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
