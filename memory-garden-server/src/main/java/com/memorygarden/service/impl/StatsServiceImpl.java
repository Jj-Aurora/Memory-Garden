package com.memorygarden.service.impl;

import com.memorygarden.mapper.KnowledgeCardMapper;
import com.memorygarden.mapper.PlantMapper;
import com.memorygarden.mapper.ReviewRecordMapper;
import com.memorygarden.mapper.UserMapper;
import com.memorygarden.model.entity.KnowledgeCard;
import com.memorygarden.model.entity.Plant;
import com.memorygarden.model.entity.ReviewRecord;
import com.memorygarden.model.entity.User;
import com.memorygarden.model.vo.StatsVO;
import com.memorygarden.service.StatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学习统计服务实现类
 *
 * @author jLU
 * @date 2026-05-20
 */
@Service
public class StatsServiceImpl implements StatsService {

    private final UserMapper userMapper;
    private final PlantMapper plantMapper;
    private final KnowledgeCardMapper cardMapper;
    private final ReviewRecordMapper reviewRecordMapper;

    public StatsServiceImpl(UserMapper userMapper, PlantMapper plantMapper,
                            KnowledgeCardMapper cardMapper, ReviewRecordMapper reviewRecordMapper) {
        this.userMapper = userMapper;
        this.plantMapper = plantMapper;
        this.cardMapper = cardMapper;
        this.reviewRecordMapper = reviewRecordMapper;
    }

    /**
     * 获取今日学习数据
     *
     * @param userId 用户 ID
     * @return 统计视图对象
     */
    @Override
    public StatsVO getToday(Long userId) {
        User user = userMapper.selectById(userId);
        List<ReviewRecord> todayRecords = reviewRecordMapper.selectByUserIdAndDate(userId, LocalDate.now());

        int todayDegraded = (int) todayRecords.stream()
                .filter(r -> r.getAfterStage() < r.getPrevStage())
                .count();

        List<KnowledgeCard> cards = cardMapper.selectByUserId(userId);
        List<Plant> plants = plantMapper.selectByUserId(userId);
        int totalCardCount = (int) cards.stream().filter(c -> c.getIsDeleted() == 0).count();
        int totalPlantCount = (int) plants.stream().filter(p -> p.getIsDeleted() == 0).count();

        StatsVO vo = new StatsVO();
        vo.setTodayNewCards(0);
        vo.setTodayReviewCount(todayRecords.size());
        vo.setTodayDegradedCount(todayDegraded);
        vo.setCurrentStreak(user != null ? user.getCurrentStreak() : 0);
        vo.setMaxStreak(user != null ? user.getMaxStreak() : 0);
        vo.setTotalCardCount(totalCardCount);
        vo.setTotalPlantCount(totalPlantCount);
        vo.setStageDistribution(getStageDistribution(userId));
        vo.setTrendData(getTrend(userId, 7));

        return vo;
    }

    /**
     * 获取趋势数据
     *
     * @param userId 用户 ID
     * @param days   天数
     * @return 日期->数量映射
     */
    @Override
    public Map<String, Integer> getTrend(Long userId, int days) {
        Map<String, Integer> trend = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            List<ReviewRecord> records = reviewRecordMapper.selectByUserIdAndDate(userId, date);
            trend.put(date.format(formatter), records.size());
        }

        return trend;
    }

    /**
     * 获取连续打卡数据
     *
     * @param userId 用户 ID
     * @return 连续天数
     */
    @Override
    public int getStreak(Long userId) {
        User user = userMapper.selectById(userId);
        return user != null ? user.getCurrentStreak() : 0;
    }

    /**
     * 获取各阶段植物数量分布
     *
     * @param userId 用户 ID
     * @return 阶段->数量映射
     */
    @Override
    public Map<Integer, Integer> getStageDistribution(Long userId) {
        List<Plant> plants = plantMapper.selectByUserId(userId);

        return plants.stream()
                .filter(p -> p.getIsDeleted() == 0)
                .collect(Collectors.groupingBy(
                        Plant::getGrowthStage,
                        Collectors.summingInt(p -> 1)
                ));
    }
}
