package com.memorygarden.model.vo;

import lombok.Data;

/**
 * 复习完成总结视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class ReviewSummaryVO {

    /**
     * 本次复习总数
     */
    private Integer totalReviewed;

    /**
     * 记住了数量
     */
    private Integer rememberedCount;

    /**
     * 模糊数量
     */
    private Integer vagueCount;

    /**
     * 忘记了数量
     */
    private Integer forgottenCount;

    /**
     * 升级数量
     */
    private Integer upgradedCount;

    /**
     * 回退数量
     */
    private Integer downgradedCount;

    /**
     * 待复习剩余数
     */
    private Integer remainingCount;
}
