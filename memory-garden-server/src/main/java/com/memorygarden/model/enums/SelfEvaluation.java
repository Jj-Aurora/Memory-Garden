package com.memorygarden.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 复习自评枚举
 *
 * @author jLU
 * @date 2026-05-20
 */
@Getter
@AllArgsConstructor
public enum SelfEvaluation {

    /**
     * 记住了
     */
    REMEMBERED(1, "记住了"),

    /**
     * 模糊
     */
    VAGUE(2, "模糊"),

    /**
     * 忘记了
     */
    FORGOTTEN(3, "忘记了");

    /**
     * 自评值
     */
    private final int value;

    /**
     * 自评名称
     */
    private final String name;

    /**
     * 根据数值获取枚举
     *
     * @param value 自评数值
     * @return 对应枚举，不存在则返回 null
     */
    public static SelfEvaluation fromValue(int value) {
        for (SelfEvaluation evaluation : values()) {
            if (evaluation.value == value) {
                return evaluation;
            }
        }
        return null;
    }
}
