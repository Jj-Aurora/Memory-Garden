package com.memorygarden.algorithm;

/**
 * 艾宾浩斯遗忘曲线间隔计算器
 *
 * @author jLU
 * @date 2026-05-20
 */
public class EbbinghausCalculator {

    /**
     * 复习间隔表（天）：轮次1~6对应 [1,2,4,7,15,30]
     */
    private static final int[] INTERVAL_TABLE = {1, 2, 4, 7, 15, 30};

    /**
     * 根据复习轮次获取间隔天数
     * <p>轮次1~6查表，轮次≥6固定返回30天，轮次0返回1天</p>
     *
     * @param round 复习轮次（从1开始）
     * @return 间隔天数
     */
    public static int getIntervalDays(int round) {
        if (round <= 0) {
            return 1;
        }
        if (round > INTERVAL_TABLE.length) {
            return 30;
        }
        return INTERVAL_TABLE[round - 1];
    }

    /**
     * 智能重算等效轮次
     * <p>根据逾期天数，计算等效轮次。逾期越久，回退越多。</p>
     * <p>算法：从当前轮次逐级回退，每级扣除该轮次对应的间隔天数，
     * 直到逾期天数不足以再回退一级。最低不低于1。</p>
     *
     * @param currentRound 当前轮次
     * @param overdueDays  逾期天数
     * @return 等效轮次（最低为1）
     */
    public static int calcEffectiveRound(int currentRound, int overdueDays) {
        if (overdueDays <= 0) {
            return currentRound;
        }
        if (currentRound <= 1) {
            return 1;
        }

        int remaining = overdueDays;
        int effectiveRound = currentRound;

        // 从当前轮次逐级回退
        while (effectiveRound > 1 && remaining > 0) {
            int interval = getIntervalDays(effectiveRound);
            if (remaining >= interval) {
                remaining -= interval;
                effectiveRound--;
            } else {
                break;
            }
        }

        return Math.max(1, effectiveRound);
    }

    private EbbinghausCalculator() {
    }
}
