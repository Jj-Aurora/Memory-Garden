package com.memorygarden.controller;

import com.memorygarden.common.exception.GlobalExceptionHandler;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.vo.GardenVO;
import com.memorygarden.model.vo.PlantVO;
import com.memorygarden.service.PlantService;
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
 * GardenController 接口测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GardenController 接口测试")
class GardenControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlantService plantService;

    @InjectMocks
    private GardenController gardenController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gardenController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("获取花园视图接口测试 GET /api/garden")
    class GetGardenViewTests {

        @Test
        @DisplayName("获取花园视图成功")
        void testGetGardenView_Success() throws Exception {
            GardenVO vo = new GardenVO();
            vo.setTotalCount(5);
            vo.setWitheredCount(1);
            when(plantService.getGardenView(1L)).thenReturn(vo);

            mockMvc.perform(get("/api/garden")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(0))
                    .andExpect(jsonPath("$.data.totalCount").value(5));
        }
    }

    @Nested
    @DisplayName("筛选植物接口测试 GET /api/garden/filter")
    class FilterTests {

        @Test
        @DisplayName("按分类筛选植物")
        void testFilter_ByCategory() throws Exception {
            PlantVO vo = new PlantVO();
            vo.setId(1L);
            vo.setGrowthStage(1);
            when(plantService.filter(1L, 2L, null, null)).thenReturn(Arrays.asList(vo));

            mockMvc.perform(get("/api/garden/filter")
                            .param("categoryId", "2")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }

        @Test
        @DisplayName("按枯萎状态筛选")
        void testFilter_ByWithered() throws Exception {
            when(plantService.filter(1L, null, null, true)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/garden/filter")
                            .param("withered", "true")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(0));
        }
    }

    @Nested
    @DisplayName("排序植物接口测试 GET /api/garden/sort")
    class SortTests {

        @Test
        @DisplayName("按创建时间排序")
        void testSort_ByCreateTime() throws Exception {
            PlantVO vo = new PlantVO();
            vo.setId(1L);
            when(plantService.sort(1L, "createTime", "desc")).thenReturn(Arrays.asList(vo));

            mockMvc.perform(get("/api/garden/sort")
                            .param("sortBy", "createTime")
                            .param("order", "desc")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }
    }

    @Nested
    @DisplayName("获取枯萎植物接口测试 GET /api/garden/withered")
    class WitheredTests {

        @Test
        @DisplayName("获取枯萎植物列表")
        void testGetWithered_Success() throws Exception {
            PlantVO vo = new PlantVO();
            vo.setId(1L);
            vo.setWithered(true);
            when(plantService.getWithered(1L)).thenReturn(Arrays.asList(vo));

            mockMvc.perform(get("/api/garden/withered")
                            .requestAttr(AuthInterceptor.CURRENT_USER_ID, 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(1));
        }
    }
}
