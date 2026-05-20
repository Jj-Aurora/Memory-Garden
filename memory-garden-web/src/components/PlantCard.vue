<template>
  <div class="plant-card" :class="{ 'is-withered': plant.withered, 'is-pending': isPending }" @click="$emit('click')">
    <div class="plant-card-header">
      <PlantStageIcon :stage="plant.growthStage" :withered="plant.withered" />
      <span class="plant-stage-name">{{ plant.growthStageName }}</span>
    </div>
    <div class="plant-card-body">
      <p class="plant-card-title">{{ plant.cardFrontContent }}</p>
      <p v-if="plant.categoryName" class="plant-card-category">
        <CategoryTag :name="plant.categoryName" effect="plain" />
      </p>
    </div>
    <div class="plant-card-footer">
      <span v-if="plant.withered" class="withered-badge">已枯萎</span>
      <span v-else-if="isPending" class="pending-badge">待复习</span>
      <span class="review-info">第{{ plant.reviewRound }}轮</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import PlantStageIcon from './PlantStageIcon.vue'
import CategoryTag from './CategoryTag.vue'
import type { PlantVO } from '@/api/garden'

const props = defineProps<{
  plant: PlantVO
}>()

defineEmits<{
  click: []
}>()

const isPending = computed(() => {
  if (props.plant.withered) return false
  const nextDate = new Date(props.plant.nextReviewDate)
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return nextDate <= today
})
</script>

<style scoped lang="scss">
.plant-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  }

  &.is-withered {
    border-color: #c0c4cc;
    opacity: 0.7;

    .plant-card-header {
      filter: grayscale(0.6);
    }
  }

  &.is-pending {
    border-color: #e6a23c;
    box-shadow: 0 2px 12px rgba(230, 162, 60, 0.2);
  }

  &-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
  }

  &-stage-name {
    font-size: 13px;
    color: #909399;
  }

  &-body {
    margin-bottom: 8px;
  }

  &-title {
    font-size: 14px;
    color: #303133;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  &-category {
    margin-top: 6px;
  }

  &-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 12px;
    color: #909399;
  }
}

.withered-badge {
  color: #f56c6c;
  font-weight: 500;
}

.pending-badge {
  color: #e6a23c;
  font-weight: 500;
}

.review-info {
  color: #909399;
}
</style>
