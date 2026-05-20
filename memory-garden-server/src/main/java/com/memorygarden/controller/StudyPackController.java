package com.memorygarden.controller;

import com.memorygarden.common.result.BaseResponse;
import com.memorygarden.interceptor.AuthInterceptor;
import com.memorygarden.model.entity.StudyPack;
import com.memorygarden.model.entity.StudyPackItem;
import com.memorygarden.service.StudyPackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 预设知识包控制器
 *
 * @author jLU
 * @date 2026-05-20
 */
@RestController
@RequestMapping("/api/study-pack")
@Api(tags = "预设知识包模块")
public class StudyPackController {

    @Resource
    private StudyPackService studyPackService;

    /**
     * 获取可用知识包列表接口。
     *
     * @return 统一返回结构，data 为知识包列表
     */
    @GetMapping("/list")
    @ApiOperation("获取知识包列表")
    public BaseResponse<List<StudyPack>> list() {
        List<StudyPack> packs = studyPackService.list();
        return BaseResponse.success(packs);
    }

    /**
     * 获取知识包详情接口。
     *
     * @param id 知识包 ID
     * @return 统一返回结构，data 为知识包实体
     */
    @GetMapping("/{id}")
    @ApiOperation("获取知识包详情")
    public BaseResponse<StudyPack> getDetail(@PathVariable Long id) {
        StudyPack pack = studyPackService.getDetail(id);
        return BaseResponse.success(pack);
    }

    /**
     * 获取知识包条目列表接口。
     *
     * @param id 知识包 ID
     * @return 统一返回结构，data 为条目列表
     */
    @GetMapping("/{id}/items")
    @ApiOperation("获取知识包条目列表")
    public BaseResponse<List<StudyPackItem>> getPackItems(@PathVariable Long id) {
        List<StudyPackItem> items = studyPackService.getPackItems(id);
        return BaseResponse.success(items);
    }

    /**
     * 导入知识包到个人花园接口。
     *
     * <p>用途：前端提交导入请求，后端批量创建卡片和植物。</p>
     *
     * @param id      知识包 ID
     * @param request HTTP 请求对象（用于获取当前登录用户 ID）
     * @return 统一返回结构，data 为导入的卡片数量
     */
    @PostMapping("/{id}/import")
    @ApiOperation("导入知识包")
    public BaseResponse<Integer> importPack(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(AuthInterceptor.CURRENT_USER_ID);
        int count = studyPackService.importPack(userId, id);
        return BaseResponse.success(count);
    }
}
