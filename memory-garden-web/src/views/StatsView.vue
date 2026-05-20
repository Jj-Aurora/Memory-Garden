<template>
  <div class="stats-view">
    <h2>学习统计</h2>

    <div v-loading="loading">
      <!-- 今日数据 -->
      <div v-if="todayStats" class="today-stats">
        <div class="stat-card">
          <span class="stat-value">{{ todayStats.todayNewCards }}</span>
          <span class="stat-label">今日新增</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ todayStats.todayReviewCount }}</span>
          <span class="stat-label">今日复习</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ todayStats.currentStreak }}</span>
          <span class="stat-label">连续打卡</span>
        </div>
        <div class="stat-card">
          <span class="stat-value">{{ todayStats.maxStreak }}</span>
          <span class="stat-label">最长连续</span>
        </div>
      </div>

      <!-- 趋势图 -->
      <div class="chart-section">
        <h3>复习趋势</h3>
        <StatsChart v-if="trendData" type="line" :data="trendData" title="近7天复习趋势" />
      </div>

      <!-- 阶段分布 -->
      <div class="chart-section">
        <h3>植物阶段分布</h3>
        <StatsChart v-if="stageDistribution" type="pie" :data="stageDistribution" title="各阶段植物数量" />
      </div>

      <!-- 打卡日历 -->
      <div class="chart-section">
        <h3>打卡日历</h3>
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

    // 从趋势数据中提取打卡日期
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
  h2 {
    margin: 0 0 24px;
    color: #303133;
  }

  h3 {
    margin: 0 0 12px;
    font-size: 16px;
    color: #303133;
  }
}

.today-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  flex: 1;
  min-width: 100px;

  .stat-value {
    font-size: 28px;
    font-weight: 700;
    color: #2d8c3c;
  }

  .stat-label {
    font-size: 13px;
    color: #909399;
    margin-top: 4px;
  }
}

.chart-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
</style>
