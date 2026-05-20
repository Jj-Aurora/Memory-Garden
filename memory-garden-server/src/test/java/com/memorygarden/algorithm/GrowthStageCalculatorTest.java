package com.memorygarden.algorithm;

import com.memorygarden.model.enums.GrowthStage;
import com.memorygarden.model.enums.SelfEvaluation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GrowthStageCalculator 测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class GrowthStageCalculatorTest {

    @Test
    void testRemembered_Upgrade() {
        // 记住了→升级
        int result = GrowthStageCalculator.calculateNextStage(1, SelfEvaluation.REMEMBERED, false);
        assertEquals(2, result);
    }

    @Test
    void testRemembered_StageFiveNoUpgrade() {
        // Stage5 不再升级
        int result = GrowthStageCalculator.calculateNextStage(5, SelfEvaluation.REMEMBERED, false);
        assertEquals(5, result);
    }

    @Test
    void testVague_Maintain() {
        // 模糊→维持
        int result = GrowthStageCalculator.calculateNextStage(3, SelfEvaluation.VAGUE, false);
        assertEquals(3, result);
    }

    @Test
    void testForgotten_Downgrade() {
        // 忘记了→回退
        int result = GrowthStageCalculator.calculateNextStage(3, SelfEvaluation.FORGOTTEN, false);
        assertEquals(2, result);
    }

    @Test
    void testForgotten_StageOneNoDowngrade() {
        // Stage1 不再回退
        int result = GrowthStageCalculator.calculateNextStage(1, SelfEvaluation.FORGOTTEN, false);
        assertEquals(1, result);
    }

    @Test
    void testWithered_Revive() {
        // 枯萎复活：回到上一阶段（不低于1）
        int result = GrowthStageCalculator.calculateNextStage(3, SelfEvaluation.REMEMBERED, true);
        // 复活后维持当前阶段
        assertEquals(3, result);
    }

    @Test
    void testWithered_ReviveStageOne() {
        // 枯萎且Stage1复活
        int result = GrowthStageCalculator.calculateNextStage(1, SelfEvaluation.REMEMBERED, true);
        assertEquals(1, result);
    }

    @Test
    void testWithered_ForgottenStayWithered() {
        // 枯萎+忘记了→仍然枯萎（不升级）
        int result = GrowthStageCalculator.calculateNextStage(3, SelfEvaluation.FORGOTTEN, true);
        assertEquals(2, result);
    }

    @Test
    void testRemembered_FullProgression() {
        // 完整升级路径
        int stage = 1;
        stage = GrowthStageCalculator.calculateNextStage(stage, SelfEvaluation.REMEMBERED, false);
        assertEquals(2, stage);
        stage = GrowthStageCalculator.calculateNextStage(stage, SelfEvaluation.REMEMBERED, false);
        assertEquals(3, stage);
        stage = GrowthStageCalculator.calculateNextStage(stage, SelfEvaluation.REMEMBERED, false);
        assertEquals(4, stage);
        stage = GrowthStageCalculator.calculateNextStage(stage, SelfEvaluation.REMEMBERED, false);
        assertEquals(5, stage);
        stage = GrowthStageCalculator.calculateNextStage(stage, SelfEvaluation.REMEMBERED, false);
        assertEquals(5, stage);
    }
}
