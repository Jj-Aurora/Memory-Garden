package com.memorygarden.service.impl;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.model.dto.CategoryCreateRequest;
import com.memorygarden.model.entity.Category;
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
import static org.mockito.Mockito.*;

/**
 * CategoryServiceImpl 全面单元测试
 *
 * @author jLU
 * @date 2026-05-20
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryCreateRequest createRequest;
    private Category existingCategory;

    @BeforeEach
    void setUp() {
        createRequest = new CategoryCreateRequest();
        createRequest.setName("英语");
        createRequest.setIcon("icon-english");
        createRequest.setSortOrder(1);

        existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setUserId(100L);
        existingCategory.setName("英语");
        existingCategory.setIcon("icon-english");
        existingCategory.setSortOrder(1);
        existingCategory.setIsDeleted(0);
    }

    @Nested
    @DisplayName("创建分类测试")
    class CreateTests {

        @Test
        @DisplayName("创建分类成功-字段映射正确")
        void testCreate_Success() {
            when(categoryMapper.insert(any(Category.class))).thenAnswer(invocation -> {
                Category cat = invocation.getArgument(0);
                cat.setId(1L);
                return 1;
            });

            Long id = categoryService.create(100L, createRequest);

            assertNotNull(id);
            verify(categoryMapper).insert(argThat(cat ->
                    cat.getUserId().equals(100L) &&
                    cat.getName().equals("英语") &&
                    cat.getIcon().equals("icon-english") &&
                    cat.getSortOrder() == 1 &&
                    cat.getIsDeleted() == 0
            ));
        }

        @Test
        @DisplayName("创建分类-名称为null抛异常")
        void testCreate_NullName() {
            createRequest.setName(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("分类名称不能为空"));
            verify(categoryMapper, never()).insert(any());
        }

        @Test
        @DisplayName("创建分类-名称为空字符串抛异常")
        void testCreate_EmptyName() {
            createRequest.setName("");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("分类名称不能为空"));
        }

        @Test
        @DisplayName("创建分类-名称为纯空格抛异常")
        void testCreate_BlankName() {
            createRequest.setName("   ");

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.create(100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("分类名称不能为空"));
        }

        @Test
        @DisplayName("创建分类-未指定排序默认为0")
        void testCreate_DefaultSortOrder() {
            createRequest.setSortOrder(null);
            when(categoryMapper.insert(any(Category.class))).thenAnswer(invocation -> {
                Category cat = invocation.getArgument(0);
                cat.setId(1L);
                return 1;
            });

            categoryService.create(100L, createRequest);

            verify(categoryMapper).insert(argThat(cat -> cat.getSortOrder() == 0));
        }
    }

    @Nested
    @DisplayName("获取分类列表测试")
    class ListTests {

        @Test
        @DisplayName("获取分类列表-有数据")
        void testList_WithData() {
            when(categoryMapper.selectByUserId(100L)).thenReturn(Arrays.asList(existingCategory));

            List<Category> list = categoryService.list(100L);

            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals("英语", list.get(0).getName());
        }

        @Test
        @DisplayName("获取分类列表-无数据返回空列表")
        void testList_Empty() {
            when(categoryMapper.selectByUserId(100L)).thenReturn(Collections.emptyList());

            List<Category> list = categoryService.list(100L);

            assertNotNull(list);
            assertEquals(0, list.size());
        }
    }

    @Nested
    @DisplayName("修改分类测试")
    class UpdateTests {

        @Test
        @DisplayName("修改分类成功-字段更新正确")
        void testUpdate_Success() {
            when(categoryMapper.selectById(1L)).thenReturn(existingCategory);
            when(categoryMapper.updateById(any(Category.class))).thenReturn(1);

            CategoryCreateRequest updateReq = new CategoryCreateRequest();
            updateReq.setName("日语");
            updateReq.setIcon("icon-japanese");
            updateReq.setSortOrder(2);

            boolean result = categoryService.update(1L, 100L, updateReq);

            assertTrue(result);
            verify(categoryMapper).updateById(argThat(cat ->
                    cat.getName().equals("日语") &&
                    cat.getIcon().equals("icon-japanese") &&
                    cat.getSortOrder() == 2
            ));
        }

        @Test
        @DisplayName("修改分类-部分字段更新")
        void testUpdate_PartialFields() {
            when(categoryMapper.selectById(1L)).thenReturn(existingCategory);
            when(categoryMapper.updateById(any(Category.class))).thenReturn(1);

            CategoryCreateRequest updateReq = new CategoryCreateRequest();
            updateReq.setName("日语");

            categoryService.update(1L, 100L, updateReq);

            verify(categoryMapper).updateById(argThat(cat ->
                    cat.getName().equals("日语") &&
                    cat.getIcon().equals("icon-english") &&
                    cat.getSortOrder() == 1
            ));
        }

        @Test
        @DisplayName("修改分类-不存在抛异常")
        void testUpdate_NotFound() {
            when(categoryMapper.selectById(999L)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.update(999L, 100L, createRequest);
            });

            assertTrue(exception.getMessage().contains("分类不存在"));
        }

        @Test
        @DisplayName("修改分类-非本人分类抛异常（权限控制）")
        void testUpdate_NotOwner() {
            when(categoryMapper.selectById(1L)).thenReturn(existingCategory);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.update(1L, 200L, createRequest);
            });

            assertTrue(exception.getMessage().contains("无权操作"));
            verify(categoryMapper, never()).updateById(any());
        }
    }

    @Nested
    @DisplayName("删除分类测试")
    class DeleteTests {

        @Test
        @DisplayName("删除分类-软删除设置isDeleted=1")
        void testDelete_Success() {
            when(categoryMapper.selectById(1L)).thenReturn(existingCategory);
            when(categoryMapper.updateById(any(Category.class))).thenReturn(1);

            boolean result = categoryService.delete(1L, 100L);

            assertTrue(result);
            verify(categoryMapper).updateById(argThat(cat -> cat.getIsDeleted() == 1));
        }

        @Test
        @DisplayName("删除分类-不存在抛异常")
        void testDelete_NotFound() {
            when(categoryMapper.selectById(999L)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.delete(999L, 100L);
            });

            assertTrue(exception.getMessage().contains("分类不存在"));
        }

        @Test
        @DisplayName("删除分类-非本人抛异常（权限控制）")
        void testDelete_NotOwner() {
            when(categoryMapper.selectById(1L)).thenReturn(existingCategory);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoryService.delete(1L, 200L);
            });

            assertTrue(exception.getMessage().contains("无权操作"));
            verify(categoryMapper, never()).updateById(any());
        }

        @Test
        @DisplayName("删除分类-数据库更新失败返回false")
        void testDelete_UpdateFailed() {
            when(categoryMapper.selectById(1L)).thenReturn(existingCategory);
            when(categoryMapper.updateById(any(Category.class))).thenReturn(0);

            boolean result = categoryService.delete(1L, 100L);

            assertFalse(result);
        }
    }
}
