package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.vo.UserVO;
import com.memorygarden.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Phase 4 集成测试 - UserController 边界条件与错误处理
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Phase4 集成测试 - UserController 边界条件与错误处理")
class UserControllerIntegrationTest {

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

    // ==================== 注册接口边界测试 ====================

    @Nested
    @DisplayName("注册接口边界条件测试")
    class RegisterBoundaryTests {

        @Test
        @DisplayName("注册-空请求体-Service层校验后返回成功或错误")
        void testRegister_EmptyBody() throws Exception {
            // 空请求体时，Jackson会创建默认对象，Service层负责校验
            when(userService.register(any(UserRegisterRequest.class))).thenReturn(1L);

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));
        }

        @Test
        @DisplayName("注册-用户名为空字符串")
        void testRegister_EmptyUsername() throws Exception {
            when(userService.register(any(UserRegisterRequest.class)))
                    .thenThrow(new RuntimeException("用户名不能为空"));

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }

        @Test
        @DisplayName("注册-密码为空字符串")
        void testRegister_EmptyPassword() throws Exception {
            when(userService.register(any(UserRegisterRequest.class)))
                    .thenThrow(new RuntimeException("密码不能为空"));

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"test\",\"password\":\"\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }

        @Test
        @DisplayName("注册-超长用户名")
        void testRegister_LongUsername() throws Exception {
            String longName = "a".repeat(256);
            when(userService.register(any(UserRegisterRequest.class)))
                    .thenThrow(new RuntimeException("用户名过长"));

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"" + longName + "\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }

        @Test
        @DisplayName("注册-特殊字符用户名")
        void testRegister_SpecialChars() throws Exception {
            when(userService.register(any(UserRegisterRequest.class))).thenReturn(1L);

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"test@#$\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0));
        }
    }

    // ==================== 登录接口边界测试 ====================

    @Nested
    @DisplayName("登录接口边界条件测试")
    class LoginBoundaryTests {

        @Test
        @DisplayName("登录-用户不存在-返回系统错误")
        void testLogin_UserNotFound() throws Exception {
            when(userService.login(any(UserLoginRequest.class)))
                    .thenThrow(new RuntimeException("用户不存在"));

            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"nonexist\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }

        @Test
        @DisplayName("登录-密码错误-返回系统错误")
        void testLogin_WrongPassword() throws Exception {
            when(userService.login(any(UserLoginRequest.class)))
                    .thenThrow(new RuntimeException("密码错误"));

            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"test\",\"password\":\"wrong\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }

        @Test
        @DisplayName("登录-成功返回Token格式为userId:uuid")
        void testLogin_SuccessTokenFormat() throws Exception {
            when(userService.login(any(UserLoginRequest.class))).thenReturn("1:abc123def456");

            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"test\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data").value("1:abc123def456"));
        }
    }

    // ==================== 获取当前用户边界测试 ====================

    @Nested
    @DisplayName("获取当前用户边界条件测试")
    class GetCurrentUserBoundaryTests {

        @Test
        @DisplayName("获取当前用户-完整字段返回")
        void testGetCurrentUser_FullFields() throws Exception {
            UserVO vo = new UserVO();
            vo.setId(1L);
            vo.setUsername("testuser");
            vo.setNickname("测试");
            vo.setAvatarUrl("http://avatar.png");
            vo.setCurrentStreak(7);
            vo.setMaxStreak(14);
            vo.setLastCheckIn(LocalDate.of(2026, 5, 20));
            when(userService.getCurrentUser(1L)).thenReturn(vo);

            mockMvc.perform(get("/api/user/current")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.username").value("testuser"))
                    .andExpect(jsonPath("$.data.nickname").value("测试"))
                    .andExpect(jsonPath("$.data.currentStreak").value(7))
                    .andExpect(jsonPath("$.data.maxStreak").value(14));
        }

        @Test
        @DisplayName("获取当前用户-用户不存在抛异常")
        void testGetCurrentUser_NotFound() throws Exception {
            when(userService.getCurrentUser(999L))
                    .thenThrow(new RuntimeException("用户不存在"));

            mockMvc.perform(get("/api/user/current")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 999L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }
    }

    // ==================== 修改个人信息边界测试 ====================

    @Nested
    @DisplayName("修改个人信息边界条件测试")
    class UpdateProfileBoundaryTests {

        @Test
        @DisplayName("修改个人信息-仅修改昵称")
        void testUpdateProfile_OnlyNickname() throws Exception {
            when(userService.updateProfile(1L, "新昵称", null)).thenReturn(true);

            mockMvc.perform(put("/api/user/profile")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nickname\":\"新昵称\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }

        @Test
        @DisplayName("修改个人信息-仅修改头像")
        void testUpdateProfile_OnlyAvatar() throws Exception {
            when(userService.updateProfile(1L, null, "http://new-avatar.png")).thenReturn(true);

            mockMvc.perform(put("/api/user/profile")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"avatarUrl\":\"http://new-avatar.png\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }

        @Test
        @DisplayName("修改个人信息-空请求体")
        void testUpdateProfile_EmptyBody() throws Exception {
            when(userService.updateProfile(1L, null, null)).thenReturn(true);

            mockMvc.perform(put("/api/user/profile")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }
    }
}
