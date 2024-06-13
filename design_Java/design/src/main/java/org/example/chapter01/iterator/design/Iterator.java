package org.example.chapter01.iterator.design;

/**
 * 用于遍历集合中的元素
 * 起作用相当于循环语句中的循环变量
 */
public interface Iterator {
    /**
     * 判断是否存在下一个元素
     * @return 存在返回ture；不存在返回false
     */
    abstract boolean hasNext();

    /**
     * 返回集合中的一个元素
     * 为了下次调用next时正确返回下一个元素
     * 该方法还隐含着将迭代器移动至下一个元素处理
     * @return 返回集合中的一个元素
     */
    abstract Object next();
}
