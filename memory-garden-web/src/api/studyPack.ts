import { get, post } from './request'

/**
 * 预设知识包实体
 */
export interface StudyPack {
  id: number
  name: string
  description: string
  categoryName: string
  cardCount: number
  isDeleted: number
  createTime: string
  updateTime: string
}

/**
 * 知识包条目实体
 */
export interface StudyPackItem {
  id: number
  packId: number
  frontContent: string
  backContent: string
  note: string
  sortOrder: number
  isDeleted: number
  createTime: string
}

/**
 * 预设知识包 API 封装
 */
export const studyPackApi = {
  /**
   * 获取知识包列表
   */
  list() {
    return get<StudyPack[]>('/study-pack/list')
  },

  /**
   * 获取知识包详情
   */
  getDetail(id: number) {
    return get<StudyPack>(`/study-pack/${id}`)
  },

  /**
   * 获取知识包条目列表
   */
  getPackItems(id: number) {
    return get<StudyPackItem[]>(`/study-pack/${id}/items`)
  },

  /**
   * 导入知识包
   */
  importPack(id: number) {
    return post<number>(`/study-pack/${id}/import`)
  }
}
