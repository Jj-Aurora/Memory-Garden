package com.memorygarden.model.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 复习记录实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class ReviewRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 关联知识卡片
     */
    private Long cardId;

    /**
     * 关联植物
     */
    private Long plantId;

    /**
     * 自评：1-记住了，2-模糊，3-忘记了
     */
    private Integer selfEvaluation;

    /**
     * 复习前生长阶段
     */
    private Integer prevStage;

    /**
     * 复习后生长阶段
     */
    private Integer afterStage;

    /**
     * 复习前复习轮次
     */
    private Integer prevRound;

    /**
     * 复习后复习轮次
     */
    private Integer afterRound;

    /**
     * 复习时是否处于枯萎状态
     */
    private Integer wasWithered;

    /**
     * 原计划复习日期
     */
    private LocalDate scheduledDate;

    /**
     * 实际复习日期
     */
    private LocalDate actualDate;

    /**
     * 软删除：0-未删除，1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;
}
