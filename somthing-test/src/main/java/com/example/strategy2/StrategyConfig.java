package com.example.strategy2;

import com.example.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用于调用Service的类
 */
@Configuration
@Slf4j
public class StrategyConfig {
    private static Map<String, StrategyService> STRATEGY_SERVICE_MAP;

    /**
     * 使用Bean获取一次被Spring容器注册过的 StrategyService 对应的接口
     * @param strategyServices
     * @return
     */
    @Bean
    public Map<String, StrategyService> StrategyConfigGetMap(List<StrategyService> strategyServices) {
        Map<String, StrategyService> strategyMap = strategyServices.stream()
                .collect(Collectors.toMap(StrategyService :: serviceFlag, s -> s));
        STRATEGY_SERVICE_MAP = strategyMap;
        log.info("Map: {}", strategyMap.size());
        return strategyMap;
    }

    public StrategyService getStrategyService(String flag) {
        StrategyService strategyService = STRATEGY_SERVICE_MAP.get(flag);
        if (ValidateUtil.isEmpty(strategyService)) {
            throw new RuntimeException("调用失败！无法找到对应的交易！");
        }
        return strategyService;
    }
}
