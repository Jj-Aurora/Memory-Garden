package com.memorygarden.service;

import com.memorygarden.model.dto.ReviewSubmitRequest;
import com.memorygarden.model.vo.ReviewSummaryVO;
import com.memorygarden.model.vo.ReviewVO;

import java.util.List;

/**
 * 复习服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface ReviewService {

    /**
     * 获取今日待复习列表
     *
     * @param userId 用户 ID
     * @return 待复习卡片列表
     */
    List<ReviewVO> getPending(Long userId);

    /**
     * 获取下一棵待复习植物详情
     *
     * @param userId 用户 ID
     * @return 复习视图对象，无待复习返回 null
     */
    ReviewVO getNext(Long userId);

    /**
     * 提交复习自评结果
     *
     * @param userId  用户 ID
     * @param request 提交请求
     * @return 是否成功
     */
    boolean submit(Long userId, ReviewSubmitRequest request);

    /**
     * 获取今日复习完成总结
     *
     * @param userId 用户 ID
     * @return 复习总结
     */
    ReviewSummaryVO getSummary(Long userId);

    /**
     * 获取待复习数量
     *
     * @param userId 用户 ID
     * @return 待复习数量
     */
    int getPendingCount(Long userId);
}
