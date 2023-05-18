package com.example.util;

import java.io.Serializable;

/**
 * 统一的参数返回
 */
public class WrapperResponse<T> implements Serializable {
    private static final long serialVersionUID = 5778573516446596671L;
    public static int SUCCESS = 0;
    public static int FAIL = -1;
    public static String MSG_SUCCESS = "成功";
    public static String MSG_WARNING = "成功但有告警";
    public static String MSG_FAIL = "失败";
    private int code = 0;
    private String type;
    private String message;
    private T data;

    public WrapperResponse() {
    }

    public WrapperResponse(int code, String type, String message, T data) {
        this.code = code;
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public static <T> WrapperResponse<T> success(T data) {
        return new WrapperResponse(SUCCESS, WrapperResponse.ResponseType.TYPE_SUCCESS.getType(), MSG_SUCCESS, data);
    }

    public static <T> WrapperResponse<T> success(String message, T data) {
        message = message != null && message.length() > 0 ? message : MSG_SUCCESS;
        return new WrapperResponse(SUCCESS, WrapperResponse.ResponseType.TYPE_SUCCESS.getType(), message, data);
    }

    public static <T> WrapperResponse<T> info(int code, String message, T data) {
        message = message != null && message.length() > 0 ? message : MSG_SUCCESS;
        return new WrapperResponse(code, WrapperResponse.ResponseType.TYPE_INFO.getType(), message, data);
    }

    public static <T> WrapperResponse<T> warning(int code, String message, T data) {
        message = message != null && message.length() > 0 ? message : MSG_WARNING;
        return new WrapperResponse(code, WrapperResponse.ResponseType.TYPE_WARNING.getType(), message, data);
    }

    public static <T> WrapperResponse<T> error(int code, String message, T data) {
        message = message != null && message.length() > 0 ? message : MSG_FAIL;
        return new WrapperResponse(code, WrapperResponse.ResponseType.TYPE_ERROR.getType(), message, data);
    }

    public static <T> WrapperResponse<T> fail(T data) {
        return new WrapperResponse(FAIL, WrapperResponse.ResponseType.TYPE_ERROR.getType(), MSG_FAIL, data);
    }

    public static <T> WrapperResponse<T> fail(String message, T data) {
        message = message != null && message.length() > 0 ? message : MSG_FAIL;
        return new WrapperResponse(FAIL, WrapperResponse.ResponseType.TYPE_ERROR.getType(), message, data);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static enum ResponseType {
        TYPE_SUCCESS("success"),
        TYPE_INFO("info"),
        TYPE_WARNING("warning"),
        TYPE_ERROR("error");

        private String type;

        private ResponseType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }
}
