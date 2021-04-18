package com.uni.util;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

public class Response<R> implements Serializable {
    @Getter
    private static final int SUCCESS_CODE = 1;
    @Getter
    private static final int ERROR_CODE = 0;

    @Getter
    @Setter
    public int code;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private R data;

    private Response() {
    }

    /**
     * 是否是成功响应
     * @return
     */
    public boolean isSuccess() {
        return code == 1;
    }

    /**
     * 成功
     * @return
     */
    public static Response ok() {
        Response response = new Response<>();
        response.code = SUCCESS_CODE;
        return response;
    }

    /**
     * 错误
     * @param message
     * @return
     */
    public static Response error(String message) {
        Response response = new Response<>();
        response.code = ERROR_CODE;
        response.message = message;
        return response;
    }

    /**
     * 自定义code
     * @param code
     * @param message
     * @return
     */
    public static Response customize(int code, String message) {
        Response response = new Response<>();
        response.code = code;
        response.message = message;
        return response;
    }

    /**
     * 携带哪些数据
     * @param data
     * @return
     */
    public Response wrap(R data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}

