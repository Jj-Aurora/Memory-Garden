import { get } from './request'

/**
 * 植物视图对象
 */
export interface PlantVO {
  id: number
  cardId: number
  cardFrontContent: string
  categoryName: string
  growthStage: number
  growthStageName: string
  withered: boolean
  reviewRound: number
  nextReviewDate: string
  totalReviewCount: number
}

/**
 * 花园视图对象
 */
export interface GardenVO {
  plants: PlantVO[]
  stageCount: Record<number, number>
  witheredCount: number
  totalCount: number
}

/**
 * 花园 API 封装
 */
export const gardenApi = {
  /**
   * 获取花园视图数据
   */
  getGardenView() {
    return get<GardenVO>('/garden')
  },

  /**
   * 按条件筛选植物
   */
  filter(params: { categoryId?: number; stage?: number; withered?: boolean }) {
    return get<PlantVO[]>('/garden/filter', params)
  },

  /**
   * 按条件排序植物
   */
  sort(sortBy: string, order: string) {
    return get<PlantVO[]>('/garden/sort', { sortBy, order })
  },

  /**
   * 获取枯萎植物列表
   */
  getWithered() {
    return get<PlantVO[]>('/garden/withered')
  }
}
