package com.memorygarden.service;

import com.memorygarden.model.dto.CardCreateRequest;
import com.memorygarden.model.dto.CardUpdateRequest;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.vo.CardVO;

import java.util.List;

/**
 * 知识卡片服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface KnowledgeCardService {

    /**
     * 创建知识卡片（自动创建关联 Plant）
     *
     * @param userId  用户 ID
     * @param request 创建请求
     * @return 新卡片 ID
     */
    Long create(Long userId, CardCreateRequest request);

    /**
     * 根据 ID 获取卡片详情
     *
     * @param id     卡片 ID
     * @param userId 用户 ID
     * @return 卡片视图对象
     */
    CardVO getById(Long id, Long userId);

    /**
     * 获取卡片列表
     *
     * @param userId     用户 ID
     * @param categoryId 分类 ID（可选）
     * @return 卡片列表
     */
    List<CardVO> list(Long userId, Long categoryId);

    /**
     * 修改知识卡片
     *
     * @param id      卡片 ID
     * @param userId  用户 ID
     * @param request 修改请求
     * @return 是否成功
     */
    boolean update(Long id, Long userId, CardUpdateRequest request);

    /**
     * 删除知识卡片（软删除，同步软删除关联 Plant）
     *
     * @param id     卡片 ID
     * @param userId 用户 ID
     * @return 是否成功
     */
    boolean delete(Long id, Long userId);
}
