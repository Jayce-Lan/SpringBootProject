package com.example;

import com.example.entity.PageNumAndSize;
import com.example.entity.ShopType;
import com.example.service.ShopTypeService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class RedisAppApplicationTests {
	@Resource
	ShopTypeService shopTypeService;

	@Test
	void queryShopType() {
		PageNumAndSize pageNumAndSize = new PageNumAndSize();
		pageNumAndSize.setPageSize(3);
		pageNumAndSize.setPageNum(1);
		PageInfo<ShopType> shopTypePageInfo = shopTypeService.queryShopTypeByPage(pageNumAndSize);
		log.info("shopType: {}", shopTypePageInfo);
	}

	@Test
	void testOr() {
		String text = null;
		String text2 = "123";
		if (!StringUtils.hasText(text) || !text.equals(text2)) {
			log.info("text为null");
		}
	}

}
