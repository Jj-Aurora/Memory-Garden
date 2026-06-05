package com.memorygarden.interceptor;

import com.memorygarden.common.constant.Constant;
import com.memorygarden.common.exception.BusinessException;
import com.memorygarden.common.result.ResultCode;
import com.memorygarden.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录鉴权拦截器
 *
 * <p>从请求头 Authorization 中提取 Token，解析出 userId，
 * 存入 request attribute 供后续 Controller 使用。</p>
 *
 * @author jLU
 * @date 2026-05-20
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * Request attribute 中存储当前用户 ID 的 key
     */
    public static final String CURRENT_USER_ID = "currentUserId";

    /**
     * 预处理请求：校验登录状态
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器
     * @return true=放行，false=拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(Constant.AUTHORIZATION_HEADER);

        if (authorization == null || authorization.trim().isEmpty()) {
            throw new BusinessException(ResultCode.NOT_LOGIN_ERROR);
        }

        String token = authorization;
        if (authorization.startsWith(Constant.TOKEN_PREFIX)) {
            token = authorization.substring(Constant.TOKEN_PREFIX.length());
        }

        if (token.trim().isEmpty()) {
            throw new BusinessException(ResultCode.NOT_LOGIN_ERROR);
        }

        Long userId = parseUserIdFromToken(token);
        if (userId == null) {
            throw new BusinessException(ResultCode.NOT_LOGIN_ERROR);
        }

        request.setAttribute(CURRENT_USER_ID, userId);
        return true;
    }

    /**
     * 从 Token 中解析用户 ID
     *
     * <p>使用 JWT 签名验证，确保 Token 未被篡改且未过期。</p>
     *
     * @param token 登录 Token
     * @return 用户 ID，验证失败返回 null
     */
    private Long parseUserIdFromToken(String token) {
        return JwtUtils.verifyAndGetUserId(token);
    }
}
