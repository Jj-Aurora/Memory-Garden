package com.memorygarden.mapper;

import com.memorygarden.model.entity.KnowledgeCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识卡片 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface KnowledgeCardMapper {

    /**
     * 根据 ID 查询卡片
     *
     * @param id 卡片 ID
     * @return 卡片实体
     */
    KnowledgeCard selectById(Long id);

    /**
     * 根据用户 ID 查询卡片列表
     *
     * @param userId 用户 ID
     * @return 卡片列表
     */
    List<KnowledgeCard> selectByUserId(Long userId);

    /**
     * 根据分类 ID 查询卡片列表
     *
     * @param categoryId 分类 ID
     * @return 卡片列表
     */
    List<KnowledgeCard> selectByCategoryId(Long categoryId);

    /**
     * 插入卡片
     *
     * @param card 卡片实体
     * @return 影响行数
     */
    int insert(KnowledgeCard card);

    /**
     * 更新卡片
     *
     * @param card 卡片实体
     * @return 影响行数
     */
    int updateById(KnowledgeCard card);
}
