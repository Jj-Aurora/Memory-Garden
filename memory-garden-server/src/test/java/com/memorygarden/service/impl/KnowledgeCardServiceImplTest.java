package com.memorygarden.service.impl;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.model.dto.CardCreateRequest;
import com.memorygarden.model.dto.CardUpdateRequest;
import com.memorygarden.model.entity.Category;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.vo.CardVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * KnowledgeCardServiceImpl 全面单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class KnowledgeCardServiceImplTest {

    @Mock
    private KnowledgeCardMapper cardMapper;

    @Mock
    private PlantMapper plantMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private KnowledgeCardServiceImpl cardService;

    private CardCreateRequest createRequest;
    private KnowledgeCard existingCard;
    private Plant existingPlant;
    private Category category;

    @BeforeEach
    void setUp() {
        createRequest = new CardCreateRequest();
        createRequest.setCategoryId(10L);
        createRequest.setFrontContent("What is Java?");
        createRequest.setBackContent("A programming language");
        createRequest.setNote("OOP");

        category = new Category();
        category.setId(10L);
        category.setUserId(100L);
        category.setName("编程");
        category.setIsDeleted(0);

        existingCard = new KnowledgeCard();
        existingCard.setId(1L);
        existingCard.setUserId(100L);
        existingCard.setCategoryId(10L);
        existingCard.setFrontContent("What is Java?");
        existingCard.setBackContent("A programming language");
        existingCard.setNote("OOP");
        existingCard.setSourceType(0);
        existingCard.setIsDeleted(0);

        existingPlant = new Plant();
        existingPlant.setId(1L);
        existingPlant.setCardId(1L);
        existingPlant.setUserId(100L);
        existingPlant.setGrowthStage(1);
        existingPlant.setIsWithered(0);
    }

    @Nested
    @DisplayName("创建卡片测试")
    class CreateTests {

        @Test
        @DisplayName("创建卡片成功-自动创建Plant种子")
        void testCreate_Success() {
            when(categoryMapper.selectById(10L)).thenReturn(category);
            when(cardMapper.insert(any(KnowledgeCard.class))).thenAnswer(invocation -> {
                KnowledgeCard card = invocation.getArgument(0);
                card.setId(1L);
                return 1;
            });
            when(plantMapper.insert(any(Plant.class))).thenReturn(1);

            Long id = cardService.create(100L, createRequest);

            assertNotNull(id);
            verify(cardMapper).insert(argThat(card ->
                    card.getUserId().equals(100L) &&
                    card.getFrontContent().equals("What is Java?") &&
                    card.getBackContent().equals("A programming language") &&
                    card.getSourceType() == 0 &&
                    card.getIsDeleted() == 0
            ));
            verify(plantMapper).insert(argThat(plant ->
                    plant.getGrowthStage() == 1 &&
                    plant.getIsWithered() == 0 &&
                    plant.getReviewRound() == 1 &&
                    plant.getIsDeleted() == 0
            ));
        }

        @Test
        @DisplayName("创建卡片-正面内容为null抛异常")
        void testCreate_NullFront() {
            createRequest.setFrontContent(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("正面内容不能为空"));
            verify(cardMapper, never()).insert(any());
        }

        @Test
        @DisplayName("创建卡片-正面内容为空字符串抛异常")
        void testCreate_EmptyFront() {
            createRequest.setFrontContent("");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("正面内容不能为空"));
        }

        @Test
        @DisplayName("创建卡片-背面内容为null抛异常")
        void testCreate_NullBack() {
            createRequest.setBackContent(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("背面内容不能为空"));
        }

        @Test
        @DisplayName("创建卡片-背面内容为空字符串抛异常")
        void testCreate_EmptyBack() {
            createRequest.setBackContent("");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("背面内容不能为空"));
        }

        @Test
        @DisplayName("创建卡片-分类不存在抛异常")
        void testCreate_CategoryNotFound() {
            when(categoryMapper.selectById(999L)).thenReturn(null);
            createRequest.setCategoryId(999L);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("分类不存在"));
        }

        @Test
        @DisplayName("创建卡片-分类已软删除抛异常")
        void testCreate_CategoryDeleted() {
            category.setIsDeleted(1);
            when(categoryMapper.selectById(10L)).thenReturn(category);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("分类不存在"));
        }

        @Test
        @DisplayName("创建卡片-无分类时正常创建")
        void testCreate_NoCategory() {
            createRequest.setCategoryId(null);
            when(cardMapper.insert(any(KnowledgeCard.class))).thenAnswer(invocation -> {
                KnowledgeCard card = invocation.getArgument(0);
                card.setId(1L);
                return 1;
            });
            when(plantMapper.insert(any(Plant.class))).thenReturn(1);

            Long id = cardService.create(100L, createRequest);

            assertNotNull(id);
            verify(categoryMapper, never()).selectById(any());
        }
    }

    @Nested
    @DisplayName("删除卡片测试")
    class DeleteTests {

        @Test
        @DisplayName("删除卡片-级联软删除Plant")
        void testDelete_CascadeSoftDelete() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);
            when(plantMapper.selectByCardId(1L)).thenReturn(existingPlant);
            when(cardMapper.updateById(any(KnowledgeCard.class))).thenReturn(1);
            when(plantMapper.updateById(any(Plant.class))).thenReturn(1);

            boolean result = cardService.delete(1L, 100L);

            assertTrue(result);
            verify(cardMapper).updateById(argThat(card -> card.getIsDeleted() == 1));
            verify(plantMapper).updateById(argThat(plant -> plant.getIsDeleted() == 1));
        }

        @Test
        @DisplayName("删除卡片-无关联Plant时仅删除卡片")
        void testDelete_NoPlant() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);
            when(plantMapper.selectByCardId(1L)).thenReturn(null);
            when(cardMapper.updateById(any(KnowledgeCard.class))).thenReturn(1);

            boolean result = cardService.delete(1L, 100L);

            assertTrue(result);
            verify(cardMapper).updateById(argThat(card -> card.getIsDeleted() == 1));
            verify(plantMapper, never()).updateById(any());
        }

        @Test
        @DisplayName("删除卡片-卡片不存在抛异常")
        void testDelete_NotFound() {
            when(cardMapper.selectById(999L)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.delete(999L, 100L);
            });

            assertTrue(exception.getMessage().contains("卡片不存在"));
        }

        @Test
        @DisplayName("删除卡片-卡片已软删除抛异常")
        void testDelete_AlreadyDeleted() {
            existingCard.setIsDeleted(1);
            when(cardMapper.selectById(1L)).thenReturn(existingCard);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.delete(1L, 100L);
            });

            assertTrue(exception.getMessage().contains("卡片不存在"));
        }

        @Test
        @DisplayName("删除卡片-非本人抛异常（权限控制）")
        void testDelete_NotOwner() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.delete(1L, 200L);
            });

            assertTrue(exception.getMessage().contains("无权操作"));
        }
    }

    @Nested
    @DisplayName("获取卡片测试")
    class GetTests {

        @Test
        @DisplayName("根据ID获取卡片-含分类名称")
        void testGetById_WithCategory() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);
            when(categoryMapper.selectById(10L)).thenReturn(category);

            CardVO vo = cardService.getById(1L, 100L);

            assertNotNull(vo);
            assertEquals("What is Java?", vo.getFrontContent());
            assertEquals("编程", vo.getCategoryName());
        }

        @Test
        @DisplayName("根据ID获取卡片-卡片不存在抛异常")
        void testGetById_NotFound() {
            when(cardMapper.selectById(999L)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.getById(999L, 100L);
            });

            assertTrue(exception.getMessage().contains("卡片不存在"));
        }

        @Test
        @DisplayName("根据ID获取卡片-卡片已删除抛异常")
        void testGetById_Deleted() {
            existingCard.setIsDeleted(1);
            when(cardMapper.selectById(1L)).thenReturn(existingCard);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.getById(1L, 100L);
            });

            assertTrue(exception.getMessage().contains("卡片不存在"));
        }

        @Test
        @DisplayName("获取卡片列表-按用户ID")
        void testList_ByUserId() {
            when(cardMapper.selectByUserId(100L)).thenReturn(Arrays.asList(existingCard));
            when(categoryMapper.selectById(10L)).thenReturn(category);

            List<CardVO> list = cardService.list(100L, null);

            assertNotNull(list);
            assertEquals(1, list.size());
        }

        @Test
        @DisplayName("获取卡片列表-按分类ID")
        void testList_ByCategoryId() {
            when(cardMapper.selectByCategoryId(10L)).thenReturn(Arrays.asList(existingCard));
            when(categoryMapper.selectById(10L)).thenReturn(category);

            List<CardVO> list = cardService.list(100L, 10L);

            assertNotNull(list);
            assertEquals(1, list.size());
        }

        @Test
        @DisplayName("获取卡片列表-过滤已删除卡片")
        void testList_FilterDeleted() {
            KnowledgeCard deletedCard = new KnowledgeCard();
            deletedCard.setId(2L);
            deletedCard.setIsDeleted(1);

            when(cardMapper.selectByUserId(100L)).thenReturn(Arrays.asList(existingCard, deletedCard));
            when(categoryMapper.selectById(10L)).thenReturn(category);

            List<CardVO> list = cardService.list(100L, null);

            assertEquals(1, list.size());
            assertEquals(1L, list.get(0).getId());
        }
    }

    @Nested
    @DisplayName("修改卡片测试")
    class UpdateTests {

        @Test
        @DisplayName("修改卡片成功")
        void testUpdate_Success() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);
            when(cardMapper.updateById(any(KnowledgeCard.class))).thenReturn(1);

            CardUpdateRequest updateReq = new CardUpdateRequest();
            updateReq.setFrontContent("New question");

            boolean result = cardService.update(1L, 100L, updateReq);

            assertTrue(result);
            verify(cardMapper).updateById(argThat(card ->
                    card.getFrontContent().equals("New question")
            ));
        }

        @Test
        @DisplayName("修改卡片-非本人抛异常（权限控制）")
        void testUpdate_NotOwner() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);

            CardUpdateRequest updateReq = new CardUpdateRequest();
            updateReq.setFrontContent("New question");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.update(1L, 200L, updateReq);
            });

            assertTrue(exception.getMessage().contains("无权操作"));
        }

        @Test
        @DisplayName("修改卡片-卡片不存在抛异常")
        void testUpdate_NotFound() {
            when(cardMapper.selectById(999L)).thenReturn(null);

            CardUpdateRequest updateReq = new CardUpdateRequest();
            updateReq.setFrontContent("New question");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                cardService.update(999L, 100L, updateReq);
            });

            assertTrue(exception.getMessage().contains("卡片不存在"));
        }

        @Test
        @DisplayName("修改卡片-数据库更新失败返回false")
        void testUpdate_Failed() {
            when(cardMapper.selectById(1L)).thenReturn(existingCard);
            when(cardMapper.updateById(any(KnowledgeCard.class))).thenReturn(0);

            CardUpdateRequest updateReq = new CardUpdateRequest();
            updateReq.setFrontContent("New question");

            boolean result = cardService.update(1L, 100L, updateReq);

            assertFalse(result);
        }
    }
}
