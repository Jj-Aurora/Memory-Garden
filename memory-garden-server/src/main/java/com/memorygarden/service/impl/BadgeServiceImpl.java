package com.memorygarden.service.impl;

import com.memorygarden.algorithm.BadgeEvaluator;
import com.memorygarden.mapper.BadgeMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.UserBadgeMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.entity.Badge;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.entity.UserBadge;
import com.memorygarden.model.enums.BadgeRarity;
import com.memorygarden.model.vo.BadgeVO;
import com.memorygarden.service.BadgeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 徽章服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class BadgeServiceImpl implements BadgeService {

    private final BadgeMapper badgeMapper;
    private final UserBadgeMapper userBadgeMapper;
    private final UserMapper userMapper;
    private final PlantMapper plantMapper;

    public BadgeServiceImpl(BadgeMapper badgeMapper, UserBadgeMapper userBadgeMapper,
                            UserMapper userMapper, PlantMapper plantMapper) {
        this.badgeMapper = badgeMapper;
        this.userBadgeMapper = userBadgeMapper;
        this.userMapper = userMapper;
        this.plantMapper = plantMapper;
    }

    /**
     * 获取所有徽章列表（含获得状态）
     *
     * @param userId 用户 ID
     * @return 徽章列表
     */
    @Override
    public List<BadgeVO> getAllBadges(Long userId) {
        List<Badge> allBadges = badgeMapper.selectAll();
        List<UserBadge> earnedBadges = userBadgeMapper.selectByUserId(userId);

        List<Long> earnedBadgeIds = earnedBadges.stream()
                .map(UserBadge::getBadgeId)
                .collect(Collectors.toList());

        List<BadgeVO> result = new ArrayList<>();
        for (Badge badge : allBadges) {
            BadgeVO vo = convertToVO(badge);
            vo.setEarned(earnedBadgeIds.contains(badge.getId()));

            for (UserBadge ub : earnedBadges) {
                if (ub.getBadgeId().equals(badge.getId())) {
                    vo.setEarnedTime(ub.getCreateTime());
                    break;
                }
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取已获得的徽章列表
     *
     * @param userId 用户 ID
     * @return 已获得徽章列表
     */
    @Override
    public List<BadgeVO> getMyBadges(Long userId) {
        return getAllBadges(userId).stream()
                .filter(BadgeVO::getEarned)
                .collect(Collectors.toList());
    }

    /**
     * 评估并授予徽章
     *
     * @param userId 用户 ID
     */
    @Override
    public void evaluateAndAward(Long userId) {
        List<Badge> allBadges = badgeMapper.selectAll();
        User user = userMapper.selectById(userId);
        List<Plant> plants = plantMapper.selectByUserId(userId);
        int plantCount = (int) plants.stream().filter(p -> p.getIsDeleted() == 0).count();
        int streakDays = user != null ? user.getCurrentStreak() : 0;

        for (Badge badge : allBadges) {
            if (userBadgeMapper.existsByUserIdAndBadgeId(userId, badge.getId())) {
                continue;
            }

            int currentValue = resolveCurrentValue(badge.getConditionType(), plantCount, streakDays, plants);
            int conditionValue = badge.getConditionValue() != null ? badge.getConditionValue() : 0;
            boolean achieved = BadgeEvaluator.evaluate(badge.getConditionType(), currentValue, conditionValue);

            if (achieved) {
                UserBadge userBadge = new UserBadge();
                userBadge.setUserId(userId);
                userBadge.setBadgeId(badge.getId());
                userBadge.setIsDeleted(0);
                userBadgeMapper.insert(userBadge);
            }
        }
    }

    /**
     * 根据条件类型解析当前值
     *
     * @param conditionType 条件类型
     * @param plantCount    植物总数
     * @param streakDays    连续打卡天数
     * @param plants        植物列表
     * @return 当前值
     */
    private int resolveCurrentValue(String conditionType, int plantCount, int streakDays, List<Plant> plants) {
        if (conditionType == null) {
            return 0;
        }
        switch (conditionType) {
            case "PLANT_FIRST":
            case "TOTAL_PLANTS":
                return plantCount;
            case "STREAK_DAYS":
                return streakDays;
            case "BLOOMING_COUNT":
                return (int) plants.stream()
                        .filter(p -> p.getIsDeleted() == 0 && p.getGrowthStage() >= 4)
                        .count();
            case "FRUIT_FIRST":
                return (int) plants.stream()
                        .filter(p -> p.getIsDeleted() == 0 && p.getGrowthStage() == 5)
                        .count();
            case "REVIVE_COUNT":
                return 0;
            case "CATEGORY_COUNT":
                return 0;
            default:
                return 0;
        }
    }

    /**
     * 将 Badge 实体转换为 BadgeVO
     *
     * @param badge 徽章实体
     * @return 徽章视图对象
     */
    private BadgeVO convertToVO(Badge badge) {
        BadgeVO vo = new BadgeVO();
        BeanUtils.copyProperties(badge, vo);

        BadgeRarity rarity = BadgeRarity.fromValue(badge.getRarity());
        vo.setRarityName(rarity != null ? rarity.getName() : "未知");

        return vo;
    }
}
