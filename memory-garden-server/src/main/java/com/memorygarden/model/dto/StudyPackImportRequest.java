package com.memorygarden.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 导入知识包请求 DTO
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class StudyPackImportRequest {

    /**
     * 知识包 ID
     */
    @NotNull(message = "知识包ID不能为空")
    private Long packId;
}
