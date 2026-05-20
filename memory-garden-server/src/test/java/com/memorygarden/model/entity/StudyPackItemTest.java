package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudyPackItem 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class StudyPackItemTest {

    @Test
    void testFieldAssignment() {
        StudyPackItem item = new StudyPackItem();
        item.setId(1L);
        item.setPackId(10L);
        item.setFrontContent("abandon");
        item.setBackContent("放弃，抛弃");
        item.setNote("动词");
        item.setSortOrder(1);
        item.setIsDeleted(0);
        item.setCreateTime(new Date());

        assertEquals(1L, item.getId());
        assertEquals(10L, item.getPackId());
        assertEquals("abandon", item.getFrontContent());
        assertEquals("放弃，抛弃", item.getBackContent());
        assertEquals("动词", item.getNote());
        assertEquals(1, item.getSortOrder());
    }
}
