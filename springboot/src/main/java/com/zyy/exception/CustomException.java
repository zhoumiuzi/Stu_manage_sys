package com.zyy.exception;

public class CustomException extends RuntimeException {

    //错误提示字段
    private String msg;

    public CustomException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
