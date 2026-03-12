package com.djxlzk.dsasystem.dto;

import lombok.Data;

@Data
public class ResultDTO<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ResultDTO<T> success(String msg, T data) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> ResultDTO<T> error(int code, String msg) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}