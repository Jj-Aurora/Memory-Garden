<template>
  <div class="garden-view">
    <div class="garden-header">
      <div>
        <h2 class="section-title">我的花园</h2>
        <p class="garden-subtitle">用知识浇灌，让记忆开花</p>
      </div>
      <div class="garden-actions">
        <el-button type="primary" size="large" @click="router.push('/card/create')">
          <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          添加知识卡片
        </el-button>
        <el-button size="large" @click="router.push('/review')">
          <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
            <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
          </svg>
          开始复习
          <span v-if="pendingCount > 0" class="review-count">{{ pendingCount }}</span>
        </el-button>
      </div>
    </div>

    <!-- 统计概览 -->
    <div v-if="gardenView" class="garden-stats">
      <div class="stat-card stat-total">
        <span class="stat-value">{{ gardenView.totalCount }}</span>
        <span class="stat-label">总植物</span>
      </div>
      <div v-if="gardenView.witheredCount > 0" class="stat-card stat-withered">
        <span class="stat-value">{{ gardenView.witheredCount }}</span>
        <span class="stat-label">枯萎</span>
      </div>
      <div v-for="(count, stage) in gardenView.stageCount" :key="stage" class="stat-card"
        :class="`stat-stage-${stage}`">
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
      <div v-else class="empty-state">
        <svg class="empty-icon" viewBox="0 0 64 64" fill="none">
          <circle cx="32" cy="32" r="28" stroke="var(--color-border)" stroke-width="2" fill="var(--color-primary-bg)" />
          <path d="M32 18c-4 0-8 4-8 8s4 8 8 8 8-4 8-8-4-8-8-8z" fill="var(--color-primary-lighter)" />
          <path d="M32 34v10M28 40h8" stroke="var(--color-primary)" stroke-width="2" stroke-linecap="round" />
        </svg>
        <p class="empty-text">花园里还没有植物，快去添加知识卡片吧！</p>
        <el-button type="primary" @click="router.push('/card/create')">添加第一张卡片</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
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
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: var(--space-xl);
    gap: var(--space-base);
  }

  .garden-subtitle {
    font-size: var(--font-size-sm);
    color: var(--color-text-muted);
    margin-top: var(--space-xs);
  }

  .garden-actions {
    display: flex;
    gap: var(--space-sm);
    align-items: center;
    flex-shrink: 0;
  }

  .btn-icon {
    width: 16px;
    height: 16px;
    margin-right: 4px;
  }

  .review-count {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 20px;
    height: 20px;
    padding: 0 6px;
    margin-left: 6px;
    font-size: 11px;
    font-weight: var(--font-weight-bold);
    color: #fff;
    background: var(--color-accent-danger);
    border-radius: var(--radius-full);
  }
}

.garden-stats {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-xl);
  flex-wrap: wrap;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-base) var(--space-lg);
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border-light);
  min-width: 80px;
  transition: all var(--transition-normal);

  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-primary);
  }

  .stat-value {
    font-size: var(--font-size-2xl);
    font-weight: var(--font-weight-bold);
    color: var(--color-primary);
    line-height: var(--line-height-tight);
  }

  .stat-label {
    font-size: var(--font-size-xs);
    color: var(--color-text-muted);
    margin-top: var(--space-xs);
    font-weight: var(--font-weight-medium);
  }

  &.stat-withered .stat-value {
    color: var(--color-withered);
  }

  &.stat-stage-1 .stat-value { color: var(--color-seed); }
  &.stat-stage-2 .stat-value { color: var(--color-sprout); }
  &.stat-stage-3 .stat-value { color: var(--color-growing); }
  &.stat-stage-4 .stat-value { color: var(--color-blooming); }
  &.stat-stage-5 .stat-value { color: var(--color-fruiting); }
}

.garden-filter {
  display: flex;
  gap: var(--space-sm);
  margin-bottom: var(--space-xl);
  flex-wrap: wrap;
  align-items: center;
}

.plant-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: var(--space-base);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-3xl);
  text-align: center;

  .empty-icon {
    width: 80px;
    height: 80px;
    margin-bottom: var(--space-lg);
  }

  .empty-text {
    font-size: var(--font-size-md);
    color: var(--color-text-muted);
    margin-bottom: var(--space-lg);
  }
}

@media (max-width: 768px) {
  .garden-view .garden-header {
    flex-direction: column;
  }

  .garden-view .garden-actions {
    width: 100%;
  }

  .garden-view .garden-actions .el-button {
    flex: 1;
  }

  .plant-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: var(--space-sm);
  }

  .stat-card {
    padding: var(--space-sm) var(--space-base);
    min-width: 70px;

    .stat-value {
      font-size: var(--font-size-xl);
    }
  }
}
</style>
