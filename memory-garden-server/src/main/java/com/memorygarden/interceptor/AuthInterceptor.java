package com.memorygarden.interceptor;

import com.memorygarden.common.constant.Constant;
import com.memorygarden.common.exception.BusinessException;
import com.memorygarden.common.result.ResultCode;
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
     * <p>当前 MVP 阶段 Token 格式为 userId:randomUUID，
     * 后续可替换为 JWT 解析。</p>
     *
     * @param token 登录 Token
     * @return 用户 ID，解析失败返回 null
     */
    private Long parseUserIdFromToken(String token) {
        try {
            int separatorIndex = token.indexOf(':');
            if (separatorIndex > 0) {
                return Long.parseLong(token.substring(0, separatorIndex));
            }
            return null;
        } catch (NumberFormatException e) {
            log.warn("Token 解析失败: {}", token);
            return null;
        }
    }
}
