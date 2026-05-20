package com.memorygarden.service;

import com.memorygarden.model.dto.CategoryCreateRequest;
import com.memorygarden.model.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface CategoryService {

    /**
     * 创建分类
     *
     * @param userId  用户 ID
     * @param request 创建请求
     * @return 新分类 ID
     */
    Long create(Long userId, CategoryCreateRequest request);

    /**
     * 获取用户分类列表
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    List<Category> list(Long userId);

    /**
     * 修改分类
     *
     * @param id      分类 ID
     * @param userId  用户 ID
     * @param request 创建请求
     * @return 是否成功
     */
    boolean update(Long id, Long userId, CategoryCreateRequest request);

    /**
     * 删除分类（软删除）
     *
     * @param id     分类 ID
     * @param userId 用户 ID
     * @return 是否成功
     */
    boolean delete(Long id, Long userId);
}
