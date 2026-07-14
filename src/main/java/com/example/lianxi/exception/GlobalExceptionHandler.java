package com.example.lianxi.exception;

import com.example.lianxi.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器。
 * /api/* 路径返回 JSON，页面路径返回 error.html。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleBusiness(BusinessException e, HttpServletRequest req) {
        if (isRest(req)) {
            return Result.error(e.getCode(), e.getMessage());
        }
        req.setAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleException(Exception e, HttpServletRequest req) {
        log.error("系统异常: {} - {}", req.getRequestURI(), e.getMessage(), e);
        if (isRest(req)) {
            return Result.error(500, "服务器内部错误");
        }
        req.setAttribute("error", "服务器繁忙，请稍后再试");
        return "error";
    }

    private boolean isRest(HttpServletRequest req) {
        return req.getRequestURI().startsWith("/api/");
    }
}
