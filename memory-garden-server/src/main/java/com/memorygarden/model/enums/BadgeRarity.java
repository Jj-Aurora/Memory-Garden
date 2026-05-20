package com.memorygarden.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 徽章稀有度枚举
 *
 * @author jLU
 * @date 2026-05-20
 */
@Getter
@AllArgsConstructor
public enum BadgeRarity {

    /**
     * 普通
     */
    COMMON(0, "普通"),

    /**
     * 稀有
     */
    RARE(1, "稀有"),

    /**
     * 史诗
     */
    EPIC(2, "史诗");

    /**
     * 稀有度值
     */
    private final int value;

    /**
     * 稀有度名称
     */
    private final String name;

    /**
     * 根据数值获取枚举
     *
     * @param value 稀有度数值
     * @return 对应枚举，不存在则返回 null
     */
    public static BadgeRarity fromValue(int value) {
        for (BadgeRarity rarity : values()) {
            if (rarity.value == value) {
                return rarity;
            }
        }
        return null;
    }
}
