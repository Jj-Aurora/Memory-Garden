package com.memorygarden.service;

import com.memorygarden.model.vo.StatsVO;

import java.util.Map;

/**
 * 学习统计服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface StatsService {

    /**
     * 获取今日学习数据
     *
     * @param userId 用户 ID
     * @return 统计视图对象
     */
    StatsVO getToday(Long userId);

    /**
     * 获取趋势数据
     *
     * @param userId 用户 ID
     * @param days   天数
     * @return 日期->数量映射
     */
    Map<String, Integer> getTrend(Long userId, int days);

    /**
     * 获取连续打卡数据
     *
     * @param userId 用户 ID
     * @return 连续天数
     */
    int getStreak(Long userId);

    /**
     * 获取各阶段植物数量分布
     *
     * @param userId 用户 ID
     * @return 阶段->数量映射
     */
    Map<Integer, Integer> getStageDistribution(Long userId);
}
