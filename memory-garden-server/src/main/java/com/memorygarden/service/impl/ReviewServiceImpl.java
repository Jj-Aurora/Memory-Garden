package com.memorygarden.service.impl;

import com.memorygarden.algorithm.EbbinghausCalculator;
import com.memorygarden.algorithm.GrowthStageCalculator;
import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.ReviewRecordMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.dto.ReviewSubmitRequest;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.ReviewRecord;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.enums.SelfEvaluation;
import com.memorygarden.model.vo.ReviewSummaryVO;
import com.memorygarden.model.vo.ReviewVO;
import com.memorygarden.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 复习服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final PlantMapper plantMapper;
    private final KnowledgeCardMapper cardMapper;
    private final ReviewRecordMapper reviewRecordMapper;
    private final UserMapper userMapper;

    public ReviewServiceImpl(PlantMapper plantMapper, KnowledgeCardMapper cardMapper,
                             ReviewRecordMapper reviewRecordMapper, UserMapper userMapper) {
        this.plantMapper = plantMapper;
        this.cardMapper = cardMapper;
        this.reviewRecordMapper = reviewRecordMapper;
        this.userMapper = userMapper;
    }

    /**
     * 获取今日待复习列表
     *
     * @param userId 用户 ID
     * @return 待复习卡片列表
     */
    @Override
    public List<ReviewVO> getPending(Long userId) {
        List<Plant> plants = plantMapper.selectByUserId(userId);
        LocalDate today = LocalDate.now();

        return plants.stream()
                .filter(p -> p.getIsDeleted() == 0)
                .filter(p -> p.getNextReviewDate() != null && !p.getNextReviewDate().isAfter(today))
                .sorted(Comparator.comparing(Plant::getNextReviewDate))
                .map(this::convertToReviewVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取下一棵待复习植物详情
     *
     * @param userId 用户 ID
     * @return 复习视图对象
     */
    @Override
    public ReviewVO getNext(Long userId) {
        List<ReviewVO> pending = getPending(userId);
        return pending.isEmpty() ? null : pending.get(0);
    }

    /**
     * 提交复习自评结果
     *
     * @param userId  用户 ID
     * @param request 提交请求
     * @return 是否成功
     */
    @Override
    public boolean submit(Long userId, ReviewSubmitRequest request) {
        Plant plant = plantMapper.selectByCardId(request.getCardId());
        if (plant == null || plant.getIsDeleted() == 1) {
            throw new RuntimeException("植物不存在");
        }

        KnowledgeCard card = cardMapper.selectById(request.getCardId());
        if (card == null || card.getIsDeleted() == 1) {
            throw new RuntimeException("卡片不存在");
        }

        int evalValue = request.getSelfEvaluation();
        SelfEvaluation evaluation = SelfEvaluation.fromValue(evalValue);
        if (evaluation == null) {
            throw new RuntimeException("无效的自评值");
        }

        int stageBefore = plant.getGrowthStage();
        int roundBefore = plant.getReviewRound();
        boolean wasWithered = plant.getIsWithered() == 1;
        LocalDate oldNextReviewDate = plant.getNextReviewDate();

        int newStage = GrowthStageCalculator.calculateNextStage(stageBefore, evaluation, wasWithered);

        int newRound;
        if (evaluation == SelfEvaluation.REMEMBERED) {
            newRound = roundBefore + 1;
        } else if (evaluation == SelfEvaluation.VAGUE) {
            newRound = roundBefore;
        } else {
            newRound = Math.max(1, roundBefore - 1);
        }

        int effectiveRound = newRound;
        if (wasWithered && evaluation == SelfEvaluation.REMEMBERED) {
            if (oldNextReviewDate != null) {
                long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                        oldNextReviewDate, LocalDate.now());
                effectiveRound = EbbinghausCalculator.calcEffectiveRound(roundBefore, (int) overdueDays);
            }
            plant.setIsWithered(0);
        }

        if (evaluation == SelfEvaluation.FORGOTTEN && wasWithered) {
            plant.setIsWithered(1);
        }

        int intervalDays = EbbinghausCalculator.getIntervalDays(effectiveRound);
        LocalDate nextReviewDate = LocalDate.now().plusDays(intervalDays);

        plant.setGrowthStage(newStage);
        plant.setReviewRound(newRound);
        plant.setTotalReviewCount(plant.getTotalReviewCount() + 1);
        plant.setNextReviewDate(nextReviewDate);
        plantMapper.updateById(plant);

        ReviewRecord record = new ReviewRecord();
        record.setPlantId(plant.getId());
        record.setCardId(request.getCardId());
        record.setUserId(userId);
        record.setSelfEvaluation(evalValue);
        record.setPrevStage(stageBefore);
        record.setAfterStage(newStage);
        record.setPrevRound(roundBefore);
        record.setAfterRound(newRound);
        record.setWasWithered(wasWithered ? 1 : 0);
        record.setScheduledDate(oldNextReviewDate);
        record.setActualDate(LocalDate.now());
        record.setIsDeleted(0);
        reviewRecordMapper.insert(record);

        updateCheckIn(userId);

        return true;
    }

    /**
     * 获取今日复习完成总结
     *
     * @param userId 用户 ID
     * @return 复习总结
     */
    @Override
    public ReviewSummaryVO getSummary(Long userId) {
        List<ReviewRecord> records = reviewRecordMapper.selectByUserIdAndDate(userId, LocalDate.now());

        ReviewSummaryVO summary = new ReviewSummaryVO();
        summary.setTotalReviewed(records.size());
        summary.setRememberedCount((int) records.stream().filter(r -> r.getSelfEvaluation() == 1).count());
        summary.setVagueCount((int) records.stream().filter(r -> r.getSelfEvaluation() == 2).count());
        summary.setForgottenCount((int) records.stream().filter(r -> r.getSelfEvaluation() == 3).count());
        summary.setUpgradedCount((int) records.stream().filter(r -> r.getAfterStage() > r.getPrevStage()).count());
        summary.setDowngradedCount((int) records.stream().filter(r -> r.getAfterStage() < r.getPrevStage()).count());
        summary.setRemainingCount(getPendingCount(userId));

        return summary;
    }

    /**
     * 获取待复习数量
     *
     * @param userId 用户 ID
     * @return 待复习数量
     */
    @Override
    public int getPendingCount(Long userId) {
        return getPending(userId).size();
    }

    /**
     * 更新打卡天数
     *
     * @param userId 用户 ID
     */
    private void updateCheckIn(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate lastCheckIn = user.getLastCheckIn();

        if (lastCheckIn == null || lastCheckIn.isBefore(today)) {
            if (lastCheckIn != null && lastCheckIn.plusDays(1).equals(today)) {
                user.setCurrentStreak(user.getCurrentStreak() + 1);
            } else {
                user.setCurrentStreak(1);
            }

            if (user.getCurrentStreak() > user.getMaxStreak()) {
                user.setMaxStreak(user.getCurrentStreak());
            }

            user.setLastCheckIn(today);
            userMapper.updateById(user);
        }
    }

    /**
     * 将 Plant 转换为 ReviewVO
     *
     * @param plant 植物实体
     * @return 复习视图对象
     */
    private ReviewVO convertToReviewVO(Plant plant) {
        KnowledgeCard card = cardMapper.selectById(plant.getCardId());

        ReviewVO vo = new ReviewVO();
        vo.setCardId(plant.getCardId());
        vo.setGrowthStage(plant.getGrowthStage());
        vo.setWithered(plant.getIsWithered() == 1);
        vo.setReviewRound(plant.getReviewRound());
        vo.setScheduledDate(plant.getNextReviewDate());

        if (card != null) {
            vo.setFrontContent(card.getFrontContent());
            vo.setBackContent(card.getBackContent());
            vo.setNote(card.getNote());
        }

        return vo;
    }
}
