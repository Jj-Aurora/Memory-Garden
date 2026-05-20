import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '@/api/user'
import type { UserVO } from '@/api/user'

/**
 * 用户状态管理
 */
export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserVO | null>(null)

  /**
   * 用户登录
   *
   * @param username 用户名
   * @param password 密码
   */
  async function login(username: string, password: string) {
    const res = await userApi.login({ username, password })
    token.value = res.data
    localStorage.setItem('token', res.data)
  }

  /**
   * 用户注册
   *
   * @param data 注册信息
   */
  async function register(data: { username: string; password: string; nickname?: string }) {
    await userApi.register(data)
  }

  /**
   * 获取当前登录用户信息
   */
  async function getCurrentUser() {
    const res = await userApi.getCurrentUser()
    userInfo.value = res.data
    return res.data
  }

  /**
   * 修改个人信息
   *
   * @param nickname 昵称
   * @param avatarUrl 头像URL
   */
  async function updateProfile(nickname?: string, avatarUrl?: string) {
    await userApi.updateProfile({ nickname, avatarUrl })
    if (userInfo.value) {
      if (nickname) userInfo.value.nickname = nickname
      if (avatarUrl) userInfo.value.avatarUrl = avatarUrl
    }
  }

  /**
   * 退出登录
   */
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    login,
    register,
    getCurrentUser,
    updateProfile,
    logout
  }
})
