package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.ReviewSubmitRequest;
import com.memorygarden.model.vo.ReviewSummaryVO;
import com.memorygarden.model.vo.ReviewVO;
import com.memorygarden.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ReviewController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewController 接口测试")
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("获取待复习列表接口测试 GET /api/review/pending")
    class PendingTests {

        @Test
        @DisplayName("获取待复习列表成功")
        void testGetPending_Success() throws Exception {
            ReviewVO vo = new ReviewVO();
            vo.setCardId(1L);
            when(reviewService.getPending(1L)).thenReturn(Arrays.asList(vo));

            mockMvc.perform(get("/api/review/pending")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }

        @Test
        @DisplayName("无待复习卡片-返回空列表")
        void testGetPending_Empty() throws Exception {
            when(reviewService.getPending(1L)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/review/pending")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }

    @Nested
    @DisplayName("获取下一个待复习接口测试 GET /api/review/next")
    class NextTests {

        @Test
        @DisplayName("获取下一个待复习成功")
        void testGetNext_Success() throws Exception {
            ReviewVO vo = new ReviewVO();
            vo.setCardId(1L);
            when(reviewService.getNext(1L)).thenReturn(vo);

            mockMvc.perform(get("/api/review/next")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.cardId").value(1));
        }
    }

    @Nested
    @DisplayName("提交复习自评接口测试 POST /api/review/submit")
    class SubmitTests {

        @Test
        @DisplayName("提交复习自评成功")
        void testSubmit_Success() throws Exception {
            when(reviewService.submit(eq(1L), any(ReviewSubmitRequest.class))).thenReturn(true);

            mockMvc.perform(post("/api/review/submit")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"cardId\":1,\"selfEvaluation\":1}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }
    }

    @Nested
    @DisplayName("获取复习总结接口测试 GET /api/review/summary")
    class SummaryTests {

        @Test
        @DisplayName("获取复习总结成功")
        void testGetSummary_Success() throws Exception {
            ReviewSummaryVO vo = new ReviewSummaryVO();
            vo.setTotalReviewed(5);
            vo.setRememberedCount(3);
            vo.setVagueCount(1);
            vo.setForgottenCount(1);
            when(reviewService.getSummary(1L)).thenReturn(vo);

            mockMvc.perform(get("/api/review/summary")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.totalReviewed").value(5));
        }
    }
}
