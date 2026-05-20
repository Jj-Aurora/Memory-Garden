<template>
  <div class="garden-view">
    <div class="garden-header">
      <h2>我的花园</h2>
      <div class="garden-actions">
        <el-button type="primary" @click="router.push('/card/create')">
          <el-icon><Plus /></el-icon> 添加知识卡片
        </el-button>
        <el-button @click="router.push('/review')">
          <el-icon><Reading /></el-icon> 开始复习
          <el-badge v-if="pendingCount > 0" :value="pendingCount" class="review-badge" />
        </el-button>
      </div>
    </div>

    <!-- 统计概览 -->
    <div v-if="gardenView" class="garden-stats">
      <div class="stat-item">
        <span class="stat-value">{{ gardenView.totalCount }}</span>
        <span class="stat-label">总植物</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ gardenView.witheredCount }}</span>
        <span class="stat-label">枯萎</span>
      </div>
      <div v-for="(count, stage) in gardenView.stageCount" :key="stage" class="stat-item">
        <span class="stat-value">{{ count }}</span>
        <span class="stat-label">{{ stageName(stage as number) }}</span>
      </div>
    </div>

    <!-- 筛选和排序 -->
    <div class="garden-filter">
      <el-select v-model="gardenStore.filterCategoryId" placeholder="按分类筛选" clearable style="width: 160px"
        @change="applyFilter">
        <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
      </el-select>
      <el-select v-model="gardenStore.filterStage" placeholder="按阶段筛选" clearable style="width: 140px"
        @change="applyFilter">
        <el-option label="种子" :value="1" />
        <el-option label="发芽" :value="2" />
        <el-option label="成长" :value="3" />
        <el-option label="开花" :value="4" />
        <el-option label="结果" :value="5" />
      </el-select>
      <el-select v-model="gardenStore.filterWithered" placeholder="枯萎状态" clearable style="width: 130px"
        @change="applyFilter">
        <el-option label="正常" :value="false" />
        <el-option label="枯萎" :value="true" />
      </el-select>
      <el-select v-model="gardenStore.sortBy" style="width: 140px" @change="applySort">
        <el-option label="创建时间" value="createTime" />
        <el-option label="复习日期" value="nextReviewDate" />
        <el-option label="生长阶段" value="growthStage" />
      </el-select>
      <el-select v-model="gardenStore.sortOrder" style="width: 100px" @change="applySort">
        <el-option label="升序" value="asc" />
        <el-option label="降序" value="desc" />
      </el-select>
      <el-button @click="resetFilter">重置</el-button>
    </div>

    <!-- 植物网格 -->
    <div v-loading="gardenStore.loading">
      <div v-if="displayPlants.length > 0" class="plant-grid">
        <PlantCard
          v-for="plant in displayPlants"
          :key="plant.id"
          :plant="plant"
          @click="router.push(`/card/${plant.cardId}/edit`)"
        />
      </div>
      <el-empty v-else description="花园里还没有植物，快去添加知识卡片吧！" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus, Reading } from '@element-plus/icons-vue'
import { useGardenStore } from '@/stores/garden'
import { useReviewStore } from '@/stores/review'
import { categoryApi, type Category } from '@/api/category'
import type { PlantVO } from '@/api/garden'
import PlantCard from '@/components/PlantCard.vue'

const router = useRouter()
const gardenStore = useGardenStore()
const reviewStore = useReviewStore()
const categories = ref<Category[]>([])
const displayPlants = ref<PlantVO[]>([])
const pendingCount = computed(() => reviewStore.pendingCount)

function stageName(stage: number): string {
  const map: Record<number, string> = { 1: '种子', 2: '发芽', 3: '成长', 4: '开花', 5: '结果' }
  return map[stage] || '未知'
}

async function loadData() {
  await gardenStore.fetchGardenView()
  displayPlants.value = gardenStore.gardenView?.plants || []
  reviewStore.fetchPendingCount()
  const catRes = await categoryApi.list()
  categories.value = catRes.data
}

async function applyFilter() {
  const result = await gardenStore.filterPlants()
  displayPlants.value = result
}

async function applySort() {
  const result = await gardenStore.sortPlants()
  displayPlants.value = result
}

function resetFilter() {
  gardenStore.resetFilter()
  displayPlants.value = gardenStore.gardenView?.plants || []
}

onMounted(loadData)
</script>

<style scoped lang="scss">
.garden-view {
  .garden-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      color: #303133;
    }
  }

  .garden-actions {
    display: flex;
    gap: 12px;
    align-items: center;

    .review-badge {
      margin-left: 4px;
    }
  }

  .garden-stats {
    display: flex;
    gap: 16px;
    margin-bottom: 20px;
    flex-wrap: wrap;
  }

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 12px 20px;
    background: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
    min-width: 80px;
  }

  .stat-value {
    font-size: 24px;
    font-weight: 700;
    color: #2d8c3c;
  }

  .stat-label {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }

  .garden-filter {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
    flex-wrap: wrap;
    align-items: center;
  }

  .plant-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 16px;
  }
}
</style>
