package com.example.dao.drds;

import com.example.po.UserA;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DrdsUserMapper {
    List<UserA> queryUsers();
    int addDrdsUser(UserA userA);
}
