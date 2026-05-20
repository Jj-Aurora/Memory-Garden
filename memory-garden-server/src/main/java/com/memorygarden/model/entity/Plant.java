package com.memorygarden.model.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 植物实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class Plant {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 关联知识卡片（1:1）
     */
    private Long cardId;

    /**
     * 生长阶段：1-种子，2-发芽，3-成长，4-开花，5-结果
     */
    private Integer growthStage;

    /**
     * 是否枯萎：0-否，1-是
     */
    private Integer isWithered;

    /**
     * 当前阶段成功复习次数
     */
    private Integer stageSuccessCount;

    /**
     * 总复习次数
     */
    private Integer totalReviewCount;

    /**
     * 当前艾宾浩斯复习轮次（0=未复习过）
     */
    private Integer reviewRound;

    /**
     * 下次应复习日期
     */
    private LocalDate nextReviewDate;

    /**
     * 最后复习日期
     */
    private LocalDate lastReviewDate;

    /**
     * 软删除：0-未删除，1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 判断植物是否枯萎
     *
     * @return true=枯萎
     */
    public boolean isWithered() {
        return isWithered != null && isWithered == 1;
    }

    /**
     * 设置枯萎状态
     *
     * @param withered true=枯萎，false=恢复
     */
    public void setWithered(boolean withered) {
        this.isWithered = withered ? 1 : 0;
    }

    public Plant() {
        this.growthStage = 1;
        this.isWithered = 0;
        this.stageSuccessCount = 0;
        this.totalReviewCount = 0;
        this.reviewRound = 0;
        this.isDeleted = 0;
    }
}
