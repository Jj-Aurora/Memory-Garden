package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.CategoryCreateRequest;
import com.memorygarden.model.entity.Category;
import com.memorygarden.service.CategoryService;
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
 * CategoryController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryController 接口测试")
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("创建分类接口测试 POST /api/category")
    class CreateTests {

        @Test
        @DisplayName("创建分类成功")
        void testCreate_Success() throws Exception {
            when(categoryService.create(eq(1L), any(CategoryCreateRequest.class))).thenReturn(1L);

            mockMvc.perform(post("/api/category")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"英语\",\"icon\":\"book\",\"sortOrder\":1}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data").value(1));
        }
    }

    @Nested
    @DisplayName("获取分类列表接口测试 GET /api/category/list")
    class ListTests {

        @Test
        @DisplayName("获取分类列表成功")
        void testList_Success() throws Exception {
            Category cat1 = new Category();
            cat1.setId(1L);
            cat1.setName("英语");
            Category cat2 = new Category();
            cat2.setId(2L);
            cat2.setName("数学");

            when(categoryService.list(1L)).thenReturn(Arrays.asList(cat1, cat2));

            mockMvc.perform(get("/api/category/list")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data.length()").value(2));
        }

        @Test
        @DisplayName("获取分类列表-空列表")
        void testList_Empty() throws Exception {
            when(categoryService.list(1L)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/category/list")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }

    @Nested
    @DisplayName("修改分类接口测试 PUT /api/category/{id}")
    class UpdateTests {

        @Test
        @DisplayName("修改分类成功")
        void testUpdate_Success() throws Exception {
            when(categoryService.update(eq(1L), eq(1L), any(CategoryCreateRequest.class))).thenReturn(true);

            mockMvc.perform(put("/api/category/1")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"新英语\",\"icon\":\"book2\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }

        @Test
        @DisplayName("修改分类-非本人分类抛异常")
        void testUpdate_NotOwner() throws Exception {
            when(categoryService.update(eq(1L), eq(2L), any(CategoryCreateRequest.class)))
                    .thenThrow(new RuntimeException("无权操作"));

            mockMvc.perform(put("/api/category/1")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 2L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"新英语\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(50000));
        }
    }

    @Nested
    @DisplayName("删除分类接口测试 DELETE /api/category/{id}")
    class DeleteTests {

        @Test
        @DisplayName("删除分类成功")
        void testDelete_Success() throws Exception {
            when(categoryService.delete(1L, 1L)).thenReturn(true);

            mockMvc.perform(delete("/api/category/1")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(true));
        }
    }
}
