package com.memorygarden.service;

import com.memorygarden.model.vo.GardenVO;
import com.memorygarden.model.vo.PlantVO;

import java.util.List;

/**
 * 植物服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface PlantService {

    /**
     * 获取花园视图数据
     *
     * @param userId 用户 ID
     * @return 花园视图
     */
    GardenVO getGardenView(Long userId);

    /**
     * 按条件筛选植物
     *
     * @param userId     用户 ID
     * @param categoryId 分类 ID（可选）
     * @param stage      生长阶段（可选）
     * @param withered   是否枯萎（可选）
     * @return 植物列表
     */
    List<PlantVO> filter(Long userId, Long categoryId, Integer stage, Boolean withered);

    /**
     * 按条件排序植物
     *
     * @param userId 用户 ID
     * @param sortBy 排序字段（createTime/nextReviewDate/growthStage）
     * @param order  排序方向（asc/desc）
     * @return 植物列表
     */
    List<PlantVO> sort(Long userId, String sortBy, String order);

    /**
     * 获取枯萎植物列表
     *
     * @param userId 用户 ID
     * @return 枯萎植物列表
     */
    List<PlantVO> getWithered(Long userId);
}
