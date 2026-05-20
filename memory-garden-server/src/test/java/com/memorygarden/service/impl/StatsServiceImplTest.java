package com.memorygarden.service.impl;

import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.ReviewRecordMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.ReviewRecord;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.vo.StatsVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * StatsServiceImpl 单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PlantMapper plantMapper;

    @Mock
    private ReviewRecordMapper reviewRecordMapper;

    @InjectMocks
    private StatsServiceImpl statsService;

    private User user;
    private Plant plant1;
    private Plant plant2;
    private ReviewRecord record1;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(100L);
        user.setCurrentStreak(5);
        user.setMaxStreak(10);
        user.setLastCheckIn(LocalDate.now());

        plant1 = new Plant();
        plant1.setId(1L);
        plant1.setGrowthStage(1);
        plant1.setIsWithered(0);
        plant1.setIsDeleted(0);

        plant2 = new Plant();
        plant2.setId(2L);
        plant2.setGrowthStage(3);
        plant2.setIsWithered(0);
        plant2.setIsDeleted(0);

        record1 = new ReviewRecord();
        record1.setSelfEvaluation(1);
        record1.setPrevStage(1);
        record1.setAfterStage(2);
    }

    // ========== A-52: getToday 测试 ==========

    @Test
    @DisplayName("获取今日学习数据")
    void testGetToday() {
        when(userMapper.selectById(100L)).thenReturn(user);
        when(reviewRecordMapper.selectByUserIdAndDate(eq(100L), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(record1));
        when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2));

        StatsVO stats = statsService.getToday(100L);

        assertNotNull(stats);
        assertEquals(5, stats.getCurrentStreak());
        assertEquals(10, stats.getMaxStreak());
        assertEquals(1, stats.getTodayReviewCount());
    }

    // ========== A-54: getTrend / getStreak / getStageDistribution 测试 ==========

    @Test
    @DisplayName("获取趋势数据")
    void testGetTrend() {
        when(reviewRecordMapper.selectByUserIdAndDate(eq(100L), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(record1));

        Map<String, Integer> trend = statsService.getTrend(100L, 7);

        assertNotNull(trend);
        assertEquals(7, trend.size());
    }

    @Test
    @DisplayName("获取连续打卡天数")
    void testGetStreak() {
        when(userMapper.selectById(100L)).thenReturn(user);

        int streak = statsService.getStreak(100L);

        assertEquals(5, streak);
    }

    @Test
    @DisplayName("获取各阶段植物分布")
    void testGetStageDistribution() {
        when(plantMapper.selectByUserId(100L)).thenReturn(Arrays.asList(plant1, plant2));

        Map<Integer, Integer> distribution = statsService.getStageDistribution(100L);

        assertNotNull(distribution);
        assertEquals(1, distribution.get(1));
        assertEquals(1, distribution.get(3));
    }
}
