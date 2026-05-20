package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.ReviewSubmitRequest;
import com.memorygarden.model.vo.ReviewSummaryVO;
import com.memorygarden.model.vo.ReviewVO;
import com.memorygarden.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 复习控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/review")
@Api(tags = "复习模块")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    /**
     * 获取今日待复习列表接口。
     *
     * <p>用途：前端获取今日到期的复习卡片列表，按日期排序。</p>
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为待复习列表
     */
    @GetMapping("/pending")
    @ApiOperation("获取待复习列表")
    public BaseResponse<List<ReviewVO>> getPending(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<ReviewVO> list = reviewService.getPending(userId);
        return BaseResponse.success(list);
    }

    /**
     * 获取下一棵待复习植物详情接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为复习视图对象
     */
    @GetMapping("/next")
    @ApiOperation("获取下一个待复习")
    public BaseResponse<ReviewVO> getNext(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        ReviewVO vo = reviewService.getNext(userId);
        return BaseResponse.success(vo);
    }

    /**
     * 提交复习自评结果接口。
     *
     * <p>用途：前端提交自评结果，后端更新植物生长阶段、复习轮次、打卡天数等。</p>
     *
     * @param request HTTP 请求对象
     * @param req     提交请求体（包含卡片 ID、自评值）
     * @return 统一返回结构，data 为是否成功
     */
    @PostMapping("/submit")
    @ApiOperation("提交复习自评结果")
    public BaseResponse<Boolean> submit(HttpServletRequest request, @RequestBody ReviewSubmitRequest req) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        boolean result = reviewService.submit(userId, req);
        return BaseResponse.success(result);
    }

    /**
     * 获取今日复习完成总结接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为复习总结
     */
    @GetMapping("/summary")
    @ApiOperation("获取今日复习总结")
    public BaseResponse<ReviewSummaryVO> getSummary(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        ReviewSummaryVO vo = reviewService.getSummary(userId);
        return BaseResponse.success(vo);
    }

    /**
     * 获取待复习数量接口（用于导航栏徽标）。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为待复习数量
     */
    @GetMapping("/pending-count")
    @ApiOperation("获取待复习数量")
    public BaseResponse<Integer> getPendingCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        int count = reviewService.getPendingCount(userId);
        return BaseResponse.success(count);
    }
}
