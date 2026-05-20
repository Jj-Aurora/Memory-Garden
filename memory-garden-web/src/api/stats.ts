import { get } from './request'

/**
 * 学习统计视图对象
 */
export interface StatsVO {
  todayNewCards: number
  todayReviewCount: number
  todayDegradedCount: number
  currentStreak: number
  maxStreak: number
  stageDistribution: Record<number, number>
  trendData: Record<string, number>
}

/**
 * 学习统计 API 封装
 */
export const statsApi = {
  /**
   * 获取今日学习数据
   */
  getToday() {
    return get<StatsVO>('/stats/today')
  },

  /**
   * 获取趋势数据
   */
  getTrend(days: number = 7) {
    return get<Record<string, number>>('/stats/trend', { days })
  },

  /**
   * 获取连续打卡天数
   */
  getStreak() {
    return get<number>('/stats/streak')
  },

  /**
   * 获取各阶段植物分布
   */
  getStageDistribution() {
    return get<Record<number, number>>('/stats/stage-distribution')
  }
}
