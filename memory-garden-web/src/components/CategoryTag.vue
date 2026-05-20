<template>
  <el-tag :type="tagType" :effect="effect" class="category-tag" @click="$emit('click')">
    {{ name }}
  </el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  name: string
  color?: string
  effect?: 'dark' | 'light' | 'plain'
}>()

defineEmits<{
  click: []
}>()

const tagType = computed(() => {
  const colorMap: Record<string, string> = {
    '语文': 'danger',
    '数学': '',
    '英语': 'success',
    '物理': 'warning',
    '化学': 'info'
  }
  return colorMap[props.name] || ''
})
</script>

<style scoped lang="scss">
.category-tag {
  cursor: pointer;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
