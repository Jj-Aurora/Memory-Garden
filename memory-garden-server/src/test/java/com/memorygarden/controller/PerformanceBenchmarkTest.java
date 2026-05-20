package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.vo.UserVO;
import com.memorygarden.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Phase 4 性能基准测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Phase4 性能基准测试")
class PerformanceBenchmarkTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("注册接口-100次调用性能基准")
    void testRegister_PerformanceBenchmark() throws Exception {
        when(userService.register(any(UserRegisterRequest.class))).thenReturn(1L);

        int iterations = 100;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"user" + i + "\",\"password\":\"123456\"}"));
        }

        long elapsed = System.currentTimeMillis() - startTime;
        double avgMs = (double) elapsed / iterations;
        System.out.println("[PERF] 注册接口 " + iterations + " 次调用总耗时: " + elapsed + "ms, 平均: " + String.format("%.2f", avgMs) + "ms/次");

        // 断言：平均响应时间应小于 50ms
        assertTrue(avgMs < 50, "平均响应时间 " + avgMs + "ms 超过 50ms 阈值");
    }

    @Test
    @DisplayName("登录接口-100次调用性能基准")
    void testLogin_PerformanceBenchmark() throws Exception {
        when(userService.login(any(UserLoginRequest.class))).thenReturn("1:abc123");

        int iterations = 100;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"test\",\"password\":\"123456\"}"));
        }

        long elapsed = System.currentTimeMillis() - startTime;
        double avgMs = (double) elapsed / iterations;
        System.out.println("[PERF] 登录接口 " + iterations + " 次调用总耗时: " + elapsed + "ms, 平均: " + String.format("%.2f", avgMs) + "ms/次");

        assertTrue(avgMs < 50, "平均响应时间 " + avgMs + "ms 超过 50ms 阈值");
    }

    @Test
    @DisplayName("获取当前用户-100次调用性能基准")
    void testGetCurrentUser_PerformanceBenchmark() throws Exception {
        UserVO vo = new UserVO();
        vo.setId(1L);
        vo.setUsername("test");
        when(userService.getCurrentUser(1L)).thenReturn(vo);

        int iterations = 100;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            mockMvc.perform(get("/api/user/current")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L));
        }

        long elapsed = System.currentTimeMillis() - startTime;
        double avgMs = (double) elapsed / iterations;
        System.out.println("[PERF] 获取当前用户 " + iterations + " 次调用总耗时: " + elapsed + "ms, 平均: " + String.format("%.2f", avgMs) + "ms/次");

        assertTrue(avgMs < 50, "平均响应时间 " + avgMs + "ms 超过 50ms 阈值");
    }

    private void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }
}
