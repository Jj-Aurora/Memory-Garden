package com.memorygarden.common.exception;

import com.memorygarden.common.result.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author jLU
 * @date 2026-05-20
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    /**
     * 使用错误码和消息构造业务异常
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用 ResultCode 枚举构造业务异常
     *
     * @param resultCode 错误码枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 使用 ResultCode 枚举和自定义消息构造业务异常
     *
     * @param resultCode 错误码枚举
     * @param message    自定义错误信息
     */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }
}
