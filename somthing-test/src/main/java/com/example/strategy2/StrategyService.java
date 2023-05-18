package com.example.strategy2;

/**
 * 使用 @Service 实现接口，由此实现策略模式
 */
public interface StrategyService {
    String serviceFlag();
    void doSome();
}
