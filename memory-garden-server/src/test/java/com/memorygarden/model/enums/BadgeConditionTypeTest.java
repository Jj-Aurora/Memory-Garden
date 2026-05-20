package com.memorygarden.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BadgeConditionType 枚举测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class BadgeConditionTypeTest {

    @Test
    void testAllConditionTypes() {
        assertNotNull(BadgeConditionType.PLANT_FIRST);
        assertNotNull(BadgeConditionType.STREAK_DAYS);
        assertNotNull(BadgeConditionType.BLOOMING_COUNT);
        assertNotNull(BadgeConditionType.FRUIT_FIRST);
        assertNotNull(BadgeConditionType.TOTAL_PLANTS);
        assertNotNull(BadgeConditionType.REVIVE_COUNT);
        assertNotNull(BadgeConditionType.CATEGORY_COUNT);
    }

    @Test
    void testConditionTypeCount() {
        assertEquals(7, BadgeConditionType.values().length);
    }

    @Test
    void testFromName() {
        for (BadgeConditionType type : BadgeConditionType.values()) {
            assertEquals(type, BadgeConditionType.fromName(type.name()));
        }
    }

    @Test
    void testFromNameInvalid() {
        assertNull(BadgeConditionType.fromName("INVALID_TYPE"));
    }
}
