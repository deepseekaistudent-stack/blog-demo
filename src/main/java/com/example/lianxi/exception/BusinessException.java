package com.example.lianxi.exception;

/**
 * 业务异常。
 * 带有错误码，由全局异常处理器统一拦截。
 */
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        this(500, message);
    }

    public int getCode() {
        return code;
    }
}
