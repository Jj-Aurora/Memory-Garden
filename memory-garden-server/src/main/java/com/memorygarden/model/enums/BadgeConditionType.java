package com.memorygarden.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 徽章达成条件类型枚举
 *
 * @author jLU
 * @date 2026-05-20
 */
@Getter
@AllArgsConstructor
public enum BadgeConditionType {

    /**
     * 首次种植
     */
    PLANT_FIRST("PLANT_FIRST", "首次种植"),

    /**
     * 连续打卡天数
     */
    STREAK_DAYS("STREAK_DAYS", "连续打卡天数"),

    /**
     * 开花植物数量
     */
    BLOOMING_COUNT("BLOOMING_COUNT", "开花植物数量"),

    /**
     * 首次结果
     */
    FRUIT_FIRST("FRUIT_FIRST", "首次结果"),

    /**
     * 总植物数量
     */
    TOTAL_PLANTS("TOTAL_PLANTS", "总植物数量"),

    /**
     * 复活植物次数
     */
    REVIVE_COUNT("REVIVE_COUNT", "复活植物次数"),

    /**
     * 分类数量
     */
    CATEGORY_COUNT("CATEGORY_COUNT", "分类数量");

    /**
     * 条件类型标识
     */
    private final String code;

    /**
     * 条件类型名称
     */
    private final String name;

    /**
     * 根据名称获取枚举
     *
     * @param name 枚举名称
     * @return 对应枚举，不存在则返回 null
     */
    public static BadgeConditionType fromName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
