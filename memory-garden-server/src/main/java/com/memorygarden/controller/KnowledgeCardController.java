package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.dto.CardCreateRequest;
import com.memorygarden.model.dto.CardUpdateRequest;
import com.memorygarden.model.vo.CardVO;
import com.memorygarden.service.KnowledgeCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 知识卡片控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/card")
@Api(tags = "知识卡片模块")
public class KnowledgeCardController {

    @Resource
    private KnowledgeCardService cardService;

    /**
     * 创建知识卡片接口。
     *
     * <p>用途：前端提交卡片信息，后端完成参数校验、落库，并自动创建关联 Plant 种子。</p>
     *
     * @param request HTTP 请求对象（用于获取当前登录用户 ID）
     * @param req     创建请求体（包含正面内容、背面内容、分类 ID、备注）
     * @return 统一返回结构，data 为新卡片 id
     */
    @PostMapping
    @ApiOperation("创建知识卡片")
    public BaseResponse<Long> create(HttpServletRequest request, @RequestBody CardCreateRequest req) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        Long id = cardService.create(userId, req);
        return BaseResponse.success(id);
    }

    /**
     * 获取卡片详情接口。
     *
     * @param id      卡片 ID
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为卡片视图对象
     */
    @GetMapping("/{id}")
    @ApiOperation("获取卡片详情")
    public BaseResponse<CardVO> getById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        CardVO vo = cardService.getById(id, userId);
        return BaseResponse.success(vo);
    }

    /**
     * 获取卡片列表接口。
     *
     * @param categoryId 分类 ID（可选筛选条件）
     * @param request    HTTP 请求对象
     * @return 统一返回结构，data 为卡片列表
     */
    @GetMapping("/list")
    @ApiOperation("获取卡片列表")
    public BaseResponse<List<CardVO>> list(@RequestParam(required = false) Long categoryId,
                                           HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<CardVO> list = cardService.list(userId, categoryId);
        return BaseResponse.success(list);
    }

    /**
     * 修改知识卡片接口。
     *
     * @param id      卡片 ID
     * @param request HTTP 请求对象
     * @param req     修改请求体
     * @return 统一返回结构，data 为是否成功
     */
    @PutMapping("/{id}")
    @ApiOperation("修改知识卡片")
    public BaseResponse<Boolean> update(@PathVariable Long id, HttpServletRequest request,
                                        @RequestBody CardUpdateRequest req) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        boolean result = cardService.update(id, userId, req);
        return BaseResponse.success(result);
    }

    /**
     * 删除知识卡片接口（软删除，同步软删除关联 Plant）。
     *
     * @param id      卡片 ID
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为是否成功
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除知识卡片")
    public BaseResponse<Boolean> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        boolean result = cardService.delete(id, userId);
        return BaseResponse.success(result);
    }
}
