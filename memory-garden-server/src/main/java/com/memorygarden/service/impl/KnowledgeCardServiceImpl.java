package com.memorygarden.service.impl;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.model.dto.CardCreateRequest;
import com.memorygarden.model.dto.CardUpdateRequest;
import com.memorygarden.model.entity.Category;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.vo.CardVO;
import com.memorygarden.service.KnowledgeCardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识卡片服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class KnowledgeCardServiceImpl implements KnowledgeCardService {

    private final KnowledgeCardMapper cardMapper;
    private final PlantMapper plantMapper;
    private final CategoryMapper categoryMapper;

    public KnowledgeCardServiceImpl(KnowledgeCardMapper cardMapper, PlantMapper plantMapper, CategoryMapper categoryMapper) {
        this.cardMapper = cardMapper;
        this.plantMapper = plantMapper;
        this.categoryMapper = categoryMapper;
    }

    /**
     * 创建知识卡片（自动创建关联 Plant）
     *
     * @param userId  用户 ID
     * @param request 创建请求
     * @return 新卡片 ID
     */
    @Override
    public Long create(Long userId, CardCreateRequest request) {
        if (request.getFrontContent() == null || request.getFrontContent().trim().isEmpty()) {
            throw new RuntimeException("正面内容不能为空");
        }
        if (request.getBackContent() == null || request.getBackContent().trim().isEmpty()) {
            throw new RuntimeException("背面内容不能为空");
        }

        if (request.getCategoryId() != null) {
            Category category = categoryMapper.selectById(request.getCategoryId());
            if (category == null || category.getIsDeleted() == 1) {
                throw new RuntimeException("分类不存在");
            }
        }

        KnowledgeCard card = new KnowledgeCard();
        card.setUserId(userId);
        card.setCategoryId(request.getCategoryId());
        card.setFrontContent(request.getFrontContent());
        card.setBackContent(request.getBackContent());
        card.setNote(request.getNote());
        card.setSourceType(0);
        card.setIsDeleted(0);

        cardMapper.insert(card);

        Plant plant = new Plant();
        plant.setCardId(card.getId());
        plant.setUserId(userId);
        plant.setGrowthStage(1);
        plant.setReviewRound(1);
        plant.setNextReviewDate(LocalDate.now());
        plant.setIsWithered(0);
        plant.setIsDeleted(0);

        plantMapper.insert(plant);

        return card.getId();
    }

    /**
     * 根据 ID 获取卡片详情
     *
     * @param id     卡片 ID
     * @param userId 用户 ID
     * @return 卡片视图对象
     */
    @Override
    public CardVO getById(Long id, Long userId) {
        KnowledgeCard card = cardMapper.selectById(id);
        if (card == null || card.getIsDeleted() == 1) {
            throw new RuntimeException("卡片不存在");
        }

        CardVO vo = new CardVO();
        BeanUtils.copyProperties(card, vo);

        if (card.getCategoryId() != null) {
            Category category = categoryMapper.selectById(card.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        return vo;
    }

    /**
     * 获取卡片列表
     *
     * @param userId     用户 ID
     * @param categoryId 分类 ID（可选）
     * @return 卡片列表
     */
    @Override
    public List<CardVO> list(Long userId, Long categoryId) {
        List<KnowledgeCard> cards;
        if (categoryId != null) {
            cards = cardMapper.selectByCategoryId(categoryId);
        } else {
            cards = cardMapper.selectByUserId(userId);
        }

        List<CardVO> result = new ArrayList<>();
        for (KnowledgeCard card : cards) {
            if (card.getIsDeleted() == 1) {
                continue;
            }
            CardVO vo = new CardVO();
            BeanUtils.copyProperties(card, vo);

            if (card.getCategoryId() != null) {
                Category category = categoryMapper.selectById(card.getCategoryId());
                if (category != null) {
                    vo.setCategoryName(category.getName());
                }
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 修改知识卡片
     *
     * @param id      卡片 ID
     * @param userId  用户 ID
     * @param request 修改请求
     * @return 是否成功
     */
    @Override
    public boolean update(Long id, Long userId, CardUpdateRequest request) {
        KnowledgeCard card = cardMapper.selectById(id);
        if (card == null || card.getIsDeleted() == 1) {
            throw new RuntimeException("卡片不存在");
        }
        if (!card.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }

        if (request.getCategoryId() != null) {
            card.setCategoryId(request.getCategoryId());
        }
        if (request.getFrontContent() != null) {
            card.setFrontContent(request.getFrontContent());
        }
        if (request.getBackContent() != null) {
            card.setBackContent(request.getBackContent());
        }
        if (request.getNote() != null) {
            card.setNote(request.getNote());
        }

        return cardMapper.updateById(card) > 0;
    }

    /**
     * 删除知识卡片（软删除，同步软删除关联 Plant）
     *
     * @param id     卡片 ID
     * @param userId 用户 ID
     * @return 是否成功
     */
    @Override
    public boolean delete(Long id, Long userId) {
        KnowledgeCard card = cardMapper.selectById(id);
        if (card == null || card.getIsDeleted() == 1) {
            throw new RuntimeException("卡片不存在");
        }
        if (!card.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }

        card.setIsDeleted(1);
        cardMapper.updateById(card);

        Plant plant = plantMapper.selectByCardId(id);
        if (plant != null) {
            plant.setIsDeleted(1);
            plantMapper.updateById(plant);
        }

        return true;
    }
}
