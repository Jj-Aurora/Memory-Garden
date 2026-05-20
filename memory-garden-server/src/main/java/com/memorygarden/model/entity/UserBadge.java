package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户徽章关联实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class UserBadge {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 徽章
     */
    private Long badgeId;

    /**
     * 软删除：0-未删除，1-已删除
     */
    private Integer isDeleted;

    /**
     * 获得时间
     */
    private Date createTime;
}
