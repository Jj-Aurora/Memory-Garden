package com.memorygarden.integration;

import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.vo.UserVO;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserService 集成测试（真实数据库：注册→登录→获取用户）
 *
 * @author jLU
 * @date 2026-05-20
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("I-14: UserService 集成测试")
class UserServiceIntegrationTest {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    @DisplayName("注册→登录→获取用户 完整流程")
    void testRegisterLoginGetUser() {
        // 1. 注册
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("integration_test_user");
        registerRequest.setPassword("test123456");
        registerRequest.setNickname("集成测试");

        Long userId = userService.register(registerRequest);
        assertNotNull(userId);
        assertTrue(userId > 0);

        // 2. 验证数据库中用户存在
        User dbUser = userMapper.selectByUsername("integration_test_user");
        assertNotNull(dbUser);
        assertEquals("集成测试", dbUser.getNickname());
        assertEquals(0, dbUser.getCurrentStreak());
        assertEquals(1, dbUser.getStatus());

        // 3. 登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("integration_test_user");
        loginRequest.setPassword("test123456");

        String token = userService.login(loginRequest);
        assertNotNull(token);
        assertTrue(token.contains(":"));

        // 4. 获取当前用户
        UserVO userVO = userService.getCurrentUser(dbUser.getId());
        assertNotNull(userVO);
        assertEquals("integration_test_user", userVO.getUsername());
        assertEquals("集成测试", userVO.getNickname());
    }

    @Test
    @DisplayName("注册-用户名重复抛异常")
    void testRegister_DuplicateUsername() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("dup_user");
        request.setPassword("123456");

        userService.register(request);

        assertThrows(RuntimeException.class, () -> userService.register(request));
    }

    @Test
    @DisplayName("登录-密码错误抛异常")
    void testLogin_WrongPassword() {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("login_test_user");
        registerRequest.setPassword("correct_pwd");
        userService.register(registerRequest);

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("login_test_user");
        loginRequest.setPassword("wrong_pwd");

        assertThrows(RuntimeException.class, () -> userService.login(loginRequest));
    }

    @Test
    @DisplayName("修改个人信息-更新昵称和头像")
    void testUpdateProfile() {
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("profile_user");
        registerRequest.setPassword("123456");
        Long userId = userService.register(registerRequest);

        boolean result = userService.updateProfile(userId, "新昵称", "http://new-avatar.png");
        assertTrue(result);

        User updatedUser = userMapper.selectById(userId);
        assertEquals("新昵称", updatedUser.getNickname());
        assertEquals("http://new-avatar.png", updatedUser.getAvatarUrl());
    }
}
