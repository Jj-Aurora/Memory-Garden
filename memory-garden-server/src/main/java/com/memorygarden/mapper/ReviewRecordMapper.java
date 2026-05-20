package com.memorygarden.mapper;

import com.memorygarden.model.entity.ReviewRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 复习记录 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface ReviewRecordMapper {

    /**
     * 根据卡片 ID 查询复习记录
     *
     * @param cardId 卡片 ID
     * @return 复习记录列表
     */
    List<ReviewRecord> selectByCardId(Long cardId);

    /**
     * 根据用户 ID 和日期查询复习记录
     *
     * @param userId 用户 ID
     * @param date   日期
     * @return 复习记录列表
     */
    List<ReviewRecord> selectByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    /**
     * 统计用户某日复习次数
     *
     * @param userId 用户 ID
     * @param date   日期
     * @return 复习次数
     */
    int countByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    /**
     * 插入复习记录
     *
     * @param record 复习记录
     * @return 影响行数
     */
    int insert(ReviewRecord record);
}
