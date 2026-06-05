<template>
  <div class="review-view">
    <div v-if="reviewStore.isFinished" class="review-finished">
      <div class="finished-content">
        <svg class="finished-icon" viewBox="0 0 64 64" fill="none">
          <circle cx="32" cy="32" r="28" fill="var(--color-primary-bg)" stroke="var(--color-primary)" stroke-width="2" />
          <path d="M22 32l6 6 14-14" stroke="var(--color-primary)" stroke-width="3" stroke-linecap="round"
            stroke-linejoin="round" />
        </svg>
        <h3 class="finished-title">复习完成！</h3>
        <p class="finished-subtitle">你已完成今日所有复习</p>
        <div class="finished-actions">
          <el-button type="primary" size="large" @click="goSummary">查看总结</el-button>
          <el-button size="large" @click="router.push('/')">返回花园</el-button>
        </div>
      </div>
    </div>

    <div v-else class="review-content">
      <div class="review-header">
        <div>
          <h2 class="section-title">今日复习</h2>
          <p class="review-subtitle">温故而知新</p>
        </div>
        <div class="review-progress">
          <el-progress :percentage="reviewStore.progress" :stroke-width="8"
            :color="['#2d8c3c', '#4caf50', '#81c784']" />
          <span class="progress-text">
            {{ reviewStore.currentIndex + 1 }} / {{ reviewStore.pendingList.length }}
          </span>
        </div>
      </div>

      <div v-if="reviewStore.currentItem" class="review-body">
        <div class="review-stage-info">
          <PlantStageIcon :stage="reviewStore.currentItem.growthStage" :withered="reviewStore.currentItem.withered" />
          <span class="stage-text">
            {{ stageName(reviewStore.currentItem.growthStage) }}
            <span v-if="reviewStore.currentItem.withered" class="withered-text">（枯萎中）</span>
          </span>
        </div>

        <ReviewCard ref="reviewCardRef" :front-content="reviewStore.currentItem.frontContent"
          :back-content="reviewStore.currentItem.backContent" :note="reviewStore.currentItem.note" />

        <div class="review-actions">
          <el-button type="danger" size="large" :loading="submitLoading" @click="handleSubmit(3)">
            <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
            </svg>
            忘记了
          </el-button>
          <el-button type="warning" size="large" :loading="submitLoading" @click="handleSubmit(2)">
            <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" />
            </svg>
            模糊
          </el-button>
          <el-button type="success" size="large" :loading="submitLoading" @click="handleSubmit(1)">
            <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="20 6 9 17 4 12" />
            </svg>
            记住了
          </el-button>
        </div>
      </div>

      <div v-else-if="!reviewStore.loading" class="empty-review">
        <svg class="empty-icon" viewBox="0 0 64 64" fill="none">
          <circle cx="32" cy="32" r="28" stroke="var(--color-border)" stroke-width="2" fill="var(--color-primary-bg)" />
          <path d="M24 28h16M24 36h10" stroke="var(--color-primary-lighter)" stroke-width="2" stroke-linecap="round" />
        </svg>
        <p class="empty-text">今日没有待复习的卡片</p>
        <el-button type="primary" @click="router.push('/')">返回花园</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useReviewStore } from '@/stores/review'
import PlantStageIcon from '@/components/PlantStageIcon.vue'
import ReviewCard from '@/components/ReviewCard.vue'

const router = useRouter()
const reviewStore = useReviewStore()
const submitLoading = ref(false)
const reviewCardRef = ref<InstanceType<typeof ReviewCard>>()

function stageName(stage: number): string {
  const map: Record<number, string> = { 1: '种子', 2: '发芽', 3: '成长', 4: '开花', 5: '结果' }
  return map[stage] || '未知'
}

async function handleSubmit(selfEvaluation: number) {
  if (!reviewStore.currentItem) return

  submitLoading.value = true
  try {
    await reviewStore.submitReview(reviewStore.currentItem.cardId, selfEvaluation)
    const labels: Record<number, string> = { 1: '记住了', 2: '模糊', 3: '忘记了' }
    ElMessage.success(labels[selfEvaluation])
    reviewCardRef.value?.reset()
  } catch {
    // 错误已在拦截器中处理
  } finally {
    submitLoading.value = false
  }
}

function goSummary() {
  reviewStore.fetchSummary()
  router.push('/review/summary')
}

onMounted(() => {
  reviewStore.fetchPending()
})
</script>

<style scoped lang="scss">
.review-view {
  max-width: 640px;
  margin: 0 auto;
}

.review-header {
  margin-bottom: var(--space-xl);

  .review-subtitle {
    font-size: var(--font-size-sm);
    color: var(--color-text-muted);
    margin: var(--space-xs) 0 var(--space-base);
  }

  .review-progress {
    display: flex;
    align-items: center;
    gap: var(--space-md);

    .el-progress {
      flex: 1;
    }

    .progress-text {
      font-size: var(--font-size-sm);
      color: var(--color-text-muted);
      white-space: nowrap;
      font-weight: var(--font-weight-medium);
    }
  }
}

.review-stage-info {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  margin-bottom: var(--space-base);
}

.stage-text {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.withered-text {
  color: var(--color-accent-danger);
}

.review-body {
  margin-bottom: var(--space-xl);
}

.review-actions {
  display: flex;
  justify-content: center;
  gap: var(--space-lg);
  margin-top: var(--space-xl);

  .el-button {
    min-width: 120px;
    height: var(--touch-min);
    border-radius: var(--radius-md);
    font-weight: var(--font-weight-semibold);
  }
}

.action-icon {
  width: 16px;
  height: 16px;
  margin-right: 4px;
}

.review-finished {
  padding-top: var(--space-3xl);

  .finished-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .finished-icon {
    width: 80px;
    height: 80px;
    margin-bottom: var(--space-lg);
  }

  .finished-title {
    font-size: var(--font-size-2xl);
    color: var(--color-primary);
    margin: 0 0 var(--space-sm);
  }

  .finished-subtitle {
    color: var(--color-text-muted);
    margin: 0 0 var(--space-xl);
  }

  .finished-actions {
    display: flex;
    gap: var(--space-md);
  }
}

.empty-review {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-3xl);
  text-align: center;

  .empty-icon {
    width: 80px;
    height: 80px;
    margin-bottom: var(--space-lg);
  }

  .empty-text {
    color: var(--color-text-muted);
    margin-bottom: var(--space-lg);
  }
}

@media (max-width: 768px) {
  .review-actions {
    flex-direction: column;
    gap: var(--space-sm);

    .el-button {
      width: 100%;
    }
  }
}
</style>
