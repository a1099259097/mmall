package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceRespond<T> implements Serializable{

    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private ServiceRespond (int status){
        this.status = status;
    }

    private ServiceRespond (int status, String msg){
        this.status = status;
    }

    private ServiceRespond (int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServiceRespond (int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServiceRespond<T> createBySuccess() {
        return new ServiceRespond<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServiceRespond<T> createBySuccessMessage(String msg) {
        return new ServiceRespond<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServiceRespond<T> createBySuccess(T data) {
        return new ServiceRespond<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServiceRespond<T> createBySuccess(String msg, T data) {
        return new ServiceRespond<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServiceRespond<T> createByError () {
        return new ServiceRespond<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServiceRespond<T> createByErrorMessage (String errorMessage) {
        return new ServiceRespond<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServiceRespond<T> createByCodeError (int errorCode,String errorMessage) {
        return new ServiceRespond<T>(errorCode, errorMessage);
    }

}
