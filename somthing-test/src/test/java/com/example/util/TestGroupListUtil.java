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
//        testGroupListUtil.testGoupListUtil();
        testGroupListUtil.testGetZero();
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

    /**
     * 从0只能+1或者*2，需要多少步到114514解题思路：
     * 从114514变回0需要多少步：
     * 如果当前数是偶数，直接除以二；
     * 如果是奇数，减去一。
     * 重复上述步骤，直到当前数变成0就得到了最少步骤。
     */
    public void testGetZero() {
        int num = 114514;
        int count = 0;

        while (num > 0) {
            if (num % 2 == 0) {
                num = num / 2;
            } else {
                num = num - 1;
            }
            count++;
        }
        log.info("总共进行了 {} 次", count);
    }
}
