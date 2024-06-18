package org.example.chapter06.prototype;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter06.prototype.framework.Manager;
import org.example.chapter06.prototype.framework.Product;
import org.example.chapter06.prototype.framework.impl.MessageBox;
import org.example.chapter06.prototype.framework.impl.UnderlinePen;

public class PrototypeMain {
    private final Logger log = LogManager.getLogger(this.getClass().getName());

    public static void main(String[] args) {
        // 准备
        Manager manager = new Manager();
        UnderlinePen underlinePen = new UnderlinePen('~');
        MessageBox messageBox = new MessageBox('*');
        MessageBox messageBox2 = new MessageBox('/');
        manager.register("underlinePen", underlinePen);
        manager.register("messageBox", messageBox);
        manager.register("messageBox2", messageBox2);

        // 生成
        Product product1 = manager.create("underlinePen");
        product1.use("Hello, World!");
        Product product2 = manager.create("messageBox");
        product2.use("Hello, World!");
        Product product3 = manager.create("messageBox2");
        product3.use("Hello, World!");
    }
}
