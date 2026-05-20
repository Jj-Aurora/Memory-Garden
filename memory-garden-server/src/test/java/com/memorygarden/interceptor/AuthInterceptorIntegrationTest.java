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

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("Token 格式边界测试")
    class TokenFormatTests {

        @Test
        @DisplayName("Token-多个冒号分隔符-取第一段作为userId")
        void testPreHandle_MultipleColons() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "123:abc:def:ghi");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(123L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-userId为0-合法解析")
        void testPreHandle_ZeroUserId() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "0:abc123");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(0L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-大数字userId-合法解析")
        void testPreHandle_LargeUserId() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "9999999999:abc123");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(9999999999L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-负数userId-合法解析（业务层校验）")
        void testPreHandle_NegativeUserId() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "-1:abc123");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(-1L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-仅有冒号无UUID部分-仍可解析userId")
        void testPreHandle_OnlyColon() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "123:");

            // "123:" 中冒号位置为3，substring(0,3)="123" 是合法数字
            // 冒号后为空字符串，但AuthInterceptor仅解析userId部分
            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(123L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Token-仅有userId无冒号-解析失败")
        void testPreHandle_OnlyUserId() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "123");

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
        @DisplayName("Authorization-空格分隔的Bearer Token")
        void testPreHandle_BearerWithSpace() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "Bearer 42:token123");

            boolean result = authInterceptor.preHandle(request, response, null);

            assertTrue(result);
            assertEquals(42L, request.getAttribute(AuthInterceptor.CURRENT_USER_ID));
        }

        @Test
        @DisplayName("Authorization-大小写混合的bearer前缀")
        void testPreHandle_CaseInsensitiveBearer() {
            // 当前实现仅支持 "Bearer " 前缀，小写 "bearer" 不会被识别
            request.addHeader(Constant.AUTHORIZATION_HEADER, "bearer 42:token123");

            // bearer（小写）不被识别为前缀，整个字符串作为token解析
            // "bearer 42:token123" 中 "bearer 42" 不是数字，解析失败
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("Authorization-Tab字符分隔")
        void testPreHandle_TabAsSeparator() {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "Bearer\t42:token123");

            // Tab不被识别为空格分隔符，整个 "Bearer\t42:token123" 作为token
            // 截取 "Bearer " 后为 "\t42:token123"，但startsWith("Bearer ")不匹配
            // 所以整个字符串作为token，"Bearer\t42" 不是数字
            BusinessException ex = assertThrows(BusinessException.class, () -> {
                authInterceptor.preHandle(request, response, null);
            });
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), ex.getCode());
        }

        @Test
        @DisplayName("Authorization-null值-抛出未登录异常")
        void testPreHandle_NullHeader() {
            // 不设置header

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
            request.addHeader(Constant.AUTHORIZATION_HEADER, "100:uuid");

            authInterceptor.preHandle(request, response, null);

            Object attr = request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
            assertInstanceOf(Long.class, attr);
            assertEquals(100L, attr);
        }

        @Test
        @DisplayName("解析成功后-attribute key为CURRENT_USER_ID常量")
        void testPreHandle_AttributeKey() throws Exception {
            request.addHeader(Constant.AUTHORIZATION_HEADER, "200:uuid");

            authInterceptor.preHandle(request, response, null);

            assertNotNull(request.getAttribute("currentUserId"));
            assertEquals(200L, request.getAttribute("currentUserId"));
        }
    }
}
