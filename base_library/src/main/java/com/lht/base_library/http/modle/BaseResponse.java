package com.lht.base_library.http.modle;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName(value = "state", alternate = {"code", "errorCode"})
    private int code;

    @SerializedName(value = "message", alternate = {"errorMsg", "msg"})
    private String message;

    @SerializedName(value = "data", alternate = {"info"})
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
