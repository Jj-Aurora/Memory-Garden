package com.memorygarden.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BadgeEvaluator 测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class BadgeEvaluatorTest {

    // ===== PLANT_FIRST =====

    @Test
    void testPlantFirst_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("PLANT_FIRST", 1, 1));
    }

    @Test
    void testPlantFirst_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("PLANT_FIRST", 0, 1));
    }

    // ===== STREAK_DAYS =====

    @Test
    void testStreakDays_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("STREAK_DAYS", 7, 7));
    }

    @Test
    void testStreakDays_Exceeded() {
        assertTrue(BadgeEvaluator.evaluate("STREAK_DAYS", 10, 7));
    }

    @Test
    void testStreakDays_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("STREAK_DAYS", 5, 7));
    }

    // ===== BLOOMING_COUNT =====

    @Test
    void testBloomingCount_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("BLOOMING_COUNT", 5, 5));
    }

    @Test
    void testBloomingCount_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("BLOOMING_COUNT", 3, 5));
    }

    // ===== FRUIT_FIRST =====

    @Test
    void testFruitFirst_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("FRUIT_FIRST", 1, 1));
    }

    @Test
    void testFruitFirst_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("FRUIT_FIRST", 0, 1));
    }

    // ===== TOTAL_PLANTS =====

    @Test
    void testTotalPlants_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("TOTAL_PLANTS", 50, 50));
    }

    @Test
    void testTotalPlants_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("TOTAL_PLANTS", 30, 50));
    }

    // ===== REVIVE_COUNT =====

    @Test
    void testReviveCount_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("REVIVE_COUNT", 3, 3));
    }

    @Test
    void testReviveCount_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("REVIVE_COUNT", 1, 3));
    }

    // ===== CATEGORY_COUNT =====

    @Test
    void testCategoryCount_Achieved() {
        assertTrue(BadgeEvaluator.evaluate("CATEGORY_COUNT", 5, 5));
    }

    @Test
    void testCategoryCount_NotAchieved() {
        assertFalse(BadgeEvaluator.evaluate("CATEGORY_COUNT", 2, 5));
    }

    // ===== 边界值 =====

    @Test
    void testUnknownConditionType() {
        assertFalse(BadgeEvaluator.evaluate("UNKNOWN_TYPE", 10, 5));
    }
}
