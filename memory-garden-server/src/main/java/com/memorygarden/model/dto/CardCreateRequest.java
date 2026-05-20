package com.memorygarden.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 创建知识卡片请求 DTO
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class CardCreateRequest {

    /**
     * 所属分类 ID（可为空=未分类）
     */
    private Long categoryId;

    /**
     * 正面内容（问题）
     */
    @NotBlank(message = "正面内容不能为空")
    private String frontContent;

    /**
     * 背面内容（答案）
     */
    @NotBlank(message = "背面内容不能为空")
    private String backContent;

    /**
     * 备注
     */
    private String note;
}
