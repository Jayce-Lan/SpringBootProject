package org.example.chapter03.templatemethod;

/**
 * 抽象类中的抽象方法，必须要实现，否则无法创建对象
 * 即，即使new一个AbstractDisplay，也需要实现它的抽象方法
 */
public abstract class AbstractDisplay {
    // protected 只能被子类和同一包调用
    protected abstract void open();
    protected abstract void print();
    protected abstract void close();

    public final void display() {
        open();
        for (int i = 0; i < 5; i++) {
            print();
        }
        close();
    }
}
