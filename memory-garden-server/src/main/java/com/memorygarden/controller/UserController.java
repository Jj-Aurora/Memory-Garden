package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.UserLoginRequest;
import com.memorygarden.model.dto.UserRegisterRequest;
import com.memorygarden.model.vo.UserVO;
import com.memorygarden.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户模块")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口。
     *
     * <p>用途：前端提交注册信息，后端完成参数校验、落库，并返回新用户 id。</p>
     *
     * @param request 注册请求体（包含用户名、密码、昵称）
     * @return 统一返回结构，data 为新创建的用户 id
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest request) {
        Long userId = userService.register(request);
        return BaseResponse.success(userId);
    }

    /**
     * 用户登录接口。
     *
     * <p>用途：前端提交登录信息，后端校验密码，返回登录 Token。</p>
     *
     * @param request 登录请求体（包含用户名、密码）
     * @return 统一返回结构，data 为登录 Token
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public BaseResponse<String> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request);
        return BaseResponse.success(token);
    }

    /**
     * 获取当前登录用户信息接口。
     *
     * <p>用途：前端获取当前登录用户的个人信息。</p>
     *
     * @param request HTTP 请求对象（用于获取当前登录用户 ID）
     * @return 统一返回结构，data 为用户视图对象
     */
    @GetMapping("/current")
    @ApiOperation("获取当前登录用户信息")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        UserVO userVO = userService.getCurrentUser(userId);
        return BaseResponse.success(userVO);
    }

    /**
     * 修改个人信息接口。
     *
     * <p>用途：前端提交修改后的昵称和头像 URL，后端更新用户信息。</p>
     *
     * @param request    HTTP 请求对象（用于获取当前登录用户 ID）
     * @param updateBody 修改请求体（包含 nickname、avatarUrl）
     * @return 统一返回结构，data 为是否成功
     */
    @PutMapping("/profile")
    @ApiOperation("修改个人信息")
    public BaseResponse<Boolean> updateProfile(HttpServletRequest request,
                                               @RequestBody Map<String, String> updateBody) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        String nickname = updateBody.get("nickname");
        String avatarUrl = updateBody.get("avatarUrl");
        boolean result = userService.updateProfile(userId, nickname, avatarUrl);
        return BaseResponse.success(result);
    }
}
