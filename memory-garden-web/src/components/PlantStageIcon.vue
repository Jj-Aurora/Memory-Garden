<template>
  <span class="plant-stage-icon" :class="stageClass">
    <img v-if="imgSrc" :src="imgSrc" :alt="stageName" class="stage-img" />
    <span v-else class="stage-emoji">{{ stageEmoji }}</span>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  stage: number
  withered?: boolean
}>()

const stageMap: Record<number, { name: string; emoji: string; cls: string }> = {
  1: { name: '种子', emoji: '🌰', cls: 'seed' },
  2: { name: '发芽', emoji: '🌱', cls: 'sprout' },
  3: { name: '成长', emoji: '🌿', cls: 'growing' },
  4: { name: '开花', emoji: '🌸', cls: 'blooming' },
  5: { name: '结果', emoji: '🍎', cls: 'fruiting' }
}

const stageName = computed(() => {
  if (props.withered) return '枯萎'
  return stageMap[props.stage]?.name || '未知'
})

const stageEmoji = computed(() => {
  if (props.withered) return '🥀'
  return stageMap[props.stage]?.emoji || '❓'
})

const stageClass = computed(() => {
  if (props.withered) return 'withered'
  return stageMap[props.stage]?.cls || ''
})

const imgSrc = computed(() => {
  const name = props.withered ? 'withered' : stageMap[props.stage]?.cls
  if (!name) return ''
  const modules = import.meta.glob('../assets/images/plants/*.svg', { eager: true, as: 'url' })
  const key = `../assets/images/plants/${name}.svg`
  return (modules as Record<string, string>)[key] || ''
})
</script>

<style scoped lang="scss">
.plant-stage-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;

  .stage-emoji {
    font-size: 28px;
    line-height: 1;
  }

  .stage-img {
    width: 36px;
    height: 36px;
    object-fit: contain;
  }

  &.withered {
    opacity: 0.6;
    filter: grayscale(0.5);
  }
}
</style>
