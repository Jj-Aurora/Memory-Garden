package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Category 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class CategoryTest {

    @Test
    void testFieldAssignment() {
        Category category = new Category();
        category.setId(1L);
        category.setUserId(100L);
        category.setName("英语");
        category.setIcon("english");
        category.setSortOrder(1);
        category.setIsDeleted(0);
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());

        assertEquals(1L, category.getId());
        assertEquals(100L, category.getUserId());
        assertEquals("英语", category.getName());
        assertEquals("english", category.getIcon());
        assertEquals(1, category.getSortOrder());
    }

    @Test
    void testSoftDelete() {
        Category category = new Category();
        category.setIsDeleted(0);
        assertEquals(0, category.getIsDeleted());

        category.setIsDeleted(1);
        assertEquals(1, category.getIsDeleted());
    }
}
