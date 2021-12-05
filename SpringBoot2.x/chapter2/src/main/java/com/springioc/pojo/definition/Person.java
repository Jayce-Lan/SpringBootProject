package com.springioc.pojo.definition;

/**
 * 依赖注入
 */
public interface Person {
    /**
     * 使用动物服务
     */
    public void service();

    /**
     * 设置动物
     * @param animal 被设置的动物
     */
    public void setAnimal(Animal animal);
}
