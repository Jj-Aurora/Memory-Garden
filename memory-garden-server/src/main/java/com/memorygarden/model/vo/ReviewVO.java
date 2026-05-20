package com.memorygarden.model.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 复习视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class ReviewVO {

    /**
     * 卡片 ID
     */
    private Long cardId;

    /**
     * 正面内容
     */
    private String frontContent;

    /**
     * 背面内容
     */
    private String backContent;

    /**
     * 备注
     */
    private String note;

    /**
     * 当前生长阶段
     */
    private Integer growthStage;

    /**
     * 是否枯萎
     */
    private Boolean withered;

    /**
     * 当前复习轮次
     */
    private Integer reviewRound;

    /**
     * 原计划复习日期
     */
    private LocalDate scheduledDate;
}
