package com.memorygarden.model.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 植物视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class PlantVO {

    /**
     * 植物 ID
     */
    private Long id;

    /**
     * 关联卡片 ID
     */
    private Long cardId;

    /**
     * 卡片正面内容（简要）
     */
    private String cardFrontContent;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 生长阶段
     */
    private Integer growthStage;

    /**
     * 生长阶段名称
     */
    private String growthStageName;

    /**
     * 是否枯萎
     */
    private Boolean withered;

    /**
     * 当前复习轮次
     */
    private Integer reviewRound;

    /**
     * 下次复习日期
     */
    private LocalDate nextReviewDate;

    /**
     * 总复习次数
     */
    private Integer totalReviewCount;
}
