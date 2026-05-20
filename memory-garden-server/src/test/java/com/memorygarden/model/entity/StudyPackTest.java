package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudyPack 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class StudyPackTest {

    @Test
    void testFieldAssignment() {
        StudyPack pack = new StudyPack();
        pack.setId(1L);
        pack.setName("英语四级核心词汇");
        pack.setDescription("包含四级高频词汇");
        pack.setCategoryName("英语");
        pack.setCardCount(50);
        pack.setIsDeleted(0);
        pack.setCreateTime(new Date());
        pack.setUpdateTime(new Date());

        assertEquals(1L, pack.getId());
        assertEquals("英语四级核心词汇", pack.getName());
        assertEquals("包含四级高频词汇", pack.getDescription());
        assertEquals("英语", pack.getCategoryName());
        assertEquals(50, pack.getCardCount());
    }
}
