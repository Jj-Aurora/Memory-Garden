package com.memorygarden.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 植物生长阶段枚举
 *
 * @author jLU
 * @date 2026-05-20
 */
@Getter
@AllArgsConstructor
public enum GrowthStage {

    /**
     * 种子
     */
    SEED(1, "种子"),

    /**
     * 发芽
     */
    SPROUT(2, "发芽"),

    /**
     * 成长
     */
    GROWING(3, "成长"),

    /**
     * 开花
     */
    BLOOMING(4, "开花"),

    /**
     * 结果
     */
    FRUITING(5, "结果");

    /**
     * 阶段值
     */
    private final int value;

    /**
     * 阶段名称
     */
    private final String name;

    /**
     * 根据数值获取枚举
     *
     * @param value 阶段数值
     * @return 对应枚举，不存在则返回 null
     */
    public static GrowthStage fromValue(int value) {
        for (GrowthStage stage : values()) {
            if (stage.value == value) {
                return stage;
            }
        }
        return null;
    }
}
