package com.memorygarden.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 知识卡片视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class CardVO {

    /**
     * 卡片 ID
     */
    private Long id;

    /**
     * 所属分类 ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

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
     * 来源类型
     */
    private Integer sourceType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
