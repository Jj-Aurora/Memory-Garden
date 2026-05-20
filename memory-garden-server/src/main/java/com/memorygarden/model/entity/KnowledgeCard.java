package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 知识卡片实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class KnowledgeCard {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 所属分类（可为空=未分类）
     */
    private Long categoryId;

    /**
     * 正面内容（问题）
     */
    private String frontContent;

    /**
     * 背面内容（答案）
     */
    private String backContent;

    /**
     * 备注
     */
    private String note;

    /**
     * 来源：0-手动创建，1-预设库导入
     */
    private Integer sourceType;

    /**
     * 来源知识包 ID（导入时记录）
     */
    private Long sourcePackId;

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
