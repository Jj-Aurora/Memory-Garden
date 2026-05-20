<template>
  <div class="review-view">
    <div v-if="reviewStore.isFinished" class="review-finished">
      <el-result icon="success" title="复习完成！" sub-title="你已完成今日所有复习">
        <template #extra>
          <el-button type="primary" @click="goSummary">查看总结</el-button>
          <el-button @click="router.push('/')">返回花园</el-button>
        </template>
      </el-result>
    </div>

    <div v-else class="review-content">
      <div class="review-header">
        <h2>今日复习</h2>
        <div class="review-progress">
          <el-progress :percentage="reviewStore.progress" :stroke-width="10" />
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

        <ReviewCard
          ref="reviewCardRef"
          :front-content="reviewStore.currentItem.frontContent"
          :back-content="reviewStore.currentItem.backContent"
          :note="reviewStore.currentItem.note"
        />

        <div class="review-actions">
          <el-button type="danger" size="large" :loading="submitLoading" @click="handleSubmit(3)">
            忘记了
          </el-button>
          <el-button type="warning" size="large" :loading="submitLoading" @click="handleSubmit(2)">
            模糊
          </el-button>
          <el-button type="success" size="large" :loading="submitLoading" @click="handleSubmit(1)">
            记住了
          </el-button>
        </div>
      </div>

      <el-empty v-else-if="!reviewStore.loading" description="今日没有待复习的卡片" />
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

    // 重置翻转状态
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
  margin-bottom: 24px;

  h2 {
    margin: 0 0 12px;
    color: #303133;
  }

  .review-progress {
    display: flex;
    align-items: center;
    gap: 12px;

    .el-progress {
      flex: 1;
    }

    .progress-text {
      font-size: 14px;
      color: #909399;
      white-space: nowrap;
    }
  }
}

.review-stage-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.stage-text {
  font-size: 14px;
  color: #606266;
}

.withered-text {
  color: #f56c6c;
}

.review-body {
  margin-bottom: 24px;
}

.review-actions {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 24px;
}

.review-finished {
  padding-top: 60px;
}
</style>
