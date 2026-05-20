package com.memorygarden.algorithm;

/**
 * 徽章达成判定器
 *
 * @author jLU
 * @date 2026-05-20
 */
public class BadgeEvaluator {

    /**
     * 判定指定条件类型是否达成
     *
     * @param conditionType   条件类型
     * @param currentValue    当前值
     * @param conditionValue  达成阈值
     * @return true=达成
     */
    public static boolean evaluate(String conditionType, int currentValue, int conditionValue) {
        if (conditionType == null) {
            return false;
        }
        switch (conditionType) {
            case "PLANT_FIRST":
                return currentValue >= conditionValue;
            case "STREAK_DAYS":
                return currentValue >= conditionValue;
            case "BLOOMING_COUNT":
                return currentValue >= conditionValue;
            case "FRUIT_FIRST":
                return currentValue >= conditionValue;
            case "TOTAL_PLANTS":
                return currentValue >= conditionValue;
            case "REVIVE_COUNT":
                return currentValue >= conditionValue;
            case "CATEGORY_COUNT":
                return currentValue >= conditionValue;
            default:
                return false;
        }
    }

    private BadgeEvaluator() {
    }
}
