package com.memorygarden.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求 DTO
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class UserLoginRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
