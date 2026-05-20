import { get, post } from './request'

/**
 * 复习视图对象
 */
export interface ReviewVO {
  cardId: number
  frontContent: string
  backContent: string
  note: string
  growthStage: number
  withered: boolean
  reviewRound: number
  scheduledDate: string
}

/**
 * 复习总结视图对象
 */
export interface ReviewSummaryVO {
  totalReviewed: number
  rememberedCount: number
  vagueCount: number
  forgottenCount: number
  upgradedCount: number
  downgradedCount: number
  remainingCount: number
}

/**
 * 复习 API 封装
 */
export const reviewApi = {
  /**
   * 获取今日待复习列表
   */
  getPending() {
    return get<ReviewVO[]>('/review/pending')
  },

  /**
   * 获取下一个待复习
   */
  getNext() {
    return get<ReviewVO>('/review/next')
  },

  /**
   * 提交复习自评结果
   */
  submit(data: { cardId: number; selfEvaluation: number }) {
    return post<boolean>('/review/submit', data)
  },

  /**
   * 获取今日复习总结
   */
  getSummary() {
    return get<ReviewSummaryVO>('/review/summary')
  },

  /**
   * 获取待复习数量
   */
  getPendingCount() {
    return get<number>('/review/pending-count')
  }
}
