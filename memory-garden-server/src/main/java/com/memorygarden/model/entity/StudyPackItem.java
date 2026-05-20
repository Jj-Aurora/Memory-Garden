package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 知识包条目实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class StudyPackItem {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属知识包
     */
    private Long packId;

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
}
