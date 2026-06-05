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
  gap: var(--space-base);
  padding: var(--space-base);
  border-radius: var(--radius-lg);
  background: var(--color-bg-card);
  border: 1px solid var(--color-border-light);
  transition: all var(--transition-normal);

  &:hover {
    box-shadow: var(--shadow-primary);
    transform: translateY(-1px);
  }

  &.is-earned {
    border-color: var(--color-primary-lighter);
  }

  &.is-locked {
    opacity: 0.5;
    filter: grayscale(0.8);
  }
}

.badge-icon {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  &.common {
    background: linear-gradient(135deg, var(--color-rarity-common-bg), #cfd8dc);
  }

  &.rare {
    background: linear-gradient(135deg, var(--color-rarity-rare-bg), #bbdefb);
  }

  &.epic {
    background: linear-gradient(135deg, var(--color-rarity-epic-bg), #e1bee7);
  }

  .badge-emoji {
    font-size: 26px;
  }
}

.badge-info {
  flex: 1;
  min-width: 0;
}

.badge-name {
  font-size: var(--font-size-base);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  margin: 0 0 var(--space-xs);
}

.badge-desc {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin: 0;
}

.badge-time {
  font-size: var(--font-size-xs);
  color: var(--color-primary);
  margin-top: var(--space-xs);
  display: inline-block;
}

.badge-rarity {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  padding: 2px 10px;
  border-radius: var(--radius-full);
  flex-shrink: 0;

  &.common {
    background: var(--color-rarity-common-bg);
    color: var(--color-rarity-common);
  }

  &.rare {
    background: var(--color-rarity-rare-bg);
    color: var(--color-rarity-rare);
  }

  &.epic {
    background: var(--color-rarity-epic-bg);
    color: var(--color-rarity-epic);
  }
}

@media (max-width: 480px) {
  .badge-item {
    gap: var(--space-sm);
    padding: var(--space-sm);
  }

  .badge-icon {
    width: 44px;
    height: 44px;

    .badge-emoji {
      font-size: 22px;
    }
  }
}
</style>
