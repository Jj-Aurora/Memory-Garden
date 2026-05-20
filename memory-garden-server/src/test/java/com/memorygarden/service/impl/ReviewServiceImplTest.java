package com.memorygarden.service.impl;

import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.ReviewRecordMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.dto.ReviewSubmitRequest;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.ReviewRecord;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.enums.SelfEvaluation;
import com.memorygarden.model.vo.ReviewSummaryVO;
import com.memorygarden.model.vo.ReviewVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ReviewServiceImpl 全面单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private PlantMapper plantMapper;

    @Mock
    private KnowledgeCardMapper cardMapper;

    @Mock
    private ReviewRecordMapper reviewRecordMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Plant plant1;
    private Plant plant2;
    private KnowledgeCard card1;
    private KnowledgeCard card2;
    private User user;

    @BeforeEach
    void setUp() {
        card1 = new KnowledgeCard();
        card1.setId(1L);
        card1.setFrontContent("What is Java?");
        card1.setBackContent("A programming language");
        card1.setNote("OOP");
        card1.setIsDeleted(0);

        card2 = new KnowledgeCard();
        card2.setId(2L);
        card2.setFrontContent("What is Python?");
        card2.setBackContent("A scripting language");
        card2.setNote("Dynamic");
        card2.setIsDeleted(0);

        plant1 = new Plant();
        plant1.setId(1L);
        plant1.setCardId(1L);
        plant1.setUserId(100L);
        plant1.setGrowthStage(1);
        plant1.setIsWithered(0);
        plant1.setReviewRound(1);
        plant1.setNextReviewDate(LocalDate.now());
        plant1.setIsDeleted(0);

        plant2 = new Plant();
        plant2.setId(2L);
        plant2.setCardId(2L);
        plant2.setUserId(100L);
        plant2.setGrowthStage(3);
        plant2.setIsWithered(0);
        plant2.setReviewRound(2);
        plant2.setNextReviewDate(LocalDate.now().plusDays(1));
        plant2.setIsDeleted(0);

        user = new User();
        user.setId(100L);
        user.setCurrentStreak(0);
        user.setMaxStreak(0);
        user.setLastCheckIn(LocalDate.now().minusDays(1));
    }

    @Nested
    @DisplayName("获取待复习列表测试")
    class GetPendingTests {

        @Test
        @DisplayName("获取待复习列表-仅返回今日到期")
        void testGetPending_TodayOnly() {
            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2));
            when(cardMapper.selectById(1L)).thenReturn(card1);

            List<ReviewVO> pending = reviewService.getPending(100L);

            assertEquals(1, pending.size());
            assertEquals(1L, pending.get(0).getCardId());
        }

        @Test
        @DisplayName("获取待复习列表-包含逾期卡片")
        void testGetPending_Overdue() {
            plant1.setNextReviewDate(LocalDate.now().minusDays(3));
            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1));
            when(cardMapper.selectById(1L)).thenReturn(card1);

            List<ReviewVO> pending = reviewService.getPending(100L);

            assertEquals(1, pending.size());
        }

        @Test
        @DisplayName("获取待复习列表-过滤已删除植物")
        void testGetPending_FilterDeleted() {
            plant1.setIsDeleted(1);
            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1));

            List<ReviewVO> pending = reviewService.getPending(100L);

            assertEquals(0, pending.size());
        }

        @Test
        @DisplayName("获取待复习列表-过滤nextReviewDate为null")
        void testGetPending_NullReviewDate() {
            plant1.setNextReviewDate(null);
            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1));

            List<ReviewVO> pending = reviewService.getPending(100L);

            assertEquals(0, pending.size());
        }

        @Test
        @DisplayName("获取待复习列表-按日期排序")
        void testGetPending_SortedByDate() {
            Plant plant3 = new Plant();
            plant3.setId(3L);
            plant3.setCardId(3L);
            plant3.setUserId(100L);
            plant3.setGrowthStage(2);
            plant3.setIsWithered(0);
            plant3.setReviewRound(1);
            plant3.setNextReviewDate(LocalDate.now().minusDays(1));
            plant3.setIsDeleted(0);

            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant3));
            when(cardMapper.selectById(1L)).thenReturn(card1);
            when(cardMapper.selectById(3L)).thenReturn(card2);

            List<ReviewVO> pending = reviewService.getPending(100L);

            assertEquals(2, pending.size());
            assertTrue(pending.get(0).getScheduledDate().isBefore(pending.get(1).getScheduledDate())
                    || pending.get(0).getScheduledDate().equals(pending.get(1).getScheduledDate()));
        }

        @Test
        @DisplayName("获取待复习列表-无待复习返回空列表")
        void testGetPending_Empty() {
            when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());

            List<ReviewVO> pending = reviewService.getPending(100L);

            assertTrue(pending.isEmpty());
        }
    }

    @Nested
    @DisplayName("获取下一个待复习测试")
    class GetNextTests {

        @Test
        @DisplayName("获取下一个待复习")
        void testGetNext() {
            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2));
            when(cardMapper.selectById(1L)).thenReturn(card1);

            ReviewVO next = reviewService.getNext(100L);

            assertNotNull(next);
            assertEquals(1L, next.getCardId());
        }

        @Test
        @DisplayName("无待复习返回null")
        void testGetNext_Empty() {
            when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());

            ReviewVO next = reviewService.getNext(100L);

            assertNull(next);
        }
    }

    @Nested
    @DisplayName("提交复习测试")
    class SubmitTests {

        private void setupSubmitMocks() {
            when(plantMapper.updateById(any(Plant.class))).thenReturn(1);
            when(reviewRecordMapper.insert(any(ReviewRecord.class))).thenReturn(1);
            when(userMapper.selectById(100L)).thenReturn(user);
            when(userMapper.updateById(any(User.class))).thenReturn(1);
        }

        @Test
        @DisplayName("记住了→升级+推进轮次")
        void testSubmit_Remembered() {
            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            boolean result = reviewService.submit(100L, request);

            assertTrue(result);
            verify(plantMapper).updateById(argThat(p ->
                    p.getGrowthStage() == 2 && p.getReviewRound() == 2
            ));
        }

        @Test
        @DisplayName("模糊→维持阶段+轮次不变")
        void testSubmit_Vague() {
            plant1.setGrowthStage(3);
            plant1.setReviewRound(2);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.VAGUE.getValue());

            boolean result = reviewService.submit(100L, request);

            assertTrue(result);
            verify(plantMapper).updateById(argThat(p ->
                    p.getGrowthStage() == 3 && p.getReviewRound() == 2
            ));
        }

        @Test
        @DisplayName("忘记了→回退阶段+轮次减1")
        void testSubmit_Forgotten() {
            plant1.setGrowthStage(3);
            plant1.setReviewRound(2);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.FORGOTTEN.getValue());

            boolean result = reviewService.submit(100L, request);

            assertTrue(result);
            verify(plantMapper).updateById(argThat(p ->
                    p.getGrowthStage() == 2 && p.getReviewRound() == 1
            ));
        }

        @Test
        @DisplayName("忘记了-轮次最低为1")
        void testSubmit_Forgotten_MinRound() {
            plant1.setGrowthStage(1);
            plant1.setReviewRound(1);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.FORGOTTEN.getValue());

            reviewService.submit(100L, request);

            verify(plantMapper).updateById(argThat(p -> p.getReviewRound() == 1));
        }

        @Test
        @DisplayName("枯萎+记住→复活解除枯萎")
        void testSubmit_WitheredRevive() {
            plant1.setIsWithered(1);
            plant1.setGrowthStage(2);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            boolean result = reviewService.submit(100L, request);

            assertTrue(result);
            verify(plantMapper).updateById(argThat(p -> p.getIsWithered() == 0));
        }

        @Test
        @DisplayName("枯萎+忘记→仍然枯萎")
        void testSubmit_WitheredForgotten() {
            plant1.setIsWithered(1);
            plant1.setGrowthStage(2);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.FORGOTTEN.getValue());

            reviewService.submit(100L, request);

            verify(plantMapper).updateById(argThat(p -> p.getIsWithered() == 1));
        }

        @Test
        @DisplayName("植物不存在抛异常")
        void testSubmit_PlantNotFound() {
            when(plantMapper.selectByCardId(999L)).thenReturn(null);

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(999L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                reviewService.submit(100L, request);
            });

            assertTrue(exception.getMessage().contains("植物不存在"));
        }

        @Test
        @DisplayName("植物已删除抛异常")
        void testSubmit_PlantDeleted() {
            plant1.setIsDeleted(1);
            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                reviewService.submit(100L, request);
            });

            assertTrue(exception.getMessage().contains("植物不存在"));
        }

        @Test
        @DisplayName("无效自评值抛异常")
        void testSubmit_InvalidEvaluation() {
            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(99);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                reviewService.submit(100L, request);
            });

            assertTrue(exception.getMessage().contains("无效的自评值"));
        }

        @Test
        @DisplayName("打卡天数-连续打卡+1")
        void testSubmit_StreakContinue() {
            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            reviewService.submit(100L, request);

            verify(userMapper).updateById(argThat(u -> u.getCurrentStreak() == 1));
        }

        @Test
        @DisplayName("打卡天数-断签后重置为1")
        void testSubmit_StreakReset() {
            user.setLastCheckIn(LocalDate.now().minusDays(3));
            user.setCurrentStreak(5);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            reviewService.submit(100L, request);

            verify(userMapper).updateById(argThat(u -> u.getCurrentStreak() == 1));
        }

        @Test
        @DisplayName("阶段5记住→不超5（最高阶段）")
        void testSubmit_MaxStage() {
            plant1.setGrowthStage(5);
            plant1.setReviewRound(3);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.REMEMBERED.getValue());

            reviewService.submit(100L, request);

            verify(plantMapper).updateById(argThat(p -> p.getGrowthStage() == 5));
        }

        @Test
        @DisplayName("阶段1忘记→不低1（最低阶段）")
        void testSubmit_MinStage() {
            plant1.setGrowthStage(1);
            plant1.setReviewRound(1);

            when(plantMapper.selectByCardId(1L)).thenReturn(plant1);
            when(cardMapper.selectById(1L)).thenReturn(card1);
            setupSubmitMocks();

            ReviewSubmitRequest request = new ReviewSubmitRequest();
            request.setCardId(1L);
            request.setSelfEvaluation(SelfEvaluation.FORGOTTEN.getValue());

            reviewService.submit(100L, request);

            verify(plantMapper).updateById(argThat(p -> p.getGrowthStage() == 1));
        }
    }

    @Nested
    @DisplayName("复习总结测试")
    class SummaryTests {

        @Test
        @DisplayName("获取今日复习总结")
        void testGetSummary() {
            ReviewRecord r1 = new ReviewRecord();
            r1.setSelfEvaluation(1);
            r1.setPrevStage(1);
            r1.setAfterStage(2);

            ReviewRecord r2 = new ReviewRecord();
            r2.setSelfEvaluation(3);
            r2.setPrevStage(3);
            r2.setAfterStage(2);

            when(reviewRecordMapper.selectByUserIdAndDate(eq(100L), any(LocalDate.class)))
                    .thenReturn(Arrays.asList(r1, r2));
            when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());

            ReviewSummaryVO summary = reviewService.getSummary(100L);

            assertNotNull(summary);
            assertEquals(2, summary.getTotalReviewed());
            assertEquals(1, summary.getRememberedCount());
            assertEquals(1, summary.getForgottenCount());
            assertEquals(1, summary.getUpgradedCount());
            assertEquals(1, summary.getDowngradedCount());
        }

        @Test
        @DisplayName("获取今日复习总结-无记录")
        void testGetSummary_NoRecords() {
            when(reviewRecordMapper.selectByUserIdAndDate(eq(100L), any(LocalDate.class)))
                    .thenReturn(Collections.emptyList());
            when(plantMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());

            ReviewSummaryVO summary = reviewService.getSummary(100L);

            assertNotNull(summary);
            assertEquals(0, summary.getTotalReviewed());
            assertEquals(0, summary.getRememberedCount());
        }

        @Test
        @DisplayName("获取待复习数量")
        void testGetPendingCount() {
            when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2));

            int count = reviewService.getPendingCount(100L);

            assertEquals(1, count);
        }
    }
}
