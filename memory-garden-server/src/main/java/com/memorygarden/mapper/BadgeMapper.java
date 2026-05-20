package com.memorygarden.mapper;

import com.memorygarden.model.entity.Badge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 徽章 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface BadgeMapper {

    /**
     * 查询所有徽章
     *
     * @return 徽章列表
     */
    List<Badge> selectAll();

    /**
     * 根据 ID 查询徽章
     *
     * @param id 徽章 ID
     * @return 徽章实体
     */
    Badge selectById(Long id);

    /**
     * 根据条件类型查询徽章
     *
     * @param conditionType 条件类型
     * @return 徽章列表
     */
    List<Badge> selectByConditionType(String conditionType);
}
