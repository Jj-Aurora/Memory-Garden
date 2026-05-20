package com.memorygarden.service.impl;

import com.memorygarden.common.exception.BusinessException;
import com.memorygarden.common.result.ResultCode;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.vo.UserVO;
import com.memorygarden.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 新用户 ID
     */
    @Override
    public Long register(UserRegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码不能为空");
        }

        User existing = userMapper.selectByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setCurrentStreak(0);
        user.setMaxStreak(0);
        user.setStatus(1);
        user.setIsDeleted(0);

        userMapper.insert(user);
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return JWT Token
     */
    @Override
    public String login(UserLoginRequest request) {
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户名或密码错误");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户名或密码错误");
        }

        return user.getId() + ":" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户 ID
     * @return 用户视图对象
     */
    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND_ERROR, "用户不存在");
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    /**
     * 修改个人信息
     *
     * @param userId    用户 ID
     * @param nickname  昵称
     * @param avatarUrl 头像 URL
     * @return 是否成功
     */
    @Override
    public boolean updateProfile(Long userId, String nickname, String avatarUrl) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND_ERROR, "用户不存在");
        }

        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (avatarUrl != null) {
            user.setAvatarUrl(avatarUrl);
        }

        return userMapper.updateById(user) > 0;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据 ID 查询用户
     *
     * @param userId 用户 ID
     * @return 用户实体
     */
    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }
}
