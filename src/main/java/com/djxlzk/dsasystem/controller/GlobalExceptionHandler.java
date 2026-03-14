package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.mybatis.spring.MyBatisSystemException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     * 
     * @param e 异常对象
     * @return 响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultDTO.error(400, message);
    }

    /**
     * 处理绑定异常
     * 
     * @param e 异常对象
     * @return 响应结果
     */
    @ExceptionHandler(BindException.class)
    public ResultDTO<?> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultDTO.error(400, message);
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResultDTO<?> handleCannotGetJdbcConnectionException(CannotGetJdbcConnectionException e) {
        e.printStackTrace();
        return ResultDTO.error(503, "数据库连接失败，请检查 TiDB 账号/密码、IP白名单与网络");
    }

    @ExceptionHandler(MyBatisSystemException.class)
    public ResultDTO<?> handleMyBatisSystemException(MyBatisSystemException e) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof CannotGetJdbcConnectionException) {
                return handleCannotGetJdbcConnectionException((CannotGetJdbcConnectionException) cause);
            }
            cause = cause.getCause();
        }
        e.printStackTrace();
        return ResultDTO.error(500, "系统内部错误");
    }

    /**
     * 处理其他异常
     * 
     * @param e 异常对象
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public ResultDTO<?> handleException(Exception e) {
        e.printStackTrace();
        return ResultDTO.error(500, "系统内部错误");
    }
}