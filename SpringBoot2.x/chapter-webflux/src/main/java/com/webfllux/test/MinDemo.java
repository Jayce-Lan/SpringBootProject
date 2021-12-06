package com.webfllux.test;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

/**
 * 使用流的思路获取最小值
 */
@Slf4j
public class MinDemo {
    public static void main(String[] args) {
        int[] nums = {33, -666, 66, -55, 0, 99};
        //单线程
        int min = IntStream.of(nums).min().getAsInt();
        // 多线程
        int minParallel = IntStream.of(nums).parallel().min().getAsInt();
        log.info(String.valueOf(min));
        log.info(String.valueOf(minParallel));
    }
}
