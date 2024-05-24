package org.example.config.msgconfirm;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 用于交换机确认的工具类
 * 重写了交换机确认的方法 ConfirmCallback
 */
//@Configuration
@Slf4j
public class MyExchangeConfirmCallback
//        implements RabbitTemplate.ConfirmCallback
        {
//    @Resource
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * 注入
//     * 将接口的实现注入到RabbitTemplate中
//     * @ PostConstruct - 加载后构造
//     */
//    @PostConstruct
//    public void init() {
//        rabbitTemplate.setConfirmCallback(this);
//    }
    /**
     * 交换机确认回调方法
     * @param correlationData 消息内容，存储消息id及相关信息
     * @param ack 接收确认成功true；失败false
     * @param cause 存储失败原因，成功为null；失败为false
     */
//    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String messageId = "";
        if (correlationData != null) {
            messageId = StringUtils.hasText(correlationData.getId()) ? correlationData.getId(): "";
        }
        if (ack) {
            log.info("[Ex]Exchange is received success, the message id is [{}], and correlationData is [{}]",
                    messageId, JSONObject.toJSONString(correlationData));
        } else {
            log.error("[Ex Err]Exchange is received loss, the message id is [{}], and cause is [{}] , and correlationData is [{}]",
                    messageId, cause, JSONObject.toJSONString(correlationData));
        }
    }
}
