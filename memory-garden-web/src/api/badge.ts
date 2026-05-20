import { get } from './request'

/**
 * 徽章视图对象
 */
export interface BadgeVO {
  id: number
  name: string
  description: string
  icon: string
  rarity: number
  rarityName: string
  earned: boolean
  earnedTime: string
}

/**
 * 徽章 API 封装
 */
export const badgeApi = {
  /**
   * 获取所有徽章列表（含获得状态）
   */
  getAllBadges() {
    return get<BadgeVO[]>('/badge/list')
  },

  /**
   * 获取已获得的徽章列表
   */
  getMyBadges() {
    return get<BadgeVO[]>('/badge/my')
  }
}
