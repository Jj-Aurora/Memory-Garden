import { post, get, put, del } from './request'

/**
 * 知识卡片视图对象
 */
export interface CardVO {
  id: number
  categoryId: number | null
  categoryName: string
  frontContent: string
  backContent: string
  note: string
  sourceType: number
  createTime: string
  updateTime: string
}

/**
 * 知识卡片 API 封装
 */
export const cardApi = {
  /**
   * 创建知识卡片
   */
  create(data: { frontContent: string; backContent: string; categoryId?: number; note?: string }) {
    return post<number>('/card', data)
  },

  /**
   * 获取卡片详情
   */
  getById(id: number) {
    return get<CardVO>(`/card/${id}`)
  },

  /**
   * 获取卡片列表
   */
  list(categoryId?: number) {
    return get<CardVO[]>('/card/list', { categoryId })
  },

  /**
   * 修改知识卡片
   */
  update(id: number, data: { frontContent?: string; backContent?: string; categoryId?: number; note?: string }) {
    return put<boolean>(`/card/${id}`, data)
  },

  /**
   * 删除知识卡片
   */
  delete(id: number) {
    return del<boolean>(`/card/${id}`)
  }
}
