package com.example.util;

import com.example.entity.TestLombokDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@Slf4j
public class TestLombok {
    @Test
    void testGetterAndSetter() {
        TestLombokDTO testLombokDTO = new TestLombokDTO();
        testLombokDTO.setName("卢本伟");
        testLombokDTO.setBalance(new BigDecimal("10.0"));
        // 打印结果： getter ==> name: 卢本伟 balance: 10.0
        log.info("getter ==> name: {} balance: {}", testLombokDTO.getName(), testLombokDTO.getBalance());
        // 打印结果： com.example.entity.TestLombokDTO@7a83ccd2
        log.info(testLombokDTO.toString());
    }
}
