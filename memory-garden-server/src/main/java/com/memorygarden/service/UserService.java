package com.memorygarden.service;

import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 新用户 ID
     */
    Long register(UserRegisterRequest request);

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return JWT Token
     */
    String login(UserLoginRequest request);

    /**
     * 获取当前登录用户信息
     *
     * @param userId 用户 ID
     * @return 用户视图对象
     */
    UserVO getCurrentUser(Long userId);

    /**
     * 修改个人信息
     *
     * @param userId   用户 ID
     * @param nickname 昵称
     * @param avatarUrl 头像 URL
     * @return 是否成功
     */
    boolean updateProfile(Long userId, String nickname, String avatarUrl);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    User getByUsername(String username);

    /**
     * 根据 ID 查询用户
     *
     * @param userId 用户 ID
     * @return 用户实体
     */
    User getById(Long userId);
}
