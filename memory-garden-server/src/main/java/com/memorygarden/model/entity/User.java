package com.memorygarden.model.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 用户实体
 *
 * @author jLU
 * @date 2026-05-20
 */
@Data
public class User {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（BCrypt 加密）
     */
    private String password;

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
    private Integer currentStreak = 0;

    /**
     * 历史最长连续天数
     */
    private Integer maxStreak = 0;

    /**
     * 最后打卡日期
     */
    private LocalDate lastCheckIn;

    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status = 1;

    /**
     * 软删除：0-未删除，1-已删除
     */
    private Integer isDeleted = 0;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
