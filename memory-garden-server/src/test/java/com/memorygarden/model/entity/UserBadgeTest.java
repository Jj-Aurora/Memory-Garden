package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserBadge 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class UserBadgeTest {

    @Test
    void testFieldAssignment() {
        UserBadge userBadge = new UserBadge();
        userBadge.setId(1L);
        userBadge.setUserId(100L);
        userBadge.setBadgeId(5L);
        userBadge.setIsDeleted(0);
        userBadge.setCreateTime(new Date());

        assertEquals(1L, userBadge.getId());
        assertEquals(100L, userBadge.getUserId());
        assertEquals(5L, userBadge.getBadgeId());
    }

    @Test
    void testSoftDelete() {
        UserBadge userBadge = new UserBadge();
        userBadge.setIsDeleted(0);
        assertEquals(0, userBadge.getIsDeleted());

        userBadge.setIsDeleted(1);
        assertEquals(1, userBadge.getIsDeleted());
    }
}
