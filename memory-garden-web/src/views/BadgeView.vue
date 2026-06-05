<template>
  <div class="badge-view">
    <h2 class="section-title">成就徽章</h2>
    <p class="view-subtitle">记录你的每一个里程碑</p>

    <div class="badge-tabs">
      <el-radio-group v-model="activeTab" @change="loadBadges">
        <el-radio-button value="all">全部徽章</el-radio-button>
        <el-radio-button value="earned">已获得</el-radio-button>
      </el-radio-group>
    </div>

    <div v-loading="loading" class="badge-list">
      <BadgeItem v-for="badge in displayBadges" :key="badge.id" :badge="badge" />
      <el-empty v-if="displayBadges.length === 0" description="暂无徽章" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { badgeApi, type BadgeVO } from '@/api/badge'
import BadgeItem from '@/components/BadgeItem.vue'

const loading = ref(false)
const activeTab = ref('all')
const allBadges = ref<BadgeVO[]>([])

const displayBadges = computed(() => {
  if (activeTab.value === 'earned') {
    return allBadges.value.filter(b => b.earned)
  }
  return allBadges.value
})

async function loadBadges() {
  loading.value = true
  try {
    const res = await badgeApi.getAllBadges()
    allBadges.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(loadBadges)
</script>

<style scoped lang="scss">
.badge-view {
  max-width: 700px;
}

.view-subtitle {
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin: calc(var(--space-xs) * -1) 0 var(--space-xl);
}

.badge-tabs {
  margin-bottom: var(--space-xl);
}

.badge-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}
</style>
