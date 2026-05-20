package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Badge 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class BadgeTest {

    @Test
    void testFieldAssignment() {
        Badge badge = new Badge();
        badge.setId(1L);
        badge.setName("初出茅庐");
        badge.setDescription("种植第一棵植物");
        badge.setIcon("plant_first");
        badge.setRarity(0);
        badge.setConditionType("PLANT_FIRST");
        badge.setConditionValue(1);
        badge.setIsDeleted(0);
        badge.setCreateTime(new Date());

        assertEquals(1L, badge.getId());
        assertEquals("初出茅庐", badge.getName());
        assertEquals("种植第一棵植物", badge.getDescription());
        assertEquals("plant_first", badge.getIcon());
        assertEquals(0, badge.getRarity());
        assertEquals("PLANT_FIRST", badge.getConditionType());
        assertEquals(1, badge.getConditionValue());
    }

    @Test
    void testConditionTypes() {
        String[] types = {"PLANT_FIRST", "STREAK_DAYS", "BLOOMING_COUNT",
                "FRUIT_FIRST", "TOTAL_PLANTS", "REVIVE_COUNT", "CATEGORY_COUNT"};
        for (String type : types) {
            Badge badge = new Badge();
            badge.setConditionType(type);
            assertEquals(type, badge.getConditionType());
        }
    }
}
