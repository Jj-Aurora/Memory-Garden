import { post, get, put } from './request'

/**
 * 用户视图对象
 */
export interface UserVO {
  id: number
  username: string
  nickname: string
  avatarUrl: string
  currentStreak: number
  maxStreak: number
  lastCheckIn: string
}

/**
 * 用户 API 封装
 */
export const userApi = {
  /**
   * 用户注册
   */
  register(data: { username: string; password: string; nickname?: string }) {
    return post<number>('/user/register', data)
  },

  /**
   * 用户登录
   */
  login(data: { username: string; password: string }) {
    return post<string>('/user/login', data)
  },

  /**
   * 获取当前登录用户信息
   */
  getCurrentUser() {
    return get<UserVO>('/user/current')
  },

  /**
   * 修改个人信息
   */
  updateProfile(data: { nickname?: string; avatarUrl?: string }) {
    return put<boolean>('/user/profile', data)
  }
}
