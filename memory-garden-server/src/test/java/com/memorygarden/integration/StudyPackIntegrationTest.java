package com.memorygarden.integration;

import com.memorygarden.mapper.*;
import com.memorygarden.model.entity.*;
import com.memorygarden.service.StudyPackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudyPack 导入集成测试（浏览→导入→批量创建卡片+植物）
 *
 * @author jLU
 * @date 2026-05-20
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("I-18: StudyPack 导入集成测试")
class StudyPackIntegrationTest {

    @Resource
    private StudyPackService studyPackService;

    @Resource
    private StudyPackMapper studyPackMapper;

    @Resource
    private StudyPackItemMapper studyPackItemMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private KnowledgeCardMapper cardMapper;

    @Resource
    private PlantMapper plantMapper;

    private Long testUserId;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("pack_user_" + System.currentTimeMillis());
        user.setPassword("fake_password");
        user.setNickname("知识包测试用户");
        user.setIsDeleted(0);
        user.setStatus(1);
        userMapper.insert(user);
        testUserId = user.getId();
    }

    @Test
    @DisplayName("浏览知识包列表-初始数据3个")
    void testListStudyPacks() {
        List<StudyPack> packs = studyPackMapper.selectAll();
        assertNotNull(packs);
        assertEquals(3, packs.size());
    }

    @Test
    @DisplayName("查看知识包详情-包含条目")
    void testGetStudyPackDetail() {
        StudyPack detail = studyPackService.getDetail(1L);
        assertNotNull(detail);
        assertEquals("Java 基础", detail.getName());

        // 验证条目
        List<StudyPackItem> items = studyPackService.getPackItems(1L);
        assertNotNull(items);
        assertEquals(6, items.size());
    }

    @Test
    @DisplayName("导入知识包-批量创建卡片和植物")
    void testImportStudyPack() {
        // 导入 Java 基础知识包
        studyPackService.importPack(testUserId, 1L);

        // 验证卡片已创建
        List<KnowledgeCard> cards = cardMapper.selectByUserId(testUserId);
        assertNotNull(cards);
        assertEquals(6, cards.size());

        // 验证每张卡片都有对应的植物
        for (KnowledgeCard card : cards) {
            Plant plant = plantMapper.selectByCardId(card.getId());
            assertNotNull(plant, "卡片 id=" + card.getId() + " 应有对应植物");
            assertEquals(1, plant.getGrowthStage().intValue());
        }
    }

    @Test
    @DisplayName("导入知识包-卡片来源标记为预设库导入")
    void testImportSourceType() {
        studyPackService.importPack(testUserId, 2L);

        List<KnowledgeCard> cards = cardMapper.selectByUserId(testUserId);
        for (KnowledgeCard card : cards) {
            assertEquals(1, card.getSourceType().intValue());
            assertEquals(2L, card.getSourcePackId());
        }
    }
}
