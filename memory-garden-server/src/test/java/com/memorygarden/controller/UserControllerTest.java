package com.memorygarden.controller;

import com.memorygarden.common.constant.Constant;
import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.common.result.BaseResponse;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 接口测试")
class UserControllerTest {

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

    @Nested
    @DisplayName("注册接口测试 POST /api/user/register")
    class RegisterTests {

        @Test
        @DisplayName("注册成功-返回200和新用户ID")
        void testRegister_Success() throws Exception {
            when(userService.register(any(UserRegisterRequest.class))).thenReturn(1L);

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"testuser\",\"password\":\"123456\",\"nickname\":\"测试\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data").value(1));
        }

        @Test
        @DisplayName("注册失败-用户名重复抛异常")
        void testRegister_DuplicateUsername() throws Exception {
            when(userService.register(any(UserRegisterRequest.class)))
                    .thenThrow(new RuntimeException("用户名已存在"));

            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"testuser\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }
    }

    @Nested
    @DisplayName("登录接口测试 POST /api/user/login")
    class LoginTests {

        @Test
        @DisplayName("登录成功-返回Token")
        void testLogin_Success() throws Exception {
            when(userService.login(any(UserLoginRequest.class))).thenReturn("1:abc123");

            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"testuser\",\"password\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data").value("1:abc123"));
        }

        @Test
        @DisplayName("登录失败-密码错误")
        void testLogin_WrongPassword() throws Exception {
            when(userService.login(any(UserLoginRequest.class)))
                    .thenThrow(new RuntimeException("密码错误"));

            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"testuser\",\"password\":\"wrong\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }
    }

    @Nested
    @DisplayName("获取当前用户接口测试 GET /api/user/current")
    class GetCurrentUserTests {

        @Test
        @DisplayName("获取当前用户成功")
        void testGetCurrentUser_Success() throws Exception {
            UserVO userVO = new UserVO();
            userVO.setId(1L);
            userVO.setUsername("testuser");
            userVO.setNickname("测试");
            when(userService.getCurrentUser(1L)).thenReturn(userVO);

            mockMvc.perform(get("/api/user/current")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data.username").value("testuser"));
        }
    }

    @Nested
    @DisplayName("修改个人信息接口测试 PUT /api/user/profile")
    class UpdateProfileTests {

        @Test
        @DisplayName("修改个人信息成功")
        void testUpdateProfile_Success() throws Exception {
            when(userService.updateProfile(eq(1L), eq("新昵称"), eq("http://avatar.png"))).thenReturn(true);

            mockMvc.perform(put("/api/user/profile")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nickname\":\"新昵称\",\"avatarUrl\":\"http://avatar.png\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data").value(true));
        }
    }
}
