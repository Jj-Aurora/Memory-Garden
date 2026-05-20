package com.memorygarden.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 徽章视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class BadgeVO {

    /**
     * 徽章 ID
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
     * 稀有度
     */
    private Integer rarity;

    /**
     * 稀有度名称
     */
    private String rarityName;

    /**
     * 是否已获得
     */
    private Boolean earned;

    /**
     * 获得时间
     */
    private Date earnedTime;
}
