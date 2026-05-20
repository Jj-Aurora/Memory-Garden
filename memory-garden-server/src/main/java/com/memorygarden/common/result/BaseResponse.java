package com.memorygarden.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结构
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 返回码（0=成功，非0=失败）
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 构造成功响应（带数据）
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 成功响应
     */
    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(0);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * 构造成功响应（无数据）
     *
     * @return 成功响应
     */
    public static BaseResponse<Void> success() {
        return success(null);
    }

    /**
     * 构造失败响应
     *
     * @param code    错误码
     * @param message 错误信息
     * @param <T>     数据类型
     * @return 失败响应
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * 构造失败响应（使用 ResultCode 枚举）
     *
     * @param resultCode 错误码枚举
     * @param <T>        数据类型
     * @return 失败响应
     */
    public static <T> BaseResponse<T> error(ResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMessage());
    }
}
