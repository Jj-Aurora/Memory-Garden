package com.memorygarden.service;

import com.memorygarden.model.vo.BadgeVO;

import java.util.List;

/**
 * 徽章服务接口
 *
 * @author jLU
 * @date 2026-05-20
 */
public interface BadgeService {

    /**
     * 获取所有徽章列表（含获得状态）
     *
     * @param userId 用户 ID
     * @return 徽章列表
     */
    List<BadgeVO> getAllBadges(Long userId);

    /**
     * 获取已获得的徽章列表
     *
     * @param userId 用户 ID
     * @return 已获得徽章列表
     */
    List<BadgeVO> getMyBadges(Long userId);

    /**
     * 评估并授予徽章
     *
     * @param userId 用户 ID
     */
    void evaluateAndAward(Long userId);
}
