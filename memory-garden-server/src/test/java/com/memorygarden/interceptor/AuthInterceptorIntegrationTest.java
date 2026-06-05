package com.memorygarden.interceptor;

import com.memorygarden.common.constant.Constant;
import com.memorygarden.common.exception.BusinessException;
import com.memorygarden.common.result.ResultCode;
import com.memorygarden.common.util.JwtUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 4 集成测试 - AuthInterceptor 边界条件与错误处理
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Phase4 集成测试 - AuthInterceptor 边界条件与错误处理")
class AuthInterceptorIntegrationTest {

    @InjectMocks
    private AuthInterceptor authInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeAll
    static void initJwtSecret() throws Exception {
        Field secretField = JwtUtils.class.getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(null, "test-jwt-secret-key-for-unit-tests-only");
    }

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("Token 格式边界测试")
    class TokenFormatTests {

        @Test
        @DisplayName("Token-userId为0-合法解析")
        void testPreHandle_ZeroUserId() throws Exception {
            String token = JwtUtils.generateToken(0L);
            request.addHeader(Constant.AUTHORIZATION_HEADER, token);

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(0L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-大数字userId-合法解析")
        void testPreHandle_LargeUserId() throws Exception {
            String token = JwtUtils.generateToken(9999999999L);
            request.addHeader(Constant.AUTHORIZATION_HEADER, token);

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(9999999999L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-伪造Token-解析失败")
        void testPreHandle_ForgedToken() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "eyJhbGciOiJIUzI1NiJ9.fakepayload.fakesignature");

            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("Token-仅数字字符串-解析失败")
        void testPreHandle_OnlyNumbers() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "123456");

            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }
    }

    @Nested
    @DisplayName("Authorization 头格式测试")
    class AuthHeaderFormatTests {

        @Test
        @DisplayName("Authorization-Bearer前缀+有效JWT")
        void testPreHandle_BearerWithJwt() throws Exception {
            String token = JwtUtils.generateToken(42L);
            request.addHeader(Constant.AUTHORIZATION_HEADER, Constant.TOKEN_PREFIX + token);

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(42L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Authorization-大小写混合的bearer前缀-不被识别")
        void testPreHandle_CaseInsensitiveBearer() {
            String token = JwtUtils.generateToken(42L);
            request.addHeader(Constant.AUTHORIZATION_HEADER, "bearer " + token);

            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("Authorization-null值-抛出未登录异常")
        void testPreHandle_NullHeader() {
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("Authorization-纯空格-抛出未登录异常")
        void testPreHandle_WhitespaceOnly() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "   ");

            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }
    }

    @Nested
    @DisplayName("Request Attribute 设置测试")
    class RequestAttributeTests {

        @Test
        @DisplayName("解析成功后-attribute类型为Long")
        void testPreHandle_AttributeType() throws Exception {
            String token = JwtUtils.generateToken(100L);
            request.addHeader(Constant.AUTHORIZATION_HEADER, token);

            authInterceptor.preHandle(request, response, null);

            Object attr = request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
            assertInstanceOf(Long.class, attr);
            assertEquals(100L, attr);
        }

        @Test
        @DisplayName("解析成功后-attribute key为CURRENT_USER_ID常量")
        void testPreHandle_AttributeKey() throws Exception {
            String token = JwtUtils.generateToken(200L);
            request.addHeader(Constant.AUTHORIZATION_HEADER, token);

            authInterceptor.preHandle(request, response, null);

            assertNotNull(request.getAttribute("currentUserId"));
            assertEquals(200L, request.getAttribute("currentUserId"));
        }
    }
}
