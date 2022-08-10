package com.example.service;

import com.example.dto.LoginFormDTO;
import com.example.dto.Result;

public interface UserService {
    Result sendCode(String phone);
    Result login(LoginFormDTO loginFormDTO);
}
