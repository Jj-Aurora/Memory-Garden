package com.memorygarden.mapper;

import com.memorygarden.model.entity.Plant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 植物 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface PlantMapper {

    /**
     * 根据用户 ID 查询植物列表
     *
     * @param userId 用户 ID
     * @return 植物列表
     */
    List<Plant> selectByUserId(Long userId);

    /**
     * 根据卡片 ID 查询植物
     *
     * @param cardId 卡片 ID
     * @return 植物实体
     */
    Plant selectByCardId(Long cardId);

    /**
     * 查询枯萎植物列表
     *
     * @param userId 用户 ID
     * @return 枯萎植物列表
     */
    List<Plant> selectWithered(Long userId);

    /**
     * 统计各阶段植物数量
     *
     * @param userId 用户 ID
     * @return 阶段->数量映射
     */
    List<Plant> countByStage(Long userId);

    /**
     * 查询所有未枯萎且未删除的植物（定时任务用）
     *
     * @return 植物列表
     */
    List<Plant> selectAllActive();

    /**
     * 根据 ID 查询植物
     *
     * @param id 植物 ID
     * @return 植物实体
     */
    Plant selectById(Long id);

    /**
     * 插入植物
     *
     * @param plant 植物实体
     * @return 影响行数
     */
    int insert(Plant plant);

    /**
     * 更新植物
     *
     * @param plant 植物实体
     * @return 影响行数
     */
    int updateById(Plant plant);
}
