package com.memorygarden.task;

import com.memorygarden.algorithm.WitherCalculator;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.model.entity.Plant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 枯萎检测定时任务
 *
 * <p>每日凌晨扫描所有未枯萎植物，判断是否逾期超过阈值，
 * 若超过则标记为枯萎状态。</p>
 *
 * @author jLU
 * @date 2026-05-20
 */
@Slf4j
@Component
public class WitherCheckTask {

    @Resource
    private PlantMapper plantMapper;

    /**
     * 每日凌晨2点执行枯萎检测
     * <p>扫描所有未枯萎且未删除的植物，计算逾期天数，
     * 若逾期天数 >= 当前间隔 × 3，则标记为枯萎。</p>
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkWithered() {
        log.info("[WitherCheck] 开始枯萎检测...");
        LocalDate today = LocalDate.now();

        // 查询所有未枯萎且未删除的植物
        List<Plant> allPlants = plantMapper.selectAllActive();

        int witheredCount = 0;
        for (Plant plant : allPlants) {
            // 计算逾期天数
            LocalDate nextReviewDate = plant.getNextReviewDate();
            if (nextReviewDate == null) {
                continue;
            }

            long overdueDays = ChronoUnit.DAYS.between(nextReviewDate, today);
            if (overdueDays <= 0) {
                continue;
            }

            // 计算当前间隔
            int currentInterval = com.memorygarden.algorithm.EbbinghausCalculator
                    .getIntervalDays(plant.getReviewRound());

            // 判断是否应该枯萎
            if (WitherCalculator.shouldWither((int) overdueDays, currentInterval)) {
                plant.setWithered(true);
                plantMapper.updateById(plant);
                witheredCount++;
                log.debug("[WitherCheck] 植物 id={} 已标记枯萎，逾期{}天，间隔{}天",
                        plant.getId(), overdueDays, currentInterval);
            }
        }

        log.info("[WitherCheck] 枯萎检测完成，共标记 {} 棵植物枯萎", witheredCount);
    }
}
