package com.memorygarden.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BadgeRarity 枚举测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class BadgeRarityTest {

    @Test
    void testRarityValues() {
        assertEquals(0, BadgeRarity.COMMON.getValue());
        assertEquals(1, BadgeRarity.RARE.getValue());
        assertEquals(2, BadgeRarity.EPIC.getValue());
    }

    @Test
    void testRarityCount() {
        assertEquals(3, BadgeRarity.values().length);
    }

    @Test
    void testFromValue() {
        assertEquals(BadgeRarity.COMMON, BadgeRarity.fromValue(0));
        assertEquals(BadgeRarity.RARE, BadgeRarity.fromValue(1));
        assertEquals(BadgeRarity.EPIC, BadgeRarity.fromValue(2));
    }

    @Test
    void testFromValueBoundary() {
        assertNull(BadgeRarity.fromValue(-1));
        assertNull(BadgeRarity.fromValue(3));
    }
}
