package com.example.service;

import com.example.dao.Address1DAO;
import com.example.pojo.AddressDTO;
import com.example.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("address1Service")
@Slf4j
public class Address1ServiceImpl implements Address1Service{
    @Resource
    Address1DAO address1DAO;

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public List<AddressDTO> queryAddressList() {
        return address1DAO.queryAddressList();
    }

    /**
     * 使用 redis 缓存键值对
     * @param addressNo
     * @return
     */
    @Override
    public String queryAddressByAddressNo(String addressNo) {
        Map<String, String> addressMap= new HashMap<>();
        addressMap = redisTemplate.opsForHash().entries(RedisKeyUtil.ADDRESS);
        String addressName = addressMap.get(addressNo);
        // 如果区划名称不存在，那么重新查询数据库
        if (!StringUtils.hasText(addressName)) {
            log.info("======被执行=======");
            AddressDTO addressDTO = address1DAO.queryAddressByAddressNo(addressNo);
            // 如果传入的区划编码没有获取到区划，那么直接返回null
            if (StringUtils.isEmpty(addressDTO)) {
                return null;
            }

            Map<String, String> newAddressMap = new HashMap<>();
            newAddressMap.put(addressDTO.getAddressNo(), addressDTO.getAddressName());
            redisTemplate.opsForHash().putAll(RedisKeyUtil.ADDRESS, newAddressMap);
            return addressDTO.getAddressName();
        }
        return addressName;
    }
}
