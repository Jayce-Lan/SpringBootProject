package com.example;

import com.example.service.Address1Service;
import com.example.util.AddressNewKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class Chapter11CacheRedisMybatisApplicationTests {
	@Resource
	Address1Service address1Service;

	@Resource
	AddressNewKeyUtil addressNewKeyUtil;

	@Test
	void contextLoads() {
		log.info("addressList {}", address1Service.queryAddressList());
	}

	@Test
	void queryAddressNameByNo1() {
		log.info("addressName {}", address1Service.queryAddressByAddressNo("450101"));
	}

	@Test
	void newKey() throws Exception {
		log.info(addressNewKeyUtil.getAddressNewKey());
	}
}
