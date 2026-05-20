package com.memorygarden.integration;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.dto.CardCreateRequest;
import com.memorygarden.model.entity.Category;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.User;
import com.memorygarden.service.KnowledgeCardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * KnowledgeCard + Plant 集成测试
 * （创建卡片→自动创建植物→删除卡片→级联软删除）
 *
 * @author jLU
 * @date 2026-05-20
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("I-15: KnowledgeCard + Plant 集成测试")
class CardPlantIntegrationTest {

    @Resource
    private KnowledgeCardService cardService;

    @Resource
    private KnowledgeCardMapper cardMapper;

    @Resource
    private PlantMapper plantMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private UserMapper userMapper;

    @Test
    @DisplayName("创建卡片→自动创建植物→验证关联")
    void testCreateCardAutoCreatePlant() {
        Long userId = insertTestUser("create_test");
        Long categoryId = insertTestCategory(userId);

        CardCreateRequest request = new CardCreateRequest();
        request.setFrontContent("测试问题");
        request.setBackContent("测试答案");
        request.setCategoryId(categoryId);

        Long cardId = cardService.create(userId, request);
        assertNotNull(cardId);

        KnowledgeCard card = cardMapper.selectById(cardId);
        assertNotNull(card);
        assertEquals("测试问题", card.getFrontContent());
        assertEquals(categoryId, card.getCategoryId());

        Plant plant = plantMapper.selectByCardId(cardId);
        assertNotNull(plant);
        assertEquals(userId, plant.getUserId());
        assertEquals(cardId, plant.getCardId());
        assertEquals(1, plant.getGrowthStage().intValue());
        assertEquals(0, plant.getIsWithered().intValue());
    }

    @Test
    @DisplayName("删除卡片→级联软删除植物")
    void testDeleteCardCascadeSoftDelete() {
        Long userId = insertTestUser("delete_test");
        Long categoryId = insertTestCategory(userId);

        CardCreateRequest request = new CardCreateRequest();
        request.setFrontContent("待删除问题");
        request.setBackContent("待删除答案");
        request.setCategoryId(categoryId);

        Long cardId = cardService.create(userId, request);

        // 验证卡片和植物都存在
        KnowledgeCard cardBefore = cardMapper.selectById(cardId);
        assertNotNull(cardBefore, "卡片应该存在");
        assertEquals(0, cardBefore.getIsDeleted().intValue(), "卡片不应被删除");
        assertEquals(userId, cardBefore.getUserId(), "卡片userId应匹配");
        Plant plantBefore = plantMapper.selectByCardId(cardId);
        assertNotNull(plantBefore, "植物应该存在");

        // 删除卡片
        boolean deleted = cardService.delete(cardBefore.getUserId(), cardBefore.getId());
        assertTrue(deleted);

        // 验证卡片已软删除
        KnowledgeCard deletedCard = cardMapper.selectById(cardId);
        assertNull(deletedCard);

        // 验证植物也已软删除
        Plant deletedPlant = plantMapper.selectByCardId(cardId);
        assertNull(deletedPlant);
    }

    private Long insertTestUser(String suffix) {
        User user = new User();
        user.setUsername("carduser_" + suffix + "_" + System.nanoTime());
        user.setPassword("fake_password");
        user.setNickname("测试用户");
        user.setIsDeleted(0);
        user.setStatus(1);
        userMapper.insert(user);
        return user.getId();
    }

    private Long insertTestCategory(Long userId) {
        Category category = new Category();
        category.setUserId(userId);
        category.setName("测试分类_" + System.nanoTime());
        category.setSortOrder(0);
        category.setIsDeleted(0);
        categoryMapper.insert(category);
        return category.getId();
    }
}
