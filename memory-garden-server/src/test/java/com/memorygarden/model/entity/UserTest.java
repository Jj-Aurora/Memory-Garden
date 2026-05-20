package com.memorygarden.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User 实体测试
 *
 * @author jLU
 * @date 2026-05-20
 */
class UserTest {

    @Test
    void testFieldAssignment() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encoded_password");
        user.setNickname("测试用户");
        user.setAvatarUrl("http://example.com/avatar.png");
        user.setCurrentStreak(5);
        user.setMaxStreak(10);
        user.setLastCheckIn(LocalDate.of(2026, 5, 20));
        user.setStatus(1);
        user.setIsDeleted(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("encoded_password", user.getPassword());
        assertEquals("测试用户", user.getNickname());
        assertEquals("http://example.com/avatar.png", user.getAvatarUrl());
        assertEquals(5, user.getCurrentStreak());
        assertEquals(10, user.getMaxStreak());
        assertEquals(LocalDate.of(2026, 5, 20), user.getLastCheckIn());
        assertEquals(1, user.getStatus());
        assertEquals(0, user.getIsDeleted());
    }

    @Test
    void testSoftDelete() {
        User user = new User();
        user.setIsDeleted(0);
        assertEquals(0, user.getIsDeleted());

        user.setIsDeleted(1);
        assertEquals(1, user.getIsDeleted());
    }

    @Test
    void testCheckInStreakUpdate() {
        User user = new User();
        user.setCurrentStreak(5);
        user.setMaxStreak(5);

        user.setCurrentStreak(6);
        user.setMaxStreak(Math.max(user.getMaxStreak(), user.getCurrentStreak()));

        assertEquals(6, user.getCurrentStreak());
        assertEquals(6, user.getMaxStreak());
    }

    @Test
    void testDefaultValues() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertEquals(0, user.getCurrentStreak());
        assertEquals(0, user.getMaxStreak());
    }
}
