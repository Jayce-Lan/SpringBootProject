package com.example.utils;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";
    public static final String CACHE_SHOP_TYPE = "cache:shopType";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";


    public static final String INCR_ID_KEY = "incr:";


    // 用于Redis生成数据库主键的键
    public static final String USER_LOGIN_ID = "USER_LOGIN_ID";
    // 订单id
    public static final String ORDER_ID = "ORDER_ID";


    /**
     * 模拟一个全局的userid
     */
    public static final Long IMITATE_USER_ID = 1027L;
}
