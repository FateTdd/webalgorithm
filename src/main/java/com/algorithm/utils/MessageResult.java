package com.algorithm.utils;

import java.io.Serializable;

public class MessageResult<T> implements Serializable {
    //serialization
    private static final long serialVersionUID = 5586118057859274971L;


    private String code = "200";

    private String message;

    private T data;

    public MessageResult() {
    }

    public MessageResult(T data) {
        this.data = data;
    }

    public MessageResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> MessageResult buildSuccess(T data) {
        return new MessageResult(data);
    }

    public static MessageResult buildFail(String code, String message) {
        return new MessageResult(code, message);
    }
    public static MessageResult buildFail( String message) {
        return new MessageResult("500", message);
    }

    @Override
    public String toString() {
        return "{'ResultDto':{" +
                "'code':" + code +
                ", 'message':'" + message + '\'' +
                ", 'data':" + data +"}";
    }

}
