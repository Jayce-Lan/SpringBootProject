package com.example.localDateTime;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 测试 LocalDateTime
 */
@SpringBootTest
@Slf4j
public class LocalDateTimeTests {

    /**
     * 时间的创建方法
     */
    @Test
    void testLocalDateTimeCreate() {
        // 使用 of 方法，实现 年月日时分秒(纳秒)的创建
        LocalDateTime ofTime = LocalDateTime.of(2023, 6, 10, 13, 1, 0);
        log.info("ofTime: {}", ofTime); // time: 2023-06-10T13:01

        // 使用 now 方法获取当前时间(默认包含了纳秒)
        LocalDateTime now = LocalDateTime.now();
        log.info("now: {}", now); // 2023-06-10T09:45:04.778

        // 使用 parse 方法传入时间字符串创建时间，由于LocalDateTime的特性，默认格式下要在年月日后面添加"T"
        LocalDateTime parse = LocalDateTime.parse("2023-06-10T13:01:00");
        log.info("parse: {}", parse); // 2023-06-10T13:01
    }

    /**
     * 199^200 > 200^199
     */
    @Test
    void testMax() {
        BigDecimal num199 = new BigDecimal(199);
        BigDecimal num200 = new BigDecimal(200);

        for (int i = 1; i <= 200; i++) {
            num199 = num199.multiply(new BigDecimal(199));
        }

        for (int i = 1; i <= 199; i++) {
            num200 = num200.multiply(new BigDecimal(200));
        }

        log.info("num199: {}", num199);
        log.info("num200: {}", num200);
        log.info("num199.compareTo(num200): {}", num199.compareTo(num200));
    }

    @Test
    void testNum () {
        float i = 4 / 3 * 3;
        log.info("4 / 3 * 3 = {}", i);
    }

    @Test
    void testsub() {
        String admdvs = "450600";
        String substring = admdvs.substring(0, 4);
        String[] flagArr = {"4507", "4508"};
        List<String> list = Arrays.asList(flagArr);
        log.info(substring);
        log.info("flag: {}", list.contains(substring));
    }

}
