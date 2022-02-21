package com.cache.service;

import com.cache.pojo.UserADTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<UserADTO> queryUsers();
    UserADTO queryUserById(UserADTO userADTO);
    String addUser(UserADTO userADTO) throws Exception;
}
