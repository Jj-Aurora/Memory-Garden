import { defineStore } from 'pinia'
import { ref } from 'vue'
import { gardenApi } from '@/api/garden'
import type { GardenVO, PlantVO } from '@/api/garden'

/**
 * 花园状态管理
 */
export const useGardenStore = defineStore('garden', () => {
  const gardenView = ref<GardenVO | null>(null)
  const filterCategoryId = ref<number | null>(null)
  const filterStage = ref<number | null>(null)
  const filterWithered = ref<boolean | null>(null)
  const sortBy = ref<string>('createTime')
  const sortOrder = ref<string>('asc')
  const loading = ref(false)

  /**
   * 获取花园视图数据
   */
  async function fetchGardenView() {
    loading.value = true
    try {
      const res = await gardenApi.getGardenView()
      gardenView.value = res.data
    } finally {
      loading.value = false
    }
  }

  /**
   * 按条件筛选植物
   */
  async function filterPlants(): Promise<PlantVO[]> {
    const res = await gardenApi.filter({
      categoryId: filterCategoryId.value ?? undefined,
      stage: filterStage.value ?? undefined,
      withered: filterWithered.value ?? undefined
    })
    return res.data
  }

  /**
   * 按条件排序植物
   */
  async function sortPlants(): Promise<PlantVO[]> {
    const res = await gardenApi.sort(sortBy.value, sortOrder.value)
    return res.data
  }

  /**
   * 获取枯萎植物列表
   */
  async function fetchWithered(): Promise<PlantVO[]> {
    const res = await gardenApi.getWithered()
    return res.data
  }

  /**
   * 重置筛选条件
   */
  function resetFilter() {
    filterCategoryId.value = null
    filterStage.value = null
    filterWithered.value = null
  }

  return {
    gardenView,
    filterCategoryId,
    filterStage,
    filterWithered,
    sortBy,
    sortOrder,
    loading,
    fetchGardenView,
    filterPlants,
    sortPlants,
    fetchWithered,
    resetFilter
  }
})
