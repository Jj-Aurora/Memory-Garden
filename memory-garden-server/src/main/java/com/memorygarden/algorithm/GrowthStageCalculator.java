package com.memorygarden.algorithm;

import com.memorygarden.model.enums.SelfEvaluation;

/**
 * 植物生长阶段推进计算器
 *
 * @author jLU
 * @date 2026-05-20
 */
public class GrowthStageCalculator {

    /**
     * 计算复习后的生长阶段
     *
     * <p>规则：</p>
     * <ul>
     *   <li>枯萎状态 + 记住了 → 复活（维持当前阶段，解除枯萎）</li>
     *   <li>枯萎状态 + 模糊/忘记了 → 仍然枯萎，阶段回退</li>
     *   <li>记住了 → 升一级（最高5）</li>
     *   <li>模糊 → 维持当前阶段</li>
     *   <li>忘记了 → 降一级（最低1）</li>
     * </ul>
     *
     * @param currentStage 当前生长阶段（1~5）
     * @param evaluation   自评结果
     * @param isWithered   是否处于枯萎状态
     * @return 新的生长阶段
     */
    public static int calculateNextStage(int currentStage, SelfEvaluation evaluation, boolean isWithered) {
        if (isWithered) {
            if (evaluation == SelfEvaluation.REMEMBERED) {
                // 枯萎复活：维持当前阶段
                return currentStage;
            }
            // 枯萎+模糊/忘记：仍然枯萎，阶段回退
            if (evaluation == SelfEvaluation.FORGOTTEN) {
                return Math.max(1, currentStage - 1);
            }
            // 枯萎+模糊：维持当前阶段（仍枯萎）
            return currentStage;
        }

        switch (evaluation) {
            case REMEMBERED:
                // 记住了→升级，最高5
                return Math.min(5, currentStage + 1);
            case VAGUE:
                // 模糊→维持
                return currentStage;
            case FORGOTTEN:
                // 忘记了→回退，最低1
                return Math.max(1, currentStage - 1);
            default:
                return currentStage;
        }
    }

    private GrowthStageCalculator() {
    }
}
