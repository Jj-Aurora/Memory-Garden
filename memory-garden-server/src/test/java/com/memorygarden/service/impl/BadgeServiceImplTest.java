package com.memorygarden.service.impl;

import com.memorygarden.mapper.BadgeMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.UserBadgeMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.entity.Badge;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.entity.UserBadge;
import com.memorygarden.model.vo.BadgeVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * BadgeServiceImpl 单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class BadgeServiceImplTest {

    @Mock
    private BadgeMapper badgeMapper;

    @Mock
    private UserBadgeMapper userBadgeMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PlantMapper plantMapper;

    @InjectMocks
    private BadgeServiceImpl badgeService;

    private Badge badge1;
    private Badge badge2;
    private UserBadge userBadge1;
    private User user;

    @BeforeEach
    void setUp() {
        badge1 = new Badge();
        badge1.setId(1L);
        badge1.setName("初来乍到");
        badge1.setDescription("创建第一棵植物");
        badge1.setIcon("seed");
        badge1.setRarity(0);
        badge1.setConditionType("PLANT_FIRST");
        badge1.setConditionValue(1);

        badge2 = new Badge();
        badge2.setId(2L);
        badge2.setName("持之以恒");
        badge2.setDescription("连续打卡7天");
        badge2.setIcon("streak");
        badge2.setRarity(1);
        badge2.setConditionType("STREAK_DAYS");
        badge2.setConditionValue(7);

        userBadge1 = new UserBadge();
        userBadge1.setId(1L);
        userBadge1.setUserId(100L);
        userBadge1.setBadgeId(1L);
        userBadge1.setCreateTime(new Date());

        user = new User();
        user.setId(100L);
        user.setCurrentStreak(7);
        user.setMaxStreak(7);
    }

    // ========== A-48: getAllBadges / getMyBadges 测试 ==========

    @Test
    @DisplayName("获取所有徽章-含获得状态")
    void testGetAllBadges() {
        when(badgeMapper.selectAll()).thenReturn(Arrays.asList(badge1, badge2));
        when(userBadgeMapper.selectByUserId(100L)).thenReturn(Collections.singletonList(userBadge1));

        List<BadgeVO> badges = badgeService.getAllBadges(100L);

        assertEquals(2, badges.size());
        assertTrue(badges.get(0).getEarned());
        assertFalse(badges.get(1).getEarned());
    }

    @Test
    @DisplayName("获取已获得徽章")
    void testGetMyBadges() {
        when(badgeMapper.selectAll()).thenReturn(Arrays.asList(badge1, badge2));
        when(userBadgeMapper.selectByUserId(100L)).thenReturn(Collections.singletonList(userBadge1));

        List<BadgeVO> myBadges = badgeService.getMyBadges(100L);

        assertEquals(1, myBadges.size());
        assertEquals("初来乍到", myBadges.get(0).getName());
    }

    // ========== A-50: evaluateAndAward 测试 ==========

    @Test
    @DisplayName("达成条件→授予徽章")
    void testEvaluateAndAward_NewBadge() {
        Plant plant = new Plant();
        plant.setId(1L);
        plant.setGrowthStage(1);
        plant.setIsDeleted(0);

        when(badgeMapper.selectAll()).thenReturn(Collections.singletonList(badge1));
        when(userMapper.selectById(100L)).thenReturn(user);
        when(plantMapper.selectByUserId(100L)).thenReturn(Collections.singletonList(plant));
        when(userBadgeMapper.existsByUserIdAndBadgeId(100L, 1L)).thenReturn(false);
        when(userBadgeMapper.insert(any(UserBadge.class))).thenReturn(1);

        badgeService.evaluateAndAward(100L);

        verify(userBadgeMapper).insert(any(UserBadge.class));
    }

    @Test
    @DisplayName("已获得→不重复授予")
    void testEvaluateAndAward_AlreadyEarned() {
        when(badgeMapper.selectAll()).thenReturn(Collections.singletonList(badge1));
        when(userMapper.selectById(100L)).thenReturn(user);
        when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());
        when(userBadgeMapper.existsByUserIdAndBadgeId(100L, 1L)).thenReturn(true);

        badgeService.evaluateAndAward(100L);

        verify(userBadgeMapper, never()).insert(any());
    }

    @Test
    @DisplayName("未达成条件→不授予")
    void testEvaluateAndAward_NotAchieved() {
        user.setCurrentStreak(3);

        when(badgeMapper.selectAll()).thenReturn(Collections.singletonList(badge2));
        when(userMapper.selectById(100L)).thenReturn(user);
        when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());
        when(userBadgeMapper.existsByUserIdAndBadgeId(100L, 2L)).thenReturn(false);

        badgeService.evaluateAndAward(100L);

        verify(userBadgeMapper, never()).insert(any());
    }
}
