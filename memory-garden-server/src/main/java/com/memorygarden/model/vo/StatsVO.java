package com.memorygarden.model.vo;

import lombok.Data;

import java.util.Map;

/**
 * 学习统计视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class StatsVO {

    /**
     * 今日新增卡片数
     */
    private Integer todayNewCards;

    /**
     * 今日复习次数
     */
    private Integer todayReviewCount;

    /**
     * 今日生长回退次数
     */
    private Integer todayDegradedCount;

    /**
     * 当前连续打卡天数
     */
    private Integer currentStreak;

    /**
     * 历史最长连续天数
     */
    private Integer maxStreak;

    /**
     * 各阶段植物数量分布
     */
    private Map<Integer, Integer> stageDistribution;

    /**
     * 总卡片数
     */
    private Integer totalCardCount;

    /**
     * 总植物数
     */
    private Integer totalPlantCount;

    /**
     * 趋势数据（日期 -> 数量）
     */
    private Map<String, Integer> trendData;
}
