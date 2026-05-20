package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.vo.BadgeVO;
import com.memorygarden.service.BadgeService;
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

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BadgeController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BadgeController 接口测试")
class BadgeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BadgeService badgeService;

    @InjectMocks
    private BadgeController badgeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(badgeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("获取所有徽章列表接口测试 GET /api/badge/list")
    class ListTests {

        @Test
        @DisplayName("获取所有徽章列表成功")
        void testGetAllBadges_Success() throws Exception {
            BadgeVO vo = new BadgeVO();
            vo.setId(1L);
            vo.setName("初学者");
            vo.setEarned(true);
            when(badgeService.getAllBadges(1L)).thenReturn(Arrays.asList(vo));

            mockMvc.perform(get("/api/badge/list")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andExpect(jsonPath("$.data[0].earned").value(true));
        }
    }

    @Nested
    @DisplayName("获取已获得徽章接口测试 GET /api/badge/my")
    class MyBadgesTests {

        @Test
        @DisplayName("获取已获得徽章成功")
        void testGetMyBadges_Success() throws Exception {
            BadgeVO vo = new BadgeVO();
            vo.setId(1L);
            vo.setName("初学者");
            vo.setEarned(true);
            when(badgeService.getMyBadges(1L)).thenReturn(Arrays.asList(vo));

            mockMvc.perform(get("/api/badge/my")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }

        @Test
        @DisplayName("无已获得徽章-返回空列表")
        void testGetMyBadges_Empty() throws Exception {
            when(badgeService.getMyBadges(1L)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/badge/my")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }
}
