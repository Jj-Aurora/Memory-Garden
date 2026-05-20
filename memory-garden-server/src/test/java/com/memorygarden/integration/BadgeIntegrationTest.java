package com.memorygarden.integration;

import com.memorygarden.mapper.*;
import com.memorygarden.model.entity.*;
import com.memorygarden.service.BadgeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Badge 集成测试（复习触发→徽章判定→授予）
 *
 * @author jLU
 * @date 2026-05-20
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("I-17: Badge 集成测试")
class BadgeIntegrationTest {

    @Resource
    private BadgeService badgeService;

    @Resource
    private BadgeMapper badgeMapper;

    @Resource
    private UserBadgeMapper userBadgeMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PlantMapper plantMapper;

    @Resource
    private KnowledgeCardMapper cardMapper;

    private Long testUserId;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("badge_user_" + System.currentTimeMillis());
        user.setPassword("fake_password");
        user.setNickname("徽章测试用户");
        user.setIsDeleted(0);
        user.setStatus(1);
        userMapper.insert(user);
        testUserId = user.getId();
    }

    @Test
    @DisplayName("获取所有徽章-初始数据8枚")
    void testGetAllBadges() {
        List<Badge> badges = badgeMapper.selectAll();
        assertNotNull(badges);
        assertEquals(8, badges.size());
    }

    @Test
    @DisplayName("获取已获得徽章-初始为空")
    void testGetMyBadges_Empty() {
        List<UserBadge> myBadges = userBadgeMapper.selectByUserId(testUserId);
        assertNotNull(myBadges);
        assertTrue(myBadges.isEmpty());
    }

    @Test
    @DisplayName("授予徽章-判定已获得")
    void testAwardBadge() {
        List<Badge> badges = badgeMapper.selectAll();
        Badge firstBadge = badges.get(0);
        assertNotNull(firstBadge);

        UserBadge userBadge = new UserBadge();
        userBadge.setUserId(testUserId);
        userBadge.setBadgeId(firstBadge.getId());
        userBadge.setIsDeleted(0);
        userBadgeMapper.insert(userBadge);

        boolean exists = userBadgeMapper.existsByUserIdAndBadgeId(testUserId, firstBadge.getId());
        assertTrue(exists);

        List<UserBadge> myBadges = userBadgeMapper.selectByUserId(testUserId);
        assertEquals(1, myBadges.size());
    }

    @Test
    @DisplayName("徽章判定-达成条件授予")
    void testEvaluateAndAward() {
        // 先创建卡片，再创建植物（保证 card_id 唯一约束）
        KnowledgeCard card = new KnowledgeCard();
        card.setUserId(testUserId);
        card.setFrontContent("徽章测试问题");
        card.setBackContent("徽章测试答案");
        card.setSourceType(0);
        card.setIsDeleted(0);
        cardMapper.insert(card);

        Plant plant = new Plant();
        plant.setUserId(testUserId);
        plant.setCardId(card.getId());
        plant.setGrowthStage(1);
        plant.setIsWithered(0);
        plant.setStageSuccessCount(0);
        plant.setTotalReviewCount(0);
        plant.setReviewRound(0);
        plant.setNextReviewDate(LocalDate.now());
        plant.setIsDeleted(0);
        plantMapper.insert(plant);

        // 调用徽章评估
        badgeService.evaluateAndAward(testUserId);

        // 验证"初入花园"徽章已授予
        List<Badge> badges = badgeMapper.selectByConditionType("PLANT_FIRST");
        assertFalse(badges.isEmpty());
        Badge plantFirstBadge = badges.get(0);
        boolean exists = userBadgeMapper.existsByUserIdAndBadgeId(testUserId, plantFirstBadge.getId());
        assertTrue(exists);
    }
}
