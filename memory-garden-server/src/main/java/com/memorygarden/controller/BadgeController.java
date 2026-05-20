package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.vo.BadgeVO;
import com.memorygarden.service.BadgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 徽章控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/badge")
@Api(tags = "徽章模块")
public class BadgeController {

    @Resource
    private BadgeService badgeService;

    /**
     * 获取所有徽章列表接口（含获得状态）。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为徽章列表
     */
    @GetMapping("/list")
    @ApiOperation("获取所有徽章列表")
    public BaseResponse<List<BadgeVO>> getAllBadges(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<BadgeVO> list = badgeService.getAllBadges(userId);
        return BaseResponse.success(list);
    }

    /**
     * 获取已获得的徽章列表接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为已获得徽章列表
     */
    @GetMapping("/my")
    @ApiOperation("获取已获得徽章")
    public BaseResponse<List<BadgeVO>> getMyBadges(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<BadgeVO> list = badgeService.getMyBadges(userId);
        return BaseResponse.success(list);
    }
}
