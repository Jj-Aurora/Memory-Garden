package com.memorygarden.service.impl;

import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.StudyPackItemMapper;
import com.memorygarden.mapper.StudyPackMapper;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.StudyPack;
import com.memorygarden.model.entity.StudyPackItem;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * StudyPackServiceImpl 单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class StudyPackServiceImplTest {

    @Mock
    private StudyPackMapper studyPackMapper;

    @Mock
    private StudyPackItemMapper studyPackItemMapper;

    @Mock
    private KnowledgeCardMapper cardMapper;

    @Mock
    private PlantMapper plantMapper;

    @InjectMocks
    private StudyPackServiceImpl studyPackService;

    private StudyPack pack1;
    private StudyPackItem item1;
    private StudyPackItem item2;

    @BeforeEach
    void setUp() {
        pack1 = new StudyPack();
        pack1.setId(1L);
        pack1.setName("Java 基础");
        pack1.setDescription("Java 入门知识包");
        pack1.setCardCount(2);

        item1 = new StudyPackItem();
        item1.setId(1L);
        item1.setPackId(1L);
        item1.setFrontContent("What is JVM?");
        item1.setBackContent("Java Virtual Machine");

        item2 = new StudyPackItem();
        item2.setId(2L);
        item2.setPackId(1L);
        item2.setFrontContent("What is JDK?");
        item2.setBackContent("Java Development Kit");
    }

    // ========== A-56: list / getDetail 测试 ==========

    @Test
    @DisplayName("获取知识包列表")
    void testList() {
        when(studyPackMapper.selectAll()).thenReturn(Collections.singletonList(pack1));

        List<StudyPack> packs = studyPackService.list();

        assertEquals(1, packs.size());
        assertEquals("Java 基础", packs.get(0).getName());
    }

    @Test
    @DisplayName("获取知识包详情")
    void testGetDetail() {
        when(studyPackMapper.selectById(1L)).thenReturn(pack1);

        StudyPack detail = studyPackService.getDetail(1L);

        assertNotNull(detail);
        assertEquals("Java 基础", detail.getName());
    }

    @Test
    @DisplayName("获取知识包条目列表")
    void testGetPackItems() {
        when(studyPackItemMapper.selectByPackId(1L)).thenReturn(Arrays.asList(item1, item2));

        List<StudyPackItem> items = studyPackService.getPackItems(1L);

        assertEquals(2, items.size());
    }

    // ========== A-58: importPack 测试 ==========

    @Test
    @DisplayName("导入知识包-批量创建卡片+植物")
    void testImportPack() {
        when(studyPackMapper.selectById(1L)).thenReturn(pack1);
        when(studyPackItemMapper.selectByPackId(1L)).thenReturn(Arrays.asList(item1, item2));
        when(cardMapper.insert(any(KnowledgeCard.class))).thenReturn(1);
        when(plantMapper.insert(any(Plant.class))).thenReturn(1);

        int count = studyPackService.importPack(100L, 1L);

        assertEquals(2, count);
        verify(cardMapper, times(2)).insert(any(KnowledgeCard.class));
        verify(plantMapper, times(2)).insert(any(Plant.class));
    }

    @Test
    @DisplayName("导入知识包-知识包不存在抛异常")
    void testImportPack_NotFound() {
        when(studyPackMapper.selectById(999L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studyPackService.importPack(100L, 999L);
        });

        assertTrue(exception.getMessage().contains("知识包不存在"));
    }

    @Test
    @DisplayName("导入知识包-空条目返回0")
    void testImportPack_EmptyItems() {
        when(studyPackMapper.selectById(1L)).thenReturn(pack1);
        when(studyPackItemMapper.selectByPackId(1L)).thenReturn(Collections.emptyList());

        int count = studyPackService.importPack(100L, 1L);

        assertEquals(0, count);
        verify(cardMapper, never()).insert(any());
    }
}
