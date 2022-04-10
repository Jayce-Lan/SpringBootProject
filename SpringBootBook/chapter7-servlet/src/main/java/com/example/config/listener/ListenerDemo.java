package com.example.config.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 监听器
 */
@WebListener
@Slf4j
public class ListenerDemo implements ServletContextListener {
    /**
     * 当项目正常启动时，会进入初始化，并且下面的打印会打印Tomcat的版本信息
     * @param sce ServletContextEvent 信息
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);
        log.info("ServletContextListener初始化");
        log.info(sce.getServletContext().getServerInfo());
    }

    /**
     * 当项目端口被占用时，会自动销毁
     * @param sce ServletContextEvent 信息
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
        log.info("ServletContextListener 销毁");
    }
}
