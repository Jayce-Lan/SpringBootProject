package com.example.nginx.controller;

import com.example.entity.UserToCacheDTO;
import com.example.util.WrapperResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("callThisController")
@Slf4j
public class CallThisController {
    /**
     *使用 form-data 传参
     * @param userToCacheDTO key为属性，value为值
     * @return
     */
    @RequestMapping(path = "/test1", method = RequestMethod.POST)
    public WrapperResponse<UserToCacheDTO> test1(UserToCacheDTO userToCacheDTO) {
        log.info("userToCacheDTO >>>>> {}", userToCacheDTO);
        return WrapperResponse.success(userToCacheDTO);
    }

    /**
     * 使用 raw 传参
     * @param userToCacheDTO 整个对象写作 json 放在raw中
     * @return
     */
    @RequestMapping(path = "/test2", method = RequestMethod.POST)
    public WrapperResponse<UserToCacheDTO> test2(@RequestBody UserToCacheDTO userToCacheDTO) {
        log.info("userToCacheDTO >>>>> {}", userToCacheDTO);
        return WrapperResponse.success(userToCacheDTO);
    }
}
