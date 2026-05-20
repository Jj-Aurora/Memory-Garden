import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { reviewApi } from '@/api/review'
import type { ReviewVO, ReviewSummaryVO } from '@/api/review'

/**
 * 复习状态管理
 */
export const useReviewStore = defineStore('review', () => {
  const pendingList = ref<ReviewVO[]>([])
  const currentIndex = ref(0)
  const summary = ref<ReviewSummaryVO | null>(null)
  const loading = ref(false)
  const pendingCount = ref(0)

  const currentItem = computed(() => {
    if (pendingList.value.length === 0 || currentIndex.value >= pendingList.value.length) {
      return null
    }
    return pendingList.value[currentIndex.value]
  })

  const progress = computed(() => {
    if (pendingList.value.length === 0) return 0
    return Math.round(((currentIndex.value) / pendingList.value.length) * 100)
  })

  const isFinished = computed(() => {
    return currentIndex.value >= pendingList.value.length && pendingList.value.length > 0
  })

  /**
   * 获取今日待复习列表
   */
  async function fetchPending() {
    loading.value = true
    try {
      const res = await reviewApi.getPending()
      pendingList.value = res.data
      currentIndex.value = 0
    } finally {
      loading.value = false
    }
  }

  /**
   * 获取待复习数量
   */
  async function fetchPendingCount() {
    const res = await reviewApi.getPendingCount()
    pendingCount.value = res.data
  }

  /**
   * 提交复习自评结果
   *
   * @param cardId 卡片ID
   * @param selfEvaluation 自评值（1-记住了 2-模糊 3-忘记了）
   */
  async function submitReview(cardId: number, selfEvaluation: number) {
    await reviewApi.submit({ cardId, selfEvaluation })
    currentIndex.value++
  }

  /**
   * 获取今日复习总结
   */
  async function fetchSummary() {
    const res = await reviewApi.getSummary()
    summary.value = res.data
  }

  /**
   * 重置复习状态
   */
  function reset() {
    pendingList.value = []
    currentIndex.value = 0
    summary.value = null
  }

  return {
    pendingList,
    currentIndex,
    summary,
    loading,
    pendingCount,
    currentItem,
    progress,
    isFinished,
    fetchPending,
    fetchPendingCount,
    submitReview,
    fetchSummary,
    reset
  }
})
