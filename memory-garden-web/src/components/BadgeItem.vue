<template>
  <div class="badge-item" :class="{ 'is-earned': badge.earned, 'is-locked': !badge.earned }">
    <div class="badge-icon" :class="rarityClass">
      <span class="badge-emoji">{{ badgeEmoji }}</span>
    </div>
    <div class="badge-info">
      <h4 class="badge-name">{{ badge.name }}</h4>
      <p class="badge-desc">{{ badge.description }}</p>
      <span v-if="badge.earned && badge.earnedTime" class="badge-time">
        获得于 {{ formatDate(badge.earnedTime) }}
      </span>
    </div>
    <div class="badge-rarity" :class="rarityClass">
      {{ badge.rarityName }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { BadgeVO } from '@/api/badge'

const props = defineProps<{
  badge: BadgeVO
}>()

const rarityClass = computed(() => {
  const map: Record<number, string> = { 0: 'common', 1: 'rare', 2: 'epic' }
  return map[props.badge.rarity] || 'common'
})

const badgeEmoji = computed(() => {
  const iconMap: Record<string, string> = {
    'first_plant': '🌱',
    'streak_7': '🔥',
    'streak_30': '🔥',
    'blooming_5': '🌸',
    'first_fruit': '🍎',
    'total_50': '🌳',
    'revive_1': '💚',
    'category_3': '🏷️'
  }
  return iconMap[props.badge.icon] || '🏆'
})

function formatDate(dateStr: string) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}
</script>

<style scoped lang="scss">
.badge-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-radius: 12px;
  background: #fff;
  border: 1px solid #ebeef5;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  &.is-locked {
    opacity: 0.5;
    filter: grayscale(0.8);
  }
}

.badge-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  &.common {
    background: linear-gradient(135deg, #e0e0e0, #bdbdbd);
  }

  &.rare {
    background: linear-gradient(135deg, #64b5f6, #1976d2);
  }

  &.epic {
    background: linear-gradient(135deg, #ce93d8, #7b1fa2);
  }

  .badge-emoji {
    font-size: 28px;
  }
}

.badge-info {
  flex: 1;
  min-width: 0;
}

.badge-name {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 4px;
}

.badge-desc {
  font-size: 13px;
  color: #909399;
  margin: 0;
}

.badge-time {
  font-size: 12px;
  color: #67c23a;
}

.badge-rarity {
  font-size: 12px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 4px;
  flex-shrink: 0;

  &.common {
    background: #f5f5f5;
    color: #757575;
  }

  &.rare {
    background: #e3f2fd;
    color: #1565c0;
  }

  &.epic {
    background: #f3e5f5;
    color: #6a1b9a;
  }
}
</style>
