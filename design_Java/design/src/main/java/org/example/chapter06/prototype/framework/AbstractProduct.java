package org.example.chapter06.prototype.framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractProduct implements Product {
    private final Logger log = LogManager.getLogger(this.getClass().getName());

    /**
     * 通过抽象类来实现接口方法
     * 原本实现Product接口的实现类只需要继承AbstractProduct
     * 因为createClone为复用方法，因此在这里统一实现
     * 子类只需要专注于实现use方法即可
     * @return 返回clone对象
     */
    @Override
    public Product createClone() {
        Product product = null;
        try {
            product = (Product) clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
        }
        return product;
    }
}
