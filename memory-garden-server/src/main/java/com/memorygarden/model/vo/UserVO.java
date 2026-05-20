package com.memorygarden.model.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户视图对象
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class UserVO {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像 URL
     */
    private String avatarUrl;

    /**
     * 当前连续打卡天数
     */
    private Integer currentStreak;

    /**
     * 历史最长连续天数
     */
    private Integer maxStreak;

    /**
     * 最后打卡日期
     */
    private LocalDate lastCheckIn;
}
