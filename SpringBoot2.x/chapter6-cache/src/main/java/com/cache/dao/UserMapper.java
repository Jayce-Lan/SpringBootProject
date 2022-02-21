package com.cache.dao;

import com.cache.pojo.UserADTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<UserADTO> queryUsers();
    UserADTO queryUserById(Integer id);
    int addUser(UserADTO userADTO);
}
