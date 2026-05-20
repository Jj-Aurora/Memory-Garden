package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 徽章定义实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class Badge {

    /**
     * 主键
     */
    private Long id;

    /**
     * 徽章名称
     */
    private String name;

    /**
     * 解锁条件描述
     */
    private String description;

    /**
     * 徽章图标标识
     */
    private String icon;

    /**
     * 稀有度：0-普通，1-稀有，2-史诗
     */
    private Integer rarity;

    /**
     * 达成条件类型
     */
    private String conditionType;

    /**
     * 达成条件阈值
     */
    private Integer conditionValue;

    /**
     * 软删除：0-未删除，1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;
}
