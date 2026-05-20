package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.vo.StatsVO;
import com.memorygarden.service.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * StatsController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StatsController 接口测试")
class StatsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private StatsController statsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("获取今日学习数据接口测试 GET /api/stats/today")
    class TodayTests {

        @Test
        @DisplayName("获取今日学习数据成功")
        void testGetToday_Success() throws Exception {
            StatsVO vo = new StatsVO();
            vo.setTodayReviewCount(5);
            vo.setTodayNewCards(3);
            when(statsService.getToday(1L)).thenReturn(vo);

            mockMvc.perform(get("/api/stats/today")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.todayReviewCount").value(5));
        }
    }

    @Nested
    @DisplayName("获取趋势数据接口测试 GET /api/stats/trend")
    class TrendTests {

        @Test
        @DisplayName("获取7天趋势数据成功")
        void testGetTrend_Success() throws Exception {
            Map<String, Integer> trend = new HashMap<>();
            trend.put("2026-05-19", 5);
            trend.put("2026-05-20", 8);
            when(statsService.getTrend(1L, 7)).thenReturn(trend);

            mockMvc.perform(get("/api/stats/trend")
                            .param("days", "7")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isMap());
        }
    }

    @Nested
    @DisplayName("获取连续打卡天数接口测试 GET /api/stats/streak")
    class StreakTests {

        @Test
        @DisplayName("获取连续打卡天数成功")
        void testGetStreak_Success() throws Exception {
            when(statsService.getStreak(1L)).thenReturn(7);

            mockMvc.perform(get("/api/stats/streak")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(7));
        }
    }

    @Nested
    @DisplayName("获取阶段分布接口测试 GET /api/stats/stage-distribution")
    class StageDistributionTests {

        @Test
        @DisplayName("获取阶段分布成功")
        void testGetStageDistribution_Success() throws Exception {
            Map<Integer, Integer> dist = new HashMap<>();
            dist.put(0, 10);
            dist.put(1, 5);
            when(statsService.getStageDistribution(1L)).thenReturn(dist);

            mockMvc.perform(get("/api/stats/stage-distribution")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isMap());
        }
    }
}
