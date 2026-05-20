package com.memorygarden.algorithm;

/**
 * 枯萎判定计算器
 *
 * @author jLU
 * @date 2026-05-20
 */
public class WitherCalculator {

    /**
     * 枯萎阈值倍数：逾期天数 ≥ 当前间隔 × WITHER_THRESHOLD 则枯萎
     */
    private static final int WITHER_THRESHOLD = 3;

    /**
     * 判断植物是否应该枯萎
     *
     * <p>规则：逾期天数 ≥ 当前复习间隔 × 3 → 枯萎</p>
     *
     * @param overdueDays    逾期天数（实际复习日期 - 计划复习日期）
     * @param currentInterval 当前复习间隔天数
     * @return true=应该枯萎
     */
    public static boolean shouldWither(int overdueDays, int currentInterval) {
        if (currentInterval <= 0) {
            return false;
        }
        return overdueDays >= currentInterval * WITHER_THRESHOLD;
    }

    private WitherCalculator() {
    }
}
