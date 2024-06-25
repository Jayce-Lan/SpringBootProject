package org.example.chapter08.abstractfactory.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 抽象工厂
 */
public abstract class Factory {
    private static final Logger log = LogManager.getLogger();

    public static Factory getFactory(String clazz) {
        Factory factory = null;
        try {
            factory = (Factory) Class.forName(clazz).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.error("Factory.getFactory is error!");
            log.error(e);
        }
        return factory;
    }

    public abstract Link createLink(String caption, String url);
    public abstract Tray createTray(String caption);
    public abstract Page createPage(String title, String author);
}
