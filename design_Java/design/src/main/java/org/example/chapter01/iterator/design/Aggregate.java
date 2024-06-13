package org.example.chapter01.iterator.design;

/**
 * 所有要遍历集合的接口
 * 实现了该接口的类可以保存多个元素集合，类似数组
 */
public interface Aggregate {
    /**
     * 需要遍历集合元素时，可以调用该方法来生成一个实现了 Iterator 接口的类的实例
     * @return 生成一个用于遍历集合的迭代器
     */
    abstract Iterator iterator();
}
