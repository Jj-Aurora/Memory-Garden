package com.memorygarden.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GrowthStage 枚举测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class GrowthStageTest {

    @Test
    void testStageValues() {
        assertEquals(1, GrowthStage.SEED.getValue());
        assertEquals(2, GrowthStage.SPROUT.getValue());
        assertEquals(3, GrowthStage.GROWING.getValue());
        assertEquals(4, GrowthStage.BLOOMING.getValue());
        assertEquals(5, GrowthStage.FRUITING.getValue());
    }

    @Test
    void testStageCount() {
        assertEquals(5, GrowthStage.values().length);
    }

    @Test
    void testFromValue() {
        assertEquals(GrowthStage.SEED, GrowthStage.fromValue(1));
        assertEquals(GrowthStage.SPROUT, GrowthStage.fromValue(2));
        assertEquals(GrowthStage.GROWING, GrowthStage.fromValue(3));
        assertEquals(GrowthStage.BLOOMING, GrowthStage.fromValue(4));
        assertEquals(GrowthStage.FRUITING, GrowthStage.fromValue(5));
    }

    @Test
    void testFromValueBoundary() {
        assertNull(GrowthStage.fromValue(0));
        assertNull(GrowthStage.fromValue(6));
        assertNull(GrowthStage.fromValue(-1));
    }

    @Test
    void testStageNameMapping() {
        assertNotNull(GrowthStage.SEED.getName());
        assertNotNull(GrowthStage.SPROUT.getName());
        assertNotNull(GrowthStage.GROWING.getName());
        assertNotNull(GrowthStage.BLOOMING.getName());
        assertNotNull(GrowthStage.FRUITING.getName());
    }
}
