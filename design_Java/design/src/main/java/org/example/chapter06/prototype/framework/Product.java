package org.example.chapter06.prototype.framework;

public interface Product extends Cloneable {
    void use(String str);
    Product createClone();
}
