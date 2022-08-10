package com.example.dto;

import lombok.Data;

/**
 * 接收登陆时的传参
 */

@Data
public class LoginFormDTO {
    private String phone;
    private String code;
    private String password;
}
