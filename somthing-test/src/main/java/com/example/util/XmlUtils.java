package com.example.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * XML转换的工具类
 */
public class XmlUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * xml字符串转为对象
     * @param xml xml
     * @param clazz 被转换成为的对象
     * @param <T> 对象类型
     * @return 返回对象
     * @throws JsonProcessingException
     */
    public static <T> T xmlStrToObject(String xml, Class<T> clazz) throws JsonProcessingException {
        // 字符串为null时自动忽略，不再序列化
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return OBJECT_MAPPER.readValue(xml, clazz);
    }
}
