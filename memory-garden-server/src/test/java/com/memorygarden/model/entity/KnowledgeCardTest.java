package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KnowledgeCard 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class KnowledgeCardTest {

    @Test
    void testFieldAssignment() {
        KnowledgeCard card = new KnowledgeCard();
        card.setId(1L);
        card.setUserId(100L);
        card.setCategoryId(10L);
        card.setFrontContent("What is OOP?");
        card.setBackContent("Object-Oriented Programming");
        card.setNote("Basic concept");
        card.setSourceType(0);
        card.setSourcePackId(null);
        card.setIsDeleted(0);
        card.setCreateTime(new Date());
        card.setUpdateTime(new Date());

        assertEquals(1L, card.getId());
        assertEquals(100L, card.getUserId());
        assertEquals(10L, card.getCategoryId());
        assertEquals("What is OOP?", card.getFrontContent());
        assertEquals("Object-Oriented Programming", card.getBackContent());
        assertEquals("Basic concept", card.getNote());
        assertEquals(0, card.getSourceType());
        assertNull(card.getSourcePackId());
    }

    @Test
    void testSourceTypeValues() {
        KnowledgeCard manual = new KnowledgeCard();
        manual.setSourceType(0);
        assertEquals(0, manual.getSourceType());

        KnowledgeCard imported = new KnowledgeCard();
        imported.setSourceType(1);
        assertEquals(1, imported.getSourceType());
    }

    @Test
    void testSoftDelete() {
        KnowledgeCard card = new KnowledgeCard();
        card.setIsDeleted(0);
        assertEquals(0, card.getIsDeleted());

        card.setIsDeleted(1);
        assertEquals(1, card.getIsDeleted());
    }
}
