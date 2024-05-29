package org.example.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.SendMsgAndTimeDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试postman配置JSON文件后批量调用
 */
@RestController
@RequestMapping("my-test-controller")
@Slf4j
public class MyTestController {
    @PostMapping("test")
    public SendMsgAndTimeDTO test(SendMsgAndTimeDTO sendMsgAndTimeDTO) {
        log.info("request object is ===> {}", JSONObject.toJSONString(sendMsgAndTimeDTO));
        return sendMsgAndTimeDTO;
    }
}
