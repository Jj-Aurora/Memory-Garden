package com.memorygarden.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 花园视图对象（含分组统计）
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class GardenVO {

    /**
     * 所有植物列表
     */
    private List<PlantVO> plants;

    /**
     * 各阶段植物数量统计
     */
    private Map<Integer, Integer> stageCount;

    /**
     * 枯萎植物数量
     */
    private Integer witheredCount;

    /**
     * 植物总数
     */
    private Integer totalCount;
}
