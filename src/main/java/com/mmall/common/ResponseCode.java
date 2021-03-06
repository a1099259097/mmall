package com.mmall.common;

public enum ResponseCode {

    SUCCESS(0,"success"),
    ERROR(1,"error"),
    NEED_LOGIIN(10,"NEED_LOGIIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private int code;
    private String desc;

    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
