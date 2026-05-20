package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.CardCreateRequest;
import com.memorygarden.model.dto.CardUpdateRequest;
import com.memorygarden.model.vo.CardVO;
import com.memorygarden.service.KnowledgeCardService;
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
 * KnowledgeCardController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("KnowledgeCardController 接口测试")
class KnowledgeCardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private KnowledgeCardService cardService;

    @InjectMocks
    private KnowledgeCardController cardController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("创建卡片接口测试 POST /api/card")
    class CreateTests {

        @Test
        @DisplayName("创建卡片成功")
        void testCreate_Success() throws Exception {
            when(cardService.create(eq(1L), any(CardCreateRequest.class))).thenReturn(1L);

            mockMvc.perform(post("/api/card")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"frontContent\":\"问题\",\"backContent\":\"答案\",\"categoryId\":1}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data").value(1));
        }

        @Test
        @DisplayName("创建卡片-正面内容为空抛异常")
        void testCreate_NullFront() throws Exception {
            when(cardService.create(eq(1L), any(CardCreateRequest.class)))
                    .thenThrow(new RuntimeException("正面内容不能为空"));

            mockMvc.perform(post("/api/card")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"frontContent\":null,\"backContent\":\"答案\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }
    }

    @Nested
    @DisplayName("获取卡片详情接口测试 GET /api/card/{id}")
    class GetByIdTests {

        @Test
        @DisplayName("获取卡片详情成功")
        void testGetById_Success() throws Exception {
            CardVO vo = new CardVO();
            vo.setId(1L);
            vo.setFrontContent("问题");
            vo.setBackContent("答案");
            when(cardService.getById(1L, 1L)).thenReturn(vo);

            mockMvc.perform(get("/api/card/1")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.frontContent").value("问题"));
        }
    }

    @Nested
    @DisplayName("获取卡片列表接口测试 GET /api/card/list")
    class ListTests {

        @Test
        @DisplayName("获取卡片列表成功")
        void testList_Success() throws Exception {
            CardVO vo1 = new CardVO();
            vo1.setId(1L);
            vo1.setFrontContent("问题1");
            when(cardService.list(1L, null)).thenReturn(Arrays.asList(vo1));

            mockMvc.perform(get("/api/card/list")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }

        @Test
        @DisplayName("按分类筛选卡片")
        void testList_WithCategory() throws Exception {
            when(cardService.list(1L, 2L)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/card/list")
                            .param("categoryId", "2")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }

    @Nested
    @DisplayName("修改卡片接口测试 PUT /api/card/{id}")
    class UpdateTests {

        @Test
        @DisplayName("修改卡片成功")
        void testUpdate_Success() throws Exception {
            when(cardService.update(eq(1L), eq(1L), any(CardUpdateRequest.class))).thenReturn(true);

            mockMvc.perform(put("/api/card/1")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"frontContent\":\"新问题\",\"backContent\":\"新答案\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }
    }

    @Nested
    @DisplayName("删除卡片接口测试 DELETE /api/card/{id}")
    class DeleteTests {

        @Test
        @DisplayName("删除卡片成功")
        void testDelete_Success() throws Exception {
            when(cardService.delete(1L, 1L)).thenReturn(true);

            mockMvc.perform(delete("/api/card/1")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }
    }
}
