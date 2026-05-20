package com.memorygarden.service.impl;

import com.memorygarden.mapper.CategoryMapper;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.model.entity.Category;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.enums.GrowthStage;
import com.memorygarden.model.vo.GardenVO;
import com.memorygarden.model.vo.PlantVO;
import com.memorygarden.service.PlantService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 植物服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class PlantServiceImpl implements PlantService {

    private final PlantMapper plantMapper;
    private final KnowledgeCardMapper cardMapper;
    private final CategoryMapper categoryMapper;

    public PlantServiceImpl(PlantMapper plantMapper, KnowledgeCardMapper cardMapper, CategoryMapper categoryMapper) {
        this.plantMapper = plantMapper;
        this.cardMapper = cardMapper;
        this.categoryMapper = categoryMapper;
    }

    /**
     * 获取花园视图数据
     *
     * @param userId 用户 ID
     * @return 花园视图
     */
    @Override
    public GardenVO getGardenView(Long userId) {
        List<Plant> plants = plantMapper.selectByUserId(userId);

        List<Plant> activePlants = plants.stream()
                .filter(p -> p.getIsDeleted() == 0)
                .collect(Collectors.toList());

        Map<Integer, Integer> stageCount = new HashMap<>();
        int witheredCount = 0;

        for (Plant plant : activePlants) {
            if (plant.getIsWithered() == 1) {
                witheredCount++;
            }
            int stage = plant.getGrowthStage();
            stageCount.merge(stage, 1, Integer::sum);
        }

        List<PlantVO> plantVOs = activePlants.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        GardenVO vo = new GardenVO();
        vo.setPlants(plantVOs);
        vo.setStageCount(stageCount);
        vo.setWitheredCount(witheredCount);
        vo.setTotalCount(activePlants.size());

        return vo;
    }

    /**
     * 按条件筛选植物
     *
     * @param userId     用户 ID
     * @param categoryId 分类 ID（可选）
     * @param stage      生长阶段（可选）
     * @param withered   是否枯萎（可选）
     * @return 植物列表
     */
    @Override
    public List<PlantVO> filter(Long userId, Long categoryId, Integer stage, Boolean withered) {
        List<Plant> plants = plantMapper.selectByUserId(userId);

        return plants.stream()
                .filter(p -> p.getIsDeleted() == 0)
                .filter(p -> stage == null || p.getGrowthStage().equals(stage))
                .filter(p -> withered == null || (withered ? p.getIsWithered() == 1 : p.getIsWithered() == 0))
                .filter(p -> {
                    if (categoryId == null) return true;
                    KnowledgeCard card = cardMapper.selectById(p.getCardId());
                    return card != null && categoryId.equals(card.getCategoryId());
                })
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 按条件排序植物
     *
     * @param userId 用户 ID
     * @param sortBy 排序字段
     * @param order  排序方向
     * @return 植物列表
     */
    @Override
    public List<PlantVO> sort(Long userId, String sortBy, String order) {
        List<Plant> plants = plantMapper.selectByUserId(userId);

        Comparator<Plant> comparator;
        switch (sortBy != null ? sortBy : "createTime") {
            case "growthStage":
                comparator = Comparator.comparingInt(Plant::getGrowthStage);
                break;
            case "nextReviewDate":
                comparator = Comparator.comparing(p -> p.getNextReviewDate() != null ? p.getNextReviewDate() : java.time.LocalDate.MAX);
                break;
            default:
                comparator = Comparator.comparingLong(Plant::getId);
                break;
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return plants.stream()
                .filter(p -> p.getIsDeleted() == 0)
                .sorted(comparator)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取枯萎植物列表
     *
     * @param userId 用户 ID
     * @return 枯萎植物列表
     */
    @Override
    public List<PlantVO> getWithered(Long userId) {
        List<Plant> witheredPlants = plantMapper.selectWithered(userId);

        return witheredPlants.stream()
                .filter(p -> p.getIsDeleted() == 0)
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 将 Plant 实体转换为 PlantVO
     *
     * @param plant 植物实体
     * @return 植物视图对象
     */
    private PlantVO convertToVO(Plant plant) {
        PlantVO vo = new PlantVO();
        vo.setId(plant.getId());
        vo.setCardId(plant.getCardId());
        vo.setGrowthStage(plant.getGrowthStage());
        vo.setWithered(plant.getIsWithered() == 1);
        vo.setReviewRound(plant.getReviewRound());
        vo.setNextReviewDate(plant.getNextReviewDate());

        GrowthStage gs = GrowthStage.fromValue(plant.getGrowthStage());
        vo.setGrowthStageName(gs != null ? gs.getName() : "未知");

        KnowledgeCard card = cardMapper.selectById(plant.getCardId());
        if (card != null) {
            vo.setCardFrontContent(card.getFrontContent());
            if (card.getCategoryId() != null) {
                Category cat = categoryMapper.selectById(card.getCategoryId());
                if (cat != null) {
                    vo.setCategoryName(cat.getName());
                }
            }
        }

        return vo;
    }
}
