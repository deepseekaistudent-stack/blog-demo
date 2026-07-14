package com.example.lianxi.common;

/**
 * 统一 REST API 响应体。
 * 格式：{ "code": 200, "message": "success", "data": {...} }
 */
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result() {}

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static Result<Void> error(int code, String message) {
        Result<Void> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
