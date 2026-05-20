package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.vo.GardenVO;
import com.memorygarden.model.vo.PlantVO;
import com.memorygarden.service.PlantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 花园控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/garden")
@Api(tags = "花园模块")
public class GardenController {

    @Resource
    private PlantService plantService;

    /**
     * 获取花园视图数据接口。
     *
     * <p>用途：前端获取花园主页数据，包含所有植物和分组统计。</p>
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为花园视图对象
     */
    @GetMapping
    @ApiOperation("获取花园视图数据")
    public BaseResponse<GardenVO> getGardenView(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        GardenVO vo = plantService.getGardenView(userId);
        return BaseResponse.success(vo);
    }

    /**
     * 按条件筛选植物接口。
     *
     * @param categoryId 分类 ID（可选）
     * @param stage      生长阶段（可选）
     * @param withered   是否枯萎（可选）
     * @param request    HTTP 请求对象
     * @return 统一返回结构，data 为植物列表
     */
    @GetMapping("/filter")
    @ApiOperation("按条件筛选植物")
    public BaseResponse<List<PlantVO>> filter(@RequestParam(required = false) Long categoryId,
                                              @RequestParam(required = false) Integer stage,
                                              @RequestParam(required = false) Boolean withered,
                                              HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<PlantVO> list = plantService.filter(userId, categoryId, stage, withered);
        return BaseResponse.success(list);
    }

    /**
     * 按条件排序植物接口。
     *
     * @param sortBy  排序字段（createTime/nextReviewDate/growthStage）
     * @param order   排序方向（asc/desc）
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为植物列表
     */
    @GetMapping("/sort")
    @ApiOperation("按条件排序植物")
    public BaseResponse<List<PlantVO>> sort(@RequestParam(defaultValue = "createTime") String sortBy,
                                            @RequestParam(defaultValue = "asc") String order,
                                            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<PlantVO> list = plantService.sort(userId, sortBy, order);
        return BaseResponse.success(list);
    }

    /**
     * 获取枯萎植物列表接口。
     *
     * @param request HTTP 请求对象
     * @return 统一返回结构，data 为枯萎植物列表
     */
    @GetMapping("/withered")
    @ApiOperation("获取枯萎植物列表")
    public BaseResponse<List<PlantVO>> getWithered(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        List<PlantVO> list = plantService.getWithered(userId);
        return BaseResponse.success(list);
    }
}
