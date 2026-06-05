<template>
  <div class="plant-card" :class="{ 'is-withered': plant.withered, 'is-pending': isPending }" @click="$emit('click')"
    tabindex="0" role="button" :aria-label="`编辑卡片: ${plant.cardFrontContent}`">
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
      <span v-if="plant.withered" class="status-badge withered">已枯萎</span>
      <span v-else-if="isPending" class="status-badge pending">待复习</span>
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
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  padding: var(--space-base);
  cursor: pointer;
  transition: all var(--transition-normal);
  border: 2px solid transparent;
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
  min-height: 140px;

  &:hover {
    transform: translateY(-3px);
    box-shadow: var(--shadow-card-hover);
    border-color: var(--color-primary-lighter);
  }

  &:focus-visible {
    outline: 2px solid var(--color-primary);
    outline-offset: 2px;
  }

  &:active {
    transform: translateY(-1px);
  }

  &.is-withered {
    border-color: var(--color-border);
    opacity: 0.75;

    .plant-card-header {
      filter: grayscale(0.6);
    }

    &:hover {
      border-color: var(--color-withered);
      box-shadow: 0 4px 16px rgba(158, 158, 158, 0.15);
    }
  }

  &.is-pending {
    border-color: var(--color-accent-warm);
    box-shadow: 0 2px 12px rgba(255, 152, 0, 0.15);

    &:hover {
      border-color: var(--color-accent-warm);
      box-shadow: 0 6px 20px rgba(255, 152, 0, 0.2);
    }
  }

  &-header {
    display: flex;
    align-items: center;
    gap: var(--space-sm);
    margin-bottom: var(--space-sm);
  }

  &-stage-name {
    font-size: var(--font-size-xs);
    color: var(--color-text-muted);
    font-weight: var(--font-weight-medium);
  }

  &-body {
    flex: 1;
    margin-bottom: var(--space-sm);
  }

  &-title {
    font-size: var(--font-size-sm);
    color: var(--color-text-primary);
    line-height: var(--line-height-normal);
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  &-category {
    margin-top: var(--space-xs);
  }

  &-footer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: var(--font-size-xs);
  }
}

.status-badge {
  font-weight: var(--font-weight-semibold);
  padding: 2px 8px;
  border-radius: var(--radius-full);
  font-size: 11px;

  &.withered {
    color: var(--color-accent-danger);
    background: var(--color-accent-danger-light);
  }

  &.pending {
    color: var(--color-accent-warm);
    background: var(--color-accent-warm-light);
  }
}

.review-info {
  color: var(--color-text-muted);
}
</style>
