<template>
  <div class="review-summary-view">
    <h2>复习总结</h2>

    <div v-if="summary" class="summary-content">
      <div class="summary-stats">
        <div class="summary-stat">
          <span class="stat-value">{{ summary.totalReviewed }}</span>
          <span class="stat-label">复习总数</span>
        </div>
        <div class="summary-stat success">
          <span class="stat-value">{{ summary.rememberedCount }}</span>
          <span class="stat-label">记住了</span>
        </div>
        <div class="summary-stat warning">
          <span class="stat-value">{{ summary.vagueCount }}</span>
          <span class="stat-label">模糊</span>
        </div>
        <div class="summary-stat danger">
          <span class="stat-value">{{ summary.forgottenCount }}</span>
          <span class="stat-label">忘记了</span>
        </div>
      </div>

      <div class="summary-details">
        <div class="detail-row">
          <span>升级数量</span>
          <span class="detail-value upgrade">{{ summary.upgradedCount }}</span>
        </div>
        <div class="detail-row">
          <span>回退数量</span>
          <span class="detail-value downgrade">{{ summary.downgradedCount }}</span>
        </div>
        <div class="detail-row">
          <span>待复习剩余</span>
          <span class="detail-value remaining">{{ summary.remainingCount }}</span>
        </div>
      </div>

      <div class="summary-actions">
        <el-button type="primary" @click="router.push('/')">返回花园</el-button>
        <el-button v-if="summary.remainingCount > 0" @click="continueReview">继续复习</el-button>
      </div>
    </div>

    <el-empty v-else description="暂无复习数据" />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useReviewStore } from '@/stores/review'

const router = useRouter()
const reviewStore = useReviewStore()
const summary = reviewStore.summary

function continueReview() {
  reviewStore.reset()
  router.push('/review')
}

onMounted(() => {
  if (!reviewStore.summary) {
    reviewStore.fetchSummary()
  }
})
</script>

<style scoped lang="scss">
.review-summary-view {
  max-width: 640px;
  margin: 0 auto;

  h2 {
    margin: 0 0 24px;
    color: #303133;
  }
}

.summary-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.summary-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  flex: 1;
  min-width: 100px;

  &.success .stat-value { color: #67c23a; }
  &.warning .stat-value { color: #e6a23c; }
  &.danger .stat-value { color: #f56c6c; }

  .stat-value {
    font-size: 28px;
    font-weight: 700;
    color: #303133;
  }

  .stat-label {
    font-size: 13px;
    color: #909399;
    margin-top: 4px;
  }
}

.summary-details {
  background: #fff;
  border-radius: 12px;
  padding: 16px 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
  color: #606266;

  &:not(:last-child) {
    border-bottom: 1px solid #f0f0f0;
  }
}

.detail-value {
  font-weight: 600;

  &.upgrade { color: #67c23a; }
  &.downgrade { color: #f56c6c; }
  &.remaining { color: #e6a23c; }
}

.summary-actions {
  display: flex;
  gap: 12px;
}
</style>
