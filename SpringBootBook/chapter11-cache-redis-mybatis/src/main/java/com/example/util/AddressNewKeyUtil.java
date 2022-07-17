package com.example.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Configuration
public class AddressNewKeyUtil {
    @Resource
    RedisTemplate redisTemplate;

    /**
     * 通过 Redis 去生成主键
     * @return
     * @throws Exception
     */
    public String getAddressNewKey() throws Exception {
        String addressNo = ((Integer) redisTemplate.opsForValue().get(RedisKeyUtil.ADDRESS_NO)).toString();
        if (!StringUtils.hasText(addressNo)) {
            redisTemplate.opsForValue().set(RedisKeyUtil.ADDRESS_NO, 1);
            return "000000";
        }
        int addressNoInt = Integer.parseInt(addressNo);
        addressNoInt++;
        redisTemplate.opsForValue().set(RedisKeyUtil.ADDRESS_NO, addressNoInt);
        return addressNoSizeIs6(addressNo);
    }

    /**
     * 生成区划编码，如果不足6位，将补齐至满足；如果超长则报错
     * @param addressNo
     * @return
     * @throws Exception
     */
    private static String addressNoSizeIs6(String addressNo) throws Exception {
        if (addressNo.length() < 6) {
            int length = 6 - addressNo.length();
            for (int i = 0; i < length; i++) {
                addressNo = "0" + addressNo;
            }
            return addressNo;
        }

        if (addressNo.length() > 6) {
            throw new Exception("执行错误，请勿录入大于6位的区划编码！");
        }
        return addressNo;
    }
}
