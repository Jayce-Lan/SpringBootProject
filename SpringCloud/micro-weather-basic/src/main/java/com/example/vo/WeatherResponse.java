package com.example.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息返回基类
 */

@Data
public class WeatherResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Weather data; // 消息数据
    private String status; // 消息状态
    private String desc; // 消息描述
}
