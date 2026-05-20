package com.memorygarden.interceptor;

import com.memorygarden.common.constant.Constant;
import com.memorygarden.common.exception.BusinessException;
import com.memorygarden.common.result.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthInterceptor 鉴权拦截器测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthInterceptor 鉴权拦截器测试")
class AuthInterceptorTest {

    @InjectMocks
    private AuthInterceptor authInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("Token 校验测试")
    class TokenValidationTests {

        @Test
        @DisplayName("无 Authorization 头-抛出未登录异常")
        void testPreHandle_NoAuthHeader() {
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("空 Authorization 头-抛出未登录异常")
        void testPreHandle_EmptyAuthHeader() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "");
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("仅 Bearer 前缀无 Token-抛出未登录异常")
        void testPreHandle_BearerOnly() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, Constant.TOKEN_PREFIX);
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("无效 Token 格式-抛出未登录异常")
        void testPreHandle_InvalidToken() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "invalidtoken");
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }
    }

    @Nested
    @DisplayName("Token 解析测试")
    class TokenParseTests {

        @Test
        @DisplayName("有效 Token（无 Bearer 前缀）-解析成功并设置 userId")
        void testPreHandle_ValidTokenWithoutPrefix() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "123:abc456def");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(123L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("有效 Token（带 Bearer 前缀）-解析成功并设置 userId")
        void testPreHandle_ValidTokenWithPrefix() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, Constant.TOKEN_PREFIX + "456:xyz789");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(456L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token 中 userId 非数字-抛出未登录异常")
        void testPreHandle_NonNumericUserId() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "abc:def123");

            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }
    }
}
