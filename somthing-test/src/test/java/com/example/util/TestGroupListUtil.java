package com.example.util;

import com.example.entity.UserToCacheDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestGroupListUtil {
    public static List<UserToCacheDTO> userToCacheDTOS = new ArrayList<>();
    static {
        for (int i = 0; i < 23; i++) {
            UserToCacheDTO user = new UserToCacheDTO();
            user.setName("Tom" + i);
            user.setAge(20 + i);
            user.setId(String.valueOf(i));
            userToCacheDTOS.add(user);
        }
    }

    public static void main(String[] args) {
        TestGroupListUtil testGroupListUtil = new TestGroupListUtil();
        testGroupListUtil.testGoupListUtil();
    }

    public void testGoupListUtil() {
        GroupListUtil<UserToCacheDTO> groupList = new GroupListUtil<>(userToCacheDTOS, 4);
        List<List<UserToCacheDTO>> groupList1 = groupList.getGroupList();
        for (List<UserToCacheDTO> item : groupList1) {
            item.forEach(item1 -> {
                log.info("item1 >>>>> {}", item1);
            });
            log.info("分组 >>>>> 成功！");
        }
    }
}
