package org.example.chapter04.factorymethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.chapter04.factorymethod.framework.Factory;
import org.example.chapter04.factorymethod.framework.Product;
import org.example.chapter04.factorymethod.idcard.IDCardFactory;

public class FactoryMethodMain {
    private static final Logger log = LogManager.getLogger(FactoryMethodMain.class);

    public static void main(String[] args) {
        Factory factory = new IDCardFactory();
        Product card1 = factory.create("小红");
        Product card2 = factory.create("小明");
        Product card3 = factory.create("小刚");
        card1.use();
        card2.use();
        card3.use();
    }
}
