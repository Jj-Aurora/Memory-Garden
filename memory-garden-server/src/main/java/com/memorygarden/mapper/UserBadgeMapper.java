package com.memorygarden.mapper;

import com.memorygarden.model.entity.UserBadge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户徽章 Mapper 接口
 *
 * @author jLU
 * @date 2026-05-20
 */
@Mapper
public interface UserBadgeMapper {

    /**
     * 根据用户 ID 查询已获得徽章
     *
     * @param userId 用户 ID
     * @return 用户徽章列表
     */
    List<UserBadge> selectByUserId(Long userId);

    /**
     * 判断用户是否已获得指定徽章
     *
     * @param userId  用户 ID
     * @param badgeId 徽章 ID
     * @return true=已获得
     */
    boolean existsByUserIdAndBadgeId(@Param("userId") Long userId, @Param("badgeId") Long badgeId);

    /**
     * 插入用户徽章
     *
     * @param userBadge 用户徽章
     * @return 影响行数
     */
    int insert(UserBadge userBadge);
}
