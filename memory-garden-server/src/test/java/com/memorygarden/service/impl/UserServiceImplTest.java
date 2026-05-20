package com.memorygarden.service.impl;

import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UserServiceImpl 全面单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest registerRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setNickname("测试用户");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setPassword("$2a$10$encoded");
        existingUser.setNickname("测试用户");
        existingUser.setCurrentStreak(0);
        existingUser.setMaxStreak(0);
        existingUser.setStatus(1);
        existingUser.setIsDeleted(0);
    }

    // ========== register 测试 ==========

    @Nested
    @DisplayName("注册功能测试")
    class RegisterTests {

        @Test
        @DisplayName("正常注册成功")
        void testRegister_Success() {
            when(userMapper.selectByUsername("testuser")).thenReturn(null);
            when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encoded");
            when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return 1;
            });

            Long userId = userService.register(registerRequest);

            assertNotNull(userId);
            assertEquals(1L, userId);
            verify(userMapper).insert(argThat(user ->
                    user.getUsername().equals("testuser") &&
                    user.getNickname().equals("测试用户") &&
                    user.getCurrentStreak() == 0 &&
                    user.getMaxStreak() == 0 &&
                    user.getStatus() == 1 &&
                    user.getIsDeleted() == 0
            ));
        }

        @Test
        @DisplayName("注册-无昵称时使用用户名作为昵称")
        void testRegister_NicknameDefaultToUsername() {
            registerRequest.setNickname(null);
            when(userMapper.selectByUsername("testuser")).thenReturn(null);
            when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encoded");
            when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return 1;
            });

            userService.register(registerRequest);

            verify(userMapper).insert(argThat(user ->
                    user.getNickname().equals("testuser")
            ));
        }

        @Test
        @DisplayName("注册-密码加密存储")
        void testRegister_PasswordEncoded() {
            when(userMapper.selectByUsername("testuser")).thenReturn(null);
            when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashed");
            when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return 1;
            });

            userService.register(registerRequest);

            verify(passwordEncoder).encode("password123");
            verify(userMapper).insert(argThat(user ->
                    user.getPassword().equals("$2a$10$hashed")
            ));
        }

        @Test
        @DisplayName("注册失败-用户名已存在")
        void testRegister_DuplicateUsername() {
            when(userMapper.selectByUsername("testuser")).thenReturn(existingUser);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("用户名已存在"));
            verify(userMapper, never()).insert(any());
        }

        @Test
        @DisplayName("注册失败-用户名为null")
        void testRegister_NullUsername() {
            registerRequest.setUsername(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("用户名不能为空"));
            verify(userMapper, never()).insert(any());
        }

        @Test
        @DisplayName("注册失败-用户名为空字符串")
        void testRegister_EmptyUsername() {
            registerRequest.setUsername("");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("用户名不能为空"));
        }

        @Test
        @DisplayName("注册失败-用户名为纯空格")
        void testRegister_BlankUsername() {
            registerRequest.setUsername("   ");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("用户名不能为空"));
        }

        @Test
        @DisplayName("注册失败-密码为null")
        void testRegister_NullPassword() {
            registerRequest.setPassword(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("密码不能为空"));
        }

        @Test
        @DisplayName("注册失败-密码为空字符串")
        void testRegister_EmptyPassword() {
            registerRequest.setPassword("");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("密码不能为空"));
        }

        @Test
        @DisplayName("注册失败-密码为纯空格")
        void testRegister_BlankPassword() {
            registerRequest.setPassword("   ");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.register(registerRequest);
            });

            assertTrue(exception.getMessage().contains("密码不能为空"));
        }
    }

    // ========== login 测试 ==========

    @Nested
    @DisplayName("登录功能测试")
    class LoginTests {

        @Test
        @DisplayName("登录成功-返回有效Token")
        void testLogin_Success() {
            when(userMapper.selectByUsername("testuser")).thenReturn(existingUser);
            when(passwordEncoder.matches("password123", "$2a$10$encoded")).thenReturn(true);

            String token = userService.login(new UserLoginRequest() {{
                setUsername("testuser");
                setPassword("password123");
            }});

            assertNotNull(token);
            assertTrue(token.length() > 0);
            assertFalse(token.contains("-"));
        }

        @Test
        @DisplayName("登录失败-用户不存在")
        void testLogin_UserNotFound() {
            when(userMapper.selectByUsername("nouser")).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.login(new UserLoginRequest() {{
                    setUsername("nouser");
                    setPassword("password123");
                }});
            });

            assertTrue(exception.getMessage().contains("用户不存在"));
        }

        @Test
        @DisplayName("登录失败-密码错误")
        void testLogin_WrongPassword() {
            when(userMapper.selectByUsername("testuser")).thenReturn(existingUser);
            when(passwordEncoder.matches("wrongpass", "$2a$10$encoded")).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.login(new UserLoginRequest() {{
                    setUsername("testuser");
                    setPassword("wrongpass");
                }});
            });

            assertTrue(exception.getMessage().contains("密码错误"));
        }

        @Test
        @DisplayName("登录-每次登录返回不同Token")
        void testLogin_DifferentTokens() {
            when(userMapper.selectByUsername("testuser")).thenReturn(existingUser);
            when(passwordEncoder.matches("password123", "$2a$10$encoded")).thenReturn(true);

            UserLoginRequest loginReq = new UserLoginRequest() {{
                setUsername("testuser");
                setPassword("password123");
            }};

            String token1 = userService.login(loginReq);
            String token2 = userService.login(loginReq);

            assertNotEquals(token1, token2);
        }
    }

    // ========== getCurrentUser 测试 ==========

    @Nested
    @DisplayName("获取用户信息测试")
    class GetCurrentUserTests {

        @Test
        @DisplayName("获取当前用户信息-字段映射正确")
        void testGetCurrentUser_FieldMapping() {
            when(userMapper.selectById(1L)).thenReturn(existingUser);

            UserVO userVO = userService.getCurrentUser(1L);

            assertNotNull(userVO);
            assertEquals(1L, userVO.getId());
            assertEquals("testuser", userVO.getUsername());
            assertEquals("测试用户", userVO.getNickname());
            assertEquals(0, userVO.getCurrentStreak());
            assertEquals(0, userVO.getMaxStreak());
        }

        @Test
        @DisplayName("获取当前用户-用户不存在抛异常")
        void testGetCurrentUser_NotFound() {
            when(userMapper.selectById(999L)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.getCurrentUser(999L);
            });

            assertTrue(exception.getMessage().contains("用户不存在"));
        }
    }

    // ========== updateProfile 测试 ==========

    @Nested
    @DisplayName("修改个人信息测试")
    class UpdateProfileTests {

        @Test
        @DisplayName("修改昵称和头像")
        void testUpdateProfile_BothFields() {
            when(userMapper.selectById(1L)).thenReturn(existingUser);
            when(userMapper.updateById(any(User.class))).thenReturn(1);

            boolean result = userService.updateProfile(1L, "新昵称", "http://avatar.png");

            assertTrue(result);
            verify(userMapper).updateById(argThat(user ->
                    user.getNickname().equals("新昵称") &&
                    user.getAvatarUrl().equals("http://avatar.png")
            ));
        }

        @Test
        @DisplayName("仅修改昵称-头像不变")
        void testUpdateProfile_OnlyNickname() {
            existingUser.setAvatarUrl("http://old.png");
            when(userMapper.selectById(1L)).thenReturn(existingUser);
            when(userMapper.updateById(any(User.class))).thenReturn(1);

            userService.updateProfile(1L, "新昵称", null);

            verify(userMapper).updateById(argThat(user ->
                    user.getNickname().equals("新昵称") &&
                    user.getAvatarUrl().equals("http://old.png")
            ));
        }

        @Test
        @DisplayName("仅修改头像-昵称不变")
        void testUpdateProfile_OnlyAvatar() {
            when(userMapper.selectById(1L)).thenReturn(existingUser);
            when(userMapper.updateById(any(User.class))).thenReturn(1);

            userService.updateProfile(1L, null, "http://new.png");

            verify(userMapper).updateById(argThat(user ->
                    user.getNickname().equals("测试用户") &&
                    user.getAvatarUrl().equals("http://new.png")
            ));
        }

        @Test
        @DisplayName("修改个人信息-用户不存在抛异常")
        void testUpdateProfile_UserNotFound() {
            when(userMapper.selectById(999L)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userService.updateProfile(999L, "昵称", null);
            });

            assertTrue(exception.getMessage().contains("用户不存在"));
        }

        @Test
        @DisplayName("修改个人信息-数据库更新失败返回false")
        void testUpdateProfile_UpdateFailed() {
            when(userMapper.selectById(1L)).thenReturn(existingUser);
            when(userMapper.updateById(any(User.class))).thenReturn(0);

            boolean result = userService.updateProfile(1L, "新昵称", null);

            assertFalse(result);
        }
    }

    // ========== getByUsername / getById 测试 ==========

    @Nested
    @DisplayName("查询用户测试")
    class QueryUserTests {

        @Test
        @DisplayName("根据用户名查询-存在")
        void testGetByUsername_Exists() {
            when(userMapper.selectByUsername("testuser")).thenReturn(existingUser);

            User user = userService.getByUsername("testuser");

            assertNotNull(user);
            assertEquals("testuser", user.getUsername());
        }

        @Test
        @DisplayName("根据用户名查询-不存在返回null")
        void testGetByUsername_NotExists() {
            when(userMapper.selectByUsername("nouser")).thenReturn(null);

            User user = userService.getByUsername("nouser");

            assertNull(user);
        }

        @Test
        @DisplayName("根据ID查询-存在")
        void testGetById_Exists() {
            when(userMapper.selectById(1L)).thenReturn(existingUser);

            User user = userService.getById(1L);

            assertNotNull(user);
            assertEquals(1L, user.getId());
        }

        @Test
        @DisplayName("根据ID查询-不存在返回null")
        void testGetById_NotExists() {
            when(userMapper.selectById(999L)).thenReturn(null);

            User user = userService.getById(999L);

            assertNull(user);
        }
    }
}
