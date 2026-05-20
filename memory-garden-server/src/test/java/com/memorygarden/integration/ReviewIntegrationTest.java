package com.memorygarden.integration;

import com.memorygarden.mapper.*;
import com.memorygarden.model.dto.ReviewSubmitRequest;
import com.memorygarden.model.entity.*;
import com.memorygarden.model.enums.SelfEvaluation;
import com.memorygarden.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Review 全流程集成测试
 * （创建卡片→复习提交→植物生长→间隔计算→枯萎→复活）
 *
 * @author jLU
 * @date 2026-05-20
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("I-16: Review 全流程集成测试")
class ReviewIntegrationTest {

    @Resource
    private ReviewService reviewService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private KnowledgeCardMapper cardMapper;

    @Resource
    private PlantMapper plantMapper;

    @Resource
    private CategoryMapper categoryMapper;

    private Long testUserId;
    private Long testCardId;
    private Long testPlantId;

    @BeforeEach
    void setUp() {
        // 创建测试用户
        User user = new User();
        user.setUsername("review_user_" + System.currentTimeMillis());
        user.setPassword("fake_password");
        user.setNickname("复习测试用户");
        user.setIsDeleted(0);
        user.setStatus(1);
        userMapper.insert(user);
        testUserId = user.getId();

        // 创建分类
        Category category = new Category();
        category.setUserId(testUserId);
        category.setName("复习测试分类");
        category.setSortOrder(0);
        category.setIsDeleted(0);
        categoryMapper.insert(category);

        // 创建卡片
        KnowledgeCard card = new KnowledgeCard();
        card.setUserId(testUserId);
        card.setCategoryId(category.getId());
        card.setFrontContent("复习测试问题");
        card.setBackContent("复习测试答案");
        card.setSourceType(0);
        card.setIsDeleted(0);
        cardMapper.insert(card);
        testCardId = card.getId();

        // 创建植物
        Plant plant = new Plant();
        plant.setUserId(testUserId);
        plant.setCardId(testCardId);
        plant.setGrowthStage(1);
        plant.setIsWithered(0);
        plant.setStageSuccessCount(0);
        plant.setTotalReviewCount(0);
        plant.setReviewRound(0);
        plant.setNextReviewDate(LocalDate.now());
        plant.setIsDeleted(0);
        plantMapper.insert(plant);
        testPlantId = plant.getId();
    }

    @Test
    @DisplayName("复习-记住了→植物升级+轮次推进")
    void testReviewRemembered() {
        ReviewSubmitRequest request = new ReviewSubmitRequest();
        request.setCardId(testCardId);
        request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

        reviewService.submit(testUserId, request);

        Plant updatedPlant = plantMapper.selectById(testPlantId);
        assertNotNull(updatedPlant);
        assertEquals(2, updatedPlant.getGrowthStage().intValue());
        assertEquals(1, updatedPlant.getReviewRound().intValue());
        assertEquals(1, updatedPlant.getTotalReviewCount().intValue());
    }

    @Test
    @DisplayName("复习-模糊→植物维持+轮次不变")
    void testReviewVague() {
        ReviewSubmitRequest request = new ReviewSubmitRequest();
        request.setCardId(testCardId);
        request.setSelfEvaluation(SelfEvaluation.VAGUE.getValue());

        reviewService.submit(testUserId, request);

        Plant updatedPlant = plantMapper.selectById(testPlantId);
        assertNotNull(updatedPlant);
        assertEquals(1, updatedPlant.getGrowthStage().intValue());
        assertEquals(0, updatedPlant.getReviewRound().intValue());
    }

    @Test
    @DisplayName("复习-忘记了→植物回退")
    void testReviewForgotten() {
        // 先升级到 stage 2
        Plant plant = plantMapper.selectById(testPlantId);
        plant.setGrowthStage(2);
        plant.setReviewRound(1);
        plantMapper.updateById(plant);

        ReviewSubmitRequest request = new ReviewSubmitRequest();
        request.setCardId(testCardId);
        request.setSelfEvaluation(SelfEvaluation.FORGOTTEN.getValue());

        reviewService.submit(testUserId, request);

        Plant updatedPlant = plantMapper.selectById(testPlantId);
        assertNotNull(updatedPlant);
        assertEquals(1, updatedPlant.getGrowthStage().intValue());
    }

    @Test
    @DisplayName("复习-枯萎复活")
    void testReviewReviveWithered() {
        // 设置植物为枯萎状态
        Plant plant = plantMapper.selectById(testPlantId);
        plant.setWithered(true);
        plantMapper.updateById(plant);

        ReviewSubmitRequest request = new ReviewSubmitRequest();
        request.setCardId(testCardId);
        request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

        reviewService.submit(testUserId, request);

        Plant updatedPlant = plantMapper.selectById(testPlantId);
        assertNotNull(updatedPlant);
        assertEquals(0, updatedPlant.getIsWithered().intValue());
    }
}
