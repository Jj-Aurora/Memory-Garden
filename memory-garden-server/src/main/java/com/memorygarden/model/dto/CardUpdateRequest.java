package com.memorygarden.model.dto;

import lombok.Data;

/**
 * 修改知识卡片请求 DTO
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class CardUpdateRequest {

    /**
     * 所属分类 ID
     */
    private Long categoryId;

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
}
