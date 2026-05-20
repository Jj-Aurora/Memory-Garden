import { post, get, put, del } from './request'

/**
 * 分类实体
 */
export interface Category {
  id: number
  userId: number
  name: string
  icon: string
  sortOrder: number
  isDeleted: number
  createTime: string
  updateTime: string
}

/**
 * 分类 API 封装
 */
export const categoryApi = {
  /**
   * 创建分类
   */
  create(data: { name: string; icon?: string; sortOrder?: number }) {
    return post<number>('/category', data)
  },

  /**
   * 获取分类列表
   */
  list() {
    return get<Category[]>('/category/list')
  },

  /**
   * 修改分类
   */
  update(id: number, data: { name?: string; icon?: string; sortOrder?: number }) {
    return put<boolean>(`/category/${id}`, data)
  },

  /**
   * 删除分类
   */
  delete(id: number) {
    return del<boolean>(`/category/${id}`)
  }
}
