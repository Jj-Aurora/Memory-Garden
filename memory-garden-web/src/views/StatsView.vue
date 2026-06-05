<template>
  <div class="stats-view">
    <h2 class="section-title">学习统计</h2>

    <div v-loading="loading">
      <!-- 今日数据 -->
      <div v-if="todayStats" class="today-stats">
        <div class="stat-card stat-new">
          <svg class="stat-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          <span class="stat-value">{{ todayStats.todayNewCards }}</span>
          <span class="stat-label">今日新增</span>
        </div>
        <div class="stat-card stat-review">
          <svg class="stat-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
            <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
          </svg>
          <span class="stat-value">{{ todayStats.todayReviewCount }}</span>
          <span class="stat-label">今日复习</span>
        </div>
        <div class="stat-card stat-streak">
          <svg class="stat-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z" />
          </svg>
          <span class="stat-value">{{ todayStats.currentStreak }}</span>
          <span class="stat-label">连续打卡</span>
        </div>
        <div class="stat-card stat-max">
          <svg class="stat-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
          </svg>
          <span class="stat-value">{{ todayStats.maxStreak }}</span>
          <span class="stat-label">最长连续</span>
        </div>
      </div>

      <!-- 趋势图 -->
      <div class="chart-section">
        <h3 class="chart-title">复习趋势</h3>
        <StatsChart v-if="trendData" type="line" :data="trendData" title="近7天复习趋势" />
      </div>

      <!-- 阶段分布 -->
      <div class="chart-section">
        <h3 class="chart-title">植物阶段分布</h3>
        <StatsChart v-if="stageDistribution" type="pie" :data="stageDistribution" title="各阶段植物数量" />
      </div>

      <!-- 打卡日历 -->
      <div class="chart-section">
        <h3 class="chart-title">打卡日历</h3>
        <StreakCalendar :checked-dates="checkedDates" :last-check-in="lastCheckIn" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { statsApi, type StatsVO } from '@/api/stats'
import StatsChart from '@/components/StatsChart.vue'
import StreakCalendar from '@/components/StreakCalendar.vue'

const loading = ref(false)
const todayStats = ref<StatsVO | null>(null)
const trendData = ref<Record<string, number> | null>(null)
const stageDistribution = ref<Record<number, number> | null>(null)
const checkedDates = ref<string[]>([])
const lastCheckIn = ref<string>('')

async function loadStats() {
  loading.value = true
  try {
    const [todayRes, trendRes, distRes] = await Promise.all([
      statsApi.getToday(),
      statsApi.getTrend(7),
      statsApi.getStageDistribution()
    ])
    todayStats.value = todayRes.data
    trendData.value = trendRes.data
    stageDistribution.value = distRes.data

    if (trendRes.data) {
      checkedDates.value = Object.keys(trendRes.data).filter(k => (trendRes.data as Record<string, number>)[k] > 0)
    }
    if (todayStats.value) {
      lastCheckIn.value = todayStats.value.currentStreak > 0
        ? new Date().toISOString().split('T')[0]
        : ''
    }
  } finally {
    loading.value = false
  }
}

onMounted(loadStats)
</script>

<style scoped lang="scss">
.stats-view {
  max-width: 800px;
  margin: 0 auto;
}

.today-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: var(--space-md);
  margin-bottom: var(--space-xl);
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-lg) var(--space-base);
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border-light);
  transition: all var(--transition-normal);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-primary);
  }

  .stat-icon {
    width: 24px;
    height: 24px;
    margin-bottom: var(--space-sm);
  }

  .stat-value {
    font-size: var(--font-size-2xl);
    font-weight: var(--font-weight-bold);
    line-height: var(--line-height-tight);
  }

  .stat-label {
    font-size: var(--font-size-xs);
    color: var(--color-text-muted);
    margin-top: var(--space-xs);
  }

  &.stat-new {
    .stat-icon, .stat-value { color: var(--color-primary); }
    &::before { background: var(--color-primary); }
  }

  &.stat-review {
    .stat-icon, .stat-value { color: var(--color-sprout); }
    &::before { background: var(--color-sprout); }
  }

  &.stat-streak {
    .stat-icon, .stat-value { color: var(--color-accent-warm); }
    &::before { background: var(--color-accent-warm); }
  }

  &.stat-max {
    .stat-icon, .stat-value { color: var(--color-blooming); }
    &::before { background: var(--color-blooming); }
  }
}

.chart-section {
  margin-bottom: var(--space-xl);
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
  border: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
}

.chart-title {
  margin: 0 0 var(--space-base);
  font-size: var(--font-size-md);
  color: var(--color-text-primary);
  font-weight: var(--font-weight-semibold);
}

@media (max-width: 768px) {
  .today-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-card {
    padding: var(--space-base);

    .stat-value {
      font-size: var(--font-size-xl);
    }
  }
}
</style>
