package com.example.util;

import java.util.ArrayList;
import java.util.List;

public class GroupListUtil<T> {
    /**
     * 源数据集
     */
    private List<T> lists;
    /**
     * 子集长度
     */
    private int index;

    /**
     * 构造方法，直接构造数据
     * @param lists 需要处理的List
     * @param index 需要拆分的长度
     */
    public GroupListUtil(List<T> lists, int index) {
        this.lists = lists;
        this.index = index;
    }

    /**
     * 拆分List成组，便于遍历，防止数据库压力过大
     * @return 分组后的List
     */
    public List<List<T>> getGroupList() {
        List<List<T>> groupList = new ArrayList<>();
        int listSize = lists.size();
        for (int i = 0; i < listSize; i += index) {
            if (i + index > listSize) {
                index = listSize - i;
            }
            List<T> newList = lists.subList(i, i + index);
            groupList.add(newList);
        }
        return groupList;
    }
}
