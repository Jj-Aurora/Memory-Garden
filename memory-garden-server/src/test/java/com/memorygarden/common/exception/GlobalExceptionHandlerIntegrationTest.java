package com.memorygarden.common.exception;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.common.result.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 4 集成测试 - GlobalExceptionHandler 边界条件与错误处理
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Phase4 集成测试 - GlobalExceptionHandler 边界条件与错误处理")
class GlobalExceptionHandlerIntegrationTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Nested
    @DisplayName("BusinessException 边界测试")
    class BusinessExceptionBoundaryTests {

        @Test
        @DisplayName("处理NOT_LOGIN_ERROR-返回40100")
        void testHandle_NotLoginError() {
            BusinessException ex = new BusinessException(ResultCode.NOT_LOGIN_ERROR);
            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(40100, response.getCode());
            assertEquals("未登录", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("处理NO_AUTH_ERROR-返回40101")
        void testHandle_NoAuthError() {
            BusinessException ex = new BusinessException(ResultCode.NO_AUTH_ERROR);
            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(40101, response.getCode());
        }

        @Test
        @DisplayName("处理NOT_FOUND_ERROR-返回40400")
        void testHandle_NotFoundError() {
            BusinessException ex = new BusinessException(ResultCode.NOT_FOUND_ERROR);
            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(40400, response.getCode());
        }

        @Test
        @DisplayName("处理PARAMS_ERROR-返回40000")
        void testHandle_ParamsError() {
            BusinessException ex = new BusinessException(ResultCode.PARAMS_ERROR);
            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(40000, response.getCode());
        }

        @Test
        @DisplayName("处理自定义错误码-返回对应code和message")
        void testHandle_CustomCode() {
            BusinessException ex = new BusinessException(99999, "自定义错误");
            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(99999, response.getCode());
            assertEquals("自定义错误", response.getMessage());
        }

        @Test
        @DisplayName("处理ResultCode+自定义消息")
        void testHandle_ResultCodeWithMessage() {
            BusinessException ex = new BusinessException(ResultCode.PARAMS_ERROR, "用户名格式不正确");
            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(40000, response.getCode());
            assertEquals("用户名格式不正确", response.getMessage());
        }
    }

    @Nested
    @DisplayName("MethodArgumentNotValidException 边界测试")
    class ValidationBoundaryTests {

        @Test
        @DisplayName("多个校验错误-返回第一个错误消息")
        void testHandle_MultipleErrors() {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
            bindingResult.addError(new FieldError("obj", "username", "用户名不能为空"));
            bindingResult.addError(new FieldError("obj", "password", "密码不能为空"));

            org.springframework.web.bind.MethodArgumentNotValidException ex = new org.springframework.web.bind.MethodArgumentNotValidException(null, bindingResult);
            BaseResponse<Void> response = handler.handleMethodArgumentNotValidException(ex);

            assertEquals(40000, response.getCode());
            assertTrue(response.getMessage().contains("用户名不能为空"));
        }

        @Test
        @DisplayName("单个校验错误-返回对应消息")
        void testHandle_SingleError() {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
            bindingResult.addError(new FieldError("obj", "email", "邮箱格式不正确"));

            org.springframework.web.bind.MethodArgumentNotValidException ex = new org.springframework.web.bind.MethodArgumentNotValidException(null, bindingResult);
            BaseResponse<Void> response = handler.handleMethodArgumentNotValidException(ex);

            assertEquals(40000, response.getCode());
            assertTrue(response.getMessage().contains("邮箱格式不正确"));
        }
    }

    @Nested
    @DisplayName("RuntimeException 和 Exception 边界测试")
    class RuntimeAndExceptionTests {

        @Test
        @DisplayName("NullPointerException被RuntimeException处理器捕获")
        void testHandle_NPE() {
            NullPointerException ex = new NullPointerException("空指针");
            BaseResponse<Void> response = handler.handleRuntimeException(ex);

            assertEquals(50000, response.getCode());
            assertEquals("系统内部异常", response.getMessage());
        }

        @Test
        @DisplayName("IllegalArgumentException被RuntimeException处理器捕获")
        void testHandle_IllegalArgument() {
            IllegalArgumentException ex = new IllegalArgumentException("非法参数");
            BaseResponse<Void> response = handler.handleRuntimeException(ex);

            assertEquals(50000, response.getCode());
        }

        @Test
        @DisplayName("Checked Exception被Exception处理器捕获")
        void testHandle_CheckedException() {
            Exception ex = new Exception("IO错误");
            BaseResponse<Void> response = handler.handleException(ex);

            assertEquals(50000, response.getCode());
            assertEquals("系统内部异常", response.getMessage());
        }
    }

    @Nested
    @DisplayName("BaseResponse 结构验证测试")
    class BaseResponseStructureTests {

        @Test
        @DisplayName("成功响应结构-code=0, message=success")
        void testSuccessResponse() {
            BaseResponse<String> response = BaseResponse.success("data");

            assertEquals(0, response.getCode());
            assertEquals("success", response.getMessage());
            assertEquals("data", response.getData());
        }

        @Test
        @DisplayName("成功响应无数据-data=null")
        void testSuccessResponseNoData() {
            BaseResponse<Void> response = BaseResponse.success();

            assertEquals(0, response.getCode());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("错误响应-data始终为null")
        void testErrorResponseDataNull() {
            BaseResponse<Void> response = BaseResponse.error(ResultCode.SYSTEM_ERROR);

            assertEquals(50000, response.getCode());
            assertNull(response.getData());
        }
    }
}
