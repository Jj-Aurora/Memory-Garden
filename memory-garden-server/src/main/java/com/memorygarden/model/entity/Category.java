package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 分类实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class Category {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图标标识
     */
    private String icon;

    /**
     * 排序序号
     */
    private Integer sortOrder;

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
}
