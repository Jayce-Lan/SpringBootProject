package com.example.util;

/**
 * 字典值工具类
 */
public class CommonDict {
    /**
     * 布隆过滤器的预计容量
     */
    public static final int EXPECTED_INSERTIONS = 1000000;
    /**
     * 布隆过滤器误判率
     */
    public static final double CACHE_FPP = 0.001;
    /**
     * User的Redis键前缀
     */
    public static final String USER_TO_CACHE_KEY = "user.user_";
    /**
     * Redis所的key前缀
     */
    public static final String USER_TO_QUERY_LOCK_KEY = "user.lock_";
}
