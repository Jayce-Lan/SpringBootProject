package org.example.entity;

import lombok.Data;

/**
 * 存储发送消息的消息及TTL时间
 */
@Data
public class SendMsgAndTimeDTO {
    private String message;
    private Integer ttlTime;
}
