package com.example.mybatis;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityDAO {
    List<City> queryCity();
}
