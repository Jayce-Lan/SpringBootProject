package com.example.dao.adb;

import com.example.po.UserA;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdbUserMapper {
    List<UserA> queryUsers();
}
