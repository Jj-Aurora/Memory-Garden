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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GlobalExceptionHandler 全局异常处理测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler 全局异常处理测试")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Nested
    @DisplayName("业务异常处理测试")
    class BusinessExceptionTests {

        @Test
        @DisplayName("处理 BusinessException-返回对应错误码和消息")
        void testHandleBusinessException() {
            BusinessException ex = new BusinessException(ResultCode.NOT_LOGIN_ERROR);

            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(ResultCode.NOT_LOGIN_ERROR.getCode(), response.getCode());
            assertEquals(ResultCode.NOT_LOGIN_ERROR.getMessage(), response.getMessage());
        }

        @Test
        @DisplayName("处理自定义消息的 BusinessException")
        void testHandleBusinessException_CustomMessage() {
            BusinessException ex = new BusinessException(40000, "参数不能为空");

            BaseResponse<Void> response = handler.handleBusinessException(ex);

            assertEquals(40000, response.getCode());
            assertEquals("参数不能为空", response.getMessage());
        }
    }

    @Nested
    @DisplayName("参数校验异常处理测试")
    class ValidationExceptionTests {

        @Test
        @DisplayName("处理 MethodArgumentNotValidException-返回参数错误码")
        void testHandleValidationException() {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
            bindingResult.addError(new FieldError("obj", "username", "用户名不能为空"));
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            BaseResponse<Void> response = handler.handleMethodArgumentNotValidException(ex);

            assertEquals(ResultCode.PARAMS_ERROR.getCode(), response.getCode());
            assertTrue(response.getMessage().contains("用户名不能为空"));
        }
    }

    @Nested
    @DisplayName("运行时异常处理测试")
    class RuntimeExceptionTests {

        @Test
        @DisplayName("处理 RuntimeException-返回系统错误码")
        void testHandleRuntimeException() {
            RuntimeException ex = new RuntimeException("空指针");

            BaseResponse<Void> response = handler.handleRuntimeException(ex);

            assertEquals(ResultCode.SYSTEM_ERROR.getCode(), response.getCode());
        }
    }

    @Nested
    @DisplayName("未知异常处理测试")
    class ExceptionTests {

        @Test
        @DisplayName("处理 Exception-返回系统错误码")
        void testHandleException() {
            Exception ex = new Exception("未知错误");

            BaseResponse<Void> response = handler.handleException(ex);

            assertEquals(ResultCode.SYSTEM_ERROR.getCode(), response.getCode());
        }
    }
}
