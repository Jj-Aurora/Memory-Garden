package com.memorygarden.service.impl;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.model.dto.CategoryCreateRequest;
import com.memorygarden.model.entity.Category;
import com.memorygarden.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 创建分类
     *
     * @param userId  用户 ID
     * @param request 创建请求
     * @return 新分类 ID
     */
    @Override
    public Long create(Long userId, CategoryCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("分类名称不能为空");
        }

        Category category = new Category();
        category.setUserId(userId);
        category.setName(request.getName());
        category.setIcon(request.getIcon());
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setIsDeleted(0);

        categoryMapper.insert(category);
        return category.getId();
    }

    /**
     * 获取用户分类列表
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    @Override
    public List<Category> list(Long userId) {
        return categoryMapper.selectByUserId(userId);
    }

    /**
     * 修改分类
     *
     * @param id      分类 ID
     * @param userId  用户 ID
     * @param request 修改请求
     * @return 是否成功
     */
    @Override
    public boolean update(Long id, Long userId, CategoryCreateRequest request) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }

        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }

        return categoryMapper.updateById(category) > 0;
    }

    /**
     * 删除分类（软删除）
     *
     * @param id     分类 ID
     * @param userId 用户 ID
     * @return 是否成功
     */
    @Override
    public boolean delete(Long id, Long userId) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        if (!category.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }

        category.setIsDeleted(1);
        return categoryMapper.updateById(category) > 0;
    }
}
