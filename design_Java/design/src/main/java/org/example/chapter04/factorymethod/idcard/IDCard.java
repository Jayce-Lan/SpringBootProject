package org.example.chapter04.factorymethod.idcard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter04.factorymethod.framework.Product;

public class IDCard extends Product {
    private static final Logger log = LogManager.getLogger(IDCard.class);
    private String owner;

    public IDCard(String owner) {
        log.info("制作{}的ID卡", owner);
        this.owner = owner;
    }

    @Override
    public void use() {
        log.info("使用{}的ID卡", this.owner);
    }

    public String getOwner() {
        return this.owner;
    }
}
