package com.example.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class TestCity {
    @Resource
    private CityDAO cityDAO;

    @Test
    void testQueryCity() {
        List<City> cities = cityDAO.queryCity();
        cities.forEach(item -> {
            log.info("city: {}", item);
        });
    }
}
