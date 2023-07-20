package com.example.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * 布隆过滤器
 */
public class BloomFilterUtil {
    private static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
            CommonDict.EXPECTED_INSERTIONS,
            CommonDict.CACHE_FPP);

    /**
     * 向BloomFilter中添加元素
     * @param key
     */
    public static void add(String key) {
        bloomFilter.put(key);
    }

    /**
     * 判定元素是否存在于BloomFilter中
     * @param key
     * @return
     */
    public static boolean mightContain(String key) {
        return bloomFilter.mightContain(key);
    }
}
