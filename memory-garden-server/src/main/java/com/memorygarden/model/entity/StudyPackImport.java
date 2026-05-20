package com.memorygarden.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户导入记录实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class StudyPackImport {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 知识包
     */
    private Long packId;

    /**
     * 软删除：0-未删除，1-已删除
     */
    private Integer isDeleted;

    /**
     * 导入时间
     */
    private Date createTime;
}
