package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.entity.StudyPack;
import com.memorygarden.model.entity.StudyPackItem;
import com.memorygarden.service.StudyPackService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * StudyPackController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudyPackController 接口测试")
class StudyPackControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudyPackService studyPackService;

    @InjectMocks
    private StudyPackController studyPackController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studyPackController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("获取知识包列表接口测试 GET /api/study-pack/list")
    class ListTests {

        @Test
        @DisplayName("获取知识包列表成功")
        void testList_Success() throws Exception {
            StudyPack pack = new StudyPack();
            pack.setId(1L);
            pack.setName("英语基础");
            when(studyPackService.list()).thenReturn(Arrays.asList(pack));

            mockMvc.perform(get("/api/study-pack/list"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }
    }

    @Nested
    @DisplayName("获取知识包详情接口测试 GET /api/study-pack/{id}")
    class DetailTests {

        @Test
        @DisplayName("获取知识包详情成功")
        void testGetDetail_Success() throws Exception {
            StudyPack pack = new StudyPack();
            pack.setId(1L);
            pack.setName("英语基础");
            when(studyPackService.getDetail(1L)).thenReturn(pack);

            mockMvc.perform(get("/api/study-pack/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("英语基础"));
        }
    }

    @Nested
    @DisplayName("获取知识包条目接口测试 GET /api/study-pack/{id}/items")
    class ItemsTests {

        @Test
        @DisplayName("获取知识包条目成功")
        void testGetPackItems_Success() throws Exception {
            StudyPackItem item = new StudyPackItem();
            item.setId(1L);
            item.setFrontContent("Hello");
            when(studyPackService.getPackItems(1L)).thenReturn(Arrays.asList(item));

            mockMvc.perform(get("/api/study-pack/1/items"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }
    }

    @Nested
    @DisplayName("导入知识包接口测试 POST /api/study-pack/{id}/import")
    class ImportTests {

        @Test
        @DisplayName("导入知识包成功")
        void testImportPack_Success() throws Exception {
            when(studyPackService.importPack(1L, 1L)).thenReturn(10);

            mockMvc.perform(post("/api/study-pack/1/import")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(10));
        }
    }
}
