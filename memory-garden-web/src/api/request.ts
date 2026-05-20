import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

/**
 * 统一响应结构
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 创建 axios 实例
 */
const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

/**
 * 请求拦截器：注入 Token
 */
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token && config.headers) {
      config.headers.Authorization = token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器：统一错误处理
 */
request.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data
    if (res.code !== 0) {
      ElMessage.error(res.message || '请求失败')
      // 未登录：清除 Token 并跳转登录页
      if (res.code === 40100) {
        localStorage.removeItem('token')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res as any
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

/**
 * GET 请求
 *
 * @param url 请求地址
 * @param params 查询参数
 * @returns Promise
 */
export function get<T = any>(url: string, params?: any): Promise<ApiResponse<T>> {
  return request.get(url, { params })
}

/**
 * POST 请求
 *
 * @param url 请求地址
 * @param data 请求体
 * @returns Promise
 */
export function post<T = any>(url: string, data?: any): Promise<ApiResponse<T>> {
  return request.post(url, data)
}

/**
 * PUT 请求
 *
 * @param url 请求地址
 * @param data 请求体
 * @returns Promise
 */
export function put<T = any>(url: string, data?: any): Promise<ApiResponse<T>> {
  return request.put(url, data)
}

/**
 * DELETE 请求
 *
 * @param url 请求地址
 * @param params 查询参数
 * @returns Promise
 */
export function del<T = any>(url: string, params?: any): Promise<ApiResponse<T>> {
  return request.delete(url, { params })
}

export default request
