package com.memorygarden.mapper;

import com.memorygarden.model.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface CategoryMapper {

    /**
     * 根据用户 ID 查询分类列表
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    List<Category> selectByUserId(Long userId);

    /**
     * 根据 ID 查询分类
     *
     * @param id 分类 ID
     * @return 分类实体
     */
    Category selectById(Long id);

    /**
     * 插入分类
     *
     * @param category 分类实体
     * @return 影响行数
     */
    int insert(Category category);

    /**
     * 更新分类
     *
     * @param category 分类实体
     * @return 影响行数
     */
    int updateById(Category category);
}
