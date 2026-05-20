package com.memorygarden.common.constant;

/**
 * 系统常量定义
 *
 * @author jLU
 * @date 2026-05-20
 */
public class Constant {

    /**
     * 用户登录状态 Key
     */
    public static final String USER_LOGIN_STATE = "user_login_state";

    /**
     * Token 过期时间（秒）：24 小时
     */
    public static final int TOKEN_EXPIRE_SECONDS = 86400;

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 请求头 Authorization 字段名
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private Constant() {
    }
}
