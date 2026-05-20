package com.memorygarden.service.impl;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.model.entity.Category;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.vo.GardenVO;
import com.memorygarden.model.vo.PlantVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

/**
 * PlantServiceImpl 单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PlantServiceImplTest {

    @Mock
    private PlantMapper plantMapper;

    @Mock
    private KnowledgeCardMapper cardMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private PlantServiceImpl plantService;

    private Plant plant1;
    private Plant plant2;
    private Plant witheredPlant;
    private KnowledgeCard card1;
    private KnowledgeCard card2;
    private KnowledgeCard card3;
    private Category category;

    @BeforeEach
    void setUp() {
        card1 = new KnowledgeCard();
        card1.setId(1L);
        card1.setFrontContent("What is Java?");
        card1.setCategoryId(10L);

        card2 = new KnowledgeCard();
        card2.setId(2L);
        card2.setFrontContent("What is Python?");
        card2.setCategoryId(10L);

        card3 = new KnowledgeCard();
        card3.setId(3L);
        card3.setFrontContent("What is C++?");
        card3.setCategoryId(10L);

        category = new Category();
        category.setId(10L);
        category.setName("编程");

        plant1 = new Plant();
        plant1.setId(1L);
        plant1.setCardId(1L);
        plant1.setUserId(100L);
        plant1.setGrowthStage(1);
        plant1.setIsWithered(0);
        plant1.setReviewRound(1);
        plant1.setIsDeleted(0);

        plant2 = new Plant();
        plant2.setId(2L);
        plant2.setCardId(2L);
        plant2.setUserId(100L);
        plant2.setGrowthStage(3);
        plant2.setIsWithered(0);
        plant2.setReviewRound(2);
        plant2.setIsDeleted(0);

        witheredPlant = new Plant();
        witheredPlant.setId(3L);
        witheredPlant.setCardId(3L);
        witheredPlant.setUserId(100L);
        witheredPlant.setGrowthStage(2);
        witheredPlant.setIsWithered(1);
        witheredPlant.setIsDeleted(0);
    }

    // ========== A-38: getGardenView 测试 ==========

    @Test
    @DisplayName("获取花园视图-按阶段分组统计")
    void testGetGardenView() {
        when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2, witheredPlant));
        when(cardMapper.selectById(1L)).thenReturn(card1);
        when(cardMapper.selectById(2L)).thenReturn(card2);
        when(cardMapper.selectById(3L)).thenReturn(card3);
        when(categoryMapper.selectById(10L)).thenReturn(category);

        GardenVO vo = plantService.getGardenView(100L);

        assertNotNull(vo);
        assertEquals(3, vo.getTotalCount());
        assertEquals(1, vo.getWitheredCount());
        assertNotNull(vo.getStageCount());
        assertEquals(1, vo.getStageCount().get(1));
        assertEquals(1, vo.getStageCount().get(3));
    }

    @Test
    @DisplayName("获取花园视图-无植物")
    void testGetGardenView_Empty() {
        when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());

        GardenVO vo = plantService.getGardenView(100L);

        assertNotNull(vo);
        assertEquals(0, vo.getTotalCount());
        assertEquals(0, vo.getWitheredCount());
    }

    // ========== A-40: filter / sort / getWithered 测试 ==========

    @Test
    @DisplayName("按阶段筛选植物")
    void testFilter_ByStage() {
        when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2));
        when(cardMapper.selectById(1L)).thenReturn(card1);
        when(cardMapper.selectById(2L)).thenReturn(card2);
        when(categoryMapper.selectById(10L)).thenReturn(category);

        List<PlantVO> result = plantService.filter(100L, null, 1, null);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getGrowthStage());
    }

    @Test
    @DisplayName("按枯萎状态筛选")
    void testFilter_ByWithered() {
        when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, witheredPlant));
        when(cardMapper.selectById(1L)).thenReturn(card1);
        when(cardMapper.selectById(3L)).thenReturn(card3);
        when(categoryMapper.selectById(10L)).thenReturn(category);

        List<PlantVO> result = plantService.filter(100L, null, null, true);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getWithered());
    }

    @Test
    @DisplayName("按生长阶段排序")
    void testSort_ByStage() {
        when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant2, plant1));
        when(cardMapper.selectById(1L)).thenReturn(card1);
        when(cardMapper.selectById(2L)).thenReturn(card2);
        when(categoryMapper.selectById(10L)).thenReturn(category);

        List<PlantVO> result = plantService.sort(100L, "growthStage", "asc");

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getGrowthStage());
        assertEquals(3, result.get(1).getGrowthStage());
    }

    @Test
    @DisplayName("获取枯萎植物列表")
    void testGetWithered() {
        when(plantMapper.selectWithered(100L)).thenReturn(Arrays.asList(witheredPlant));
        when(cardMapper.selectById(3L)).thenReturn(card3);
        when(categoryMapper.selectById(10L)).thenReturn(category);

        List<PlantVO> result = plantService.getWithered(100L);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getWithered());
    }
}
