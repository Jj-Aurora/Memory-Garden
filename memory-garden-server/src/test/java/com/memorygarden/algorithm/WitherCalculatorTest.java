package com.memorygarden.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WitherCalculator 测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class WitherCalculatorTest {

    @Test
    void testShouldWither_OverdueThreeTimesThreshold() {
        // 逾期天数 ≥ 当前间隔 × 3 → 枯萎
        // 间隔=4天，逾期=12天 → 枯萎
        assertTrue(WitherCalculator.shouldWither(12, 4));
    }

    @Test
    void testShouldWither_OverdueMoreThanThreeTimes() {
        // 间隔=7天，逾期=30天 → 枯萎
        assertTrue(WitherCalculator.shouldWither(30, 7));
    }

    @Test
    void testShouldNotWither_NotOverdue() {
        // 未逾期→不枯萎
        assertFalse(WitherCalculator.shouldWither(0, 4));
    }

    @Test
    void testShouldNotWither_SlightlyOverdue() {
        // 间隔=4天，逾期=5天 → 不枯萎（5 < 4*3=12）
        assertFalse(WitherCalculator.shouldWither(5, 4));
    }

    @Test
    void testShouldNotWither_ExactlyTwoTimesThreshold() {
        // 间隔=4天，逾期=8天 → 不枯萎（8 < 12）
        assertFalse(WitherCalculator.shouldWither(8, 4));
    }

    @Test
    void testShouldWither_ExactlyThreeTimesThreshold() {
        // 间隔=4天，逾期=12天 → 枯萎（12 == 12）
        assertTrue(WitherCalculator.shouldWither(12, 4));
    }

    @Test
    void testShouldNotWither_NegativeOverdue() {
        // 负逾期天数（提前复习）→不枯萎
        assertFalse(WitherCalculator.shouldWither(-1, 4));
    }

    @Test
    void testShouldNotWither_ZeroInterval() {
        // 间隔为0→不枯萎（避免除零）
        assertFalse(WitherCalculator.shouldWither(10, 0));
    }
}
