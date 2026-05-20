package com.memorygarden.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 提交复习自评请求 DTO
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class ReviewSubmitRequest {

    /**
     * 知识卡片 ID
     */
    @NotNull(message = "卡片ID不能为空")
    private Long cardId;

    /**
     * 自评：1-记住了，2-模糊，3-忘记了
     */
    @NotNull(message = "自评结果不能为空")
    private Integer selfEvaluation;
}
