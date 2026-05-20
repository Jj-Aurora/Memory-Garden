package com.memorygarden.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EbbinghausCalculator 测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class EbbinghausCalculatorTest {

    // ===== D-27: 基础间隔计算测试 =====

    @Test
    void testIntervalRounds() {
        assertEquals(1, EbbinghausCalculator.getIntervalDays(0));
        assertEquals(1, EbbinghausCalculator.getIntervalDays(1));
        assertEquals(2, EbbinghausCalculator.getIntervalDays(2));
        assertEquals(4, EbbinghausCalculator.getIntervalDays(3));
        assertEquals(7, EbbinghausCalculator.getIntervalDays(4));
        assertEquals(15, EbbinghausCalculator.getIntervalDays(5));
        assertEquals(30, EbbinghausCalculator.getIntervalDays(6));
    }

    @Test
    void testRoundAboveFiveReturnsFixedThirty() {
        assertEquals(30, EbbinghausCalculator.getIntervalDays(7));
        assertEquals(30, EbbinghausCalculator.getIntervalDays(10));
        assertEquals(30, EbbinghausCalculator.getIntervalDays(100));
    }

    @Test
    void testRoundZeroReturnsOne() {
        assertEquals(1, EbbinghausCalculator.getIntervalDays(0));
    }

    // ===== D-29: 智能重算测试 =====

    @Test
    void testCalcEffectiveRound_NoMiss() {
        // 未逾期：等效轮次不变
        assertEquals(3, EbbinghausCalculator.calcEffectiveRound(3, 0));
    }

    @Test
    void testCalcEffectiveRound_MissOneDay() {
        // 错过1天：轻微回退
        int effective = EbbinghausCalculator.calcEffectiveRound(3, 1);
        assertTrue(effective <= 3);
        assertTrue(effective >= 1);
    }

    @Test
    void testCalcEffectiveRound_MissThreeDays() {
        // 错过3天：Round3间隔=4天，3<4不回退；Round2间隔=2天，3>=2回退到1
        // 但从Round3开始，3<4不回退，所以仍为3
        int effective = EbbinghausCalculator.calcEffectiveRound(3, 3);
        assertTrue(effective <= 3);
        // 错过5天：Round3间隔=4天，5>=4回退到2；Round2间隔=2天，1<2停止
        int effective2 = EbbinghausCalculator.calcEffectiveRound(3, 5);
        assertEquals(2, effective2);
    }

    @Test
    void testCalcEffectiveRound_MissTenDays() {
        // 错过10天：Round5间隔=15天，10<15不回退 → 5
        // 错过20天：Round5间隔=15天，20>=15回退到4；Round4间隔=7天，5<7停止 → 4
        int effective = EbbinghausCalculator.calcEffectiveRound(5, 10);
        assertEquals(5, effective);
        int effective2 = EbbinghausCalculator.calcEffectiveRound(5, 20);
        assertEquals(4, effective2);
        // 错过50天：15+7+4+2=28，50>28，回退到1
        int effective3 = EbbinghausCalculator.calcEffectiveRound(5, 50);
        assertEquals(1, effective3);
    }

    @Test
    void testCalcEffectiveRound_MissSixtyDays() {
        // 错过60天：回到第1轮
        int effective = EbbinghausCalculator.calcEffectiveRound(5, 60);
        assertEquals(1, effective);
    }

    @Test
    void testCalcEffectiveRound_RoundOneNoFallback() {
        // 第1轮不会再回退
        int effective = EbbinghausCalculator.calcEffectiveRound(1, 10);
        assertEquals(1, effective);
    }
}
