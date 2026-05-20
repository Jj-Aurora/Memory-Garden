package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 预设知识包实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class StudyPack {

    /**
     * 主键
     */
    private Long id;

    /**
     * 知识包名称
     */
    private String name;

    /**
     * 知识包描述
     */
    private String description;

    /**
     * 建议分类名称
     */
    private String categoryName;

    /**
     * 包含卡片数
     */
    private Integer cardCount;

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
