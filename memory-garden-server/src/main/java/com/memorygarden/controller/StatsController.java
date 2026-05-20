package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.vo.StatsVO;
import com.memorygarden.service.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 学习统计控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/stats")
@Api(tags = "学习统计模块")
public class StatsController {

    @Resource
    private StatsService statsService;

    /**
     * 获取今日学习数据接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为统计视图对象
     */
    @GetMapping("/today")
    @ApiOperation("获取今日学习数据")
    public BaseResponse<StatsVO> getToday(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        StatsVO vo = statsService.getToday(userId);
        return BaseResponse.success(vo);
    }

    /**
     * 获取趋势数据接口。
     *
     * @param days    天数（默认 7）
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为日期->数量映射
     */
    @GetMapping("/trend")
    @ApiOperation("获取趋势数据")
    public BaseResponse<Map<String, Integer>> getTrend(@RequestParam(defaultValue = "7") int days,
                                                        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        Map<String, Integer> trend = statsService.getTrend(userId, days);
        return BaseResponse.success(trend);
    }

    /**
     * 获取连续打卡数据接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为连续天数
     */
    @GetMapping("/streak")
    @ApiOperation("获取连续打卡天数")
    public BaseResponse<Integer> getStreak(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        int streak = statsService.getStreak(userId);
        return BaseResponse.success(streak);
    }

    /**
     * 获取各阶段植物数量分布接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为阶段->数量映射
     */
    @GetMapping("/stage-distribution")
    @ApiOperation("获取各阶段植物分布")
    public BaseResponse<Map<Integer, Integer>> getStageDistribution(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        Map<Integer, Integer> distribution = statsService.getStageDistribution(userId);
        return BaseResponse.success(distribution);
    }
}
