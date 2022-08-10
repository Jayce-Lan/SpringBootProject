package com.example.service.impl;

import com.example.entity.PageNumAndSize;
import com.example.entity.ShopType;
import com.example.mapper.ShopTypeMapper;
import com.example.service.ShopTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("shopTypeService")
@Transactional
public class ShopTypeServiceImpl implements ShopTypeService {
    @Resource
    ShopTypeMapper shopTypeMapper;

    @Override
    public PageInfo<ShopType> queryShopTypeByPage(PageNumAndSize pageNumAndSize) {
        PageInfo<ShopType> shopTypePageInfo = PageHelper.startPage(pageNumAndSize.getPageNum(), pageNumAndSize.getPageSize()).doSelectPageInfo(() ->
                shopTypeMapper.queryShopType());
        return shopTypePageInfo;
    }

    @Override
    public List<ShopType> queryShopTypeInResylt() {
        return shopTypeMapper.queryShopType();
    }
}
