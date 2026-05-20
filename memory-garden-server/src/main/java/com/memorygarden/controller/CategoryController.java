package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.CategoryCreateRequest;
import com.memorygarden.model.entity.Category;
import com.memorygarden.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分类控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/category")
@Api(tags = "分类模块")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 创建分类接口。
     *
     * <p>用途：前端提交分类信息，后端完成参数校验、落库，返回新分类 id。</p>
     *
     * @param request HTTP 请求对象（用于获取当前登录用户 ID）
     * @param req     创建请求体（包含分类名称、图标、排序）
     * @return 统一返回结构，data 为新分类 id
     */
    @PostMapping
    @ApiOperation("创建分类")
    public BaseResponse<Long> create(HttpServletRequest request, @RequestBody CategoryCreateRequest req) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        Long id = categoryService.create(userId, req);
        return BaseResponse.success(id);
    }

    /**
     * 获取当前用户分类列表接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为分类列表
     */
    @GetMapping("/list")
    @ApiOperation("获取分类列表")
    public BaseResponse<List<Category>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<Category> list = categoryService.list(userId);
        return BaseResponse.success(list);
    }

    /**
     * 修改分类接口。
     *
     * @param id      分类 ID
     * @param request HTTP 请求对象
     * @param req     修改请求体
     * @return 统一返回结构，data 为是否成功
     */
    @PutMapping("/{id}")
    @ApiOperation("修改分类")
    public BaseResponse<Boolean> update(@PathVariable Long id, HttpServletRequest request,
                                        @RequestBody CategoryCreateRequest req) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        boolean result = categoryService.update(id, userId, req);
        return BaseResponse.success(result);
    }

    /**
     * 删除分类接口（软删除）。
     *
     * @param id      分类 ID
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为是否成功
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除分类")
    public BaseResponse<Boolean> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        boolean result = categoryService.delete(id, userId);
        return BaseResponse.success(result);
    }
}
