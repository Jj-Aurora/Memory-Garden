package com.memorygarden.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 创建分类请求 DTO
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class CategoryCreateRequest {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 分类图标标识
     */
    private String icon;

    /**
     * 排序序号
     */
    private Integer sortOrder;
}
