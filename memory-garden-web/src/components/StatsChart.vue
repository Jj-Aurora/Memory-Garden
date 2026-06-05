<template>
  <div class="stats-chart">
    <div v-if="type === 'line'" ref="lineChartRef" class="chart-container"></div>
    <div v-if="type === 'pie'" ref="pieChartRef" class="chart-container"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'

const props = defineProps<{
  type: 'line' | 'pie'
  data: Record<string, number>
  title?: string
}>()

const lineChartRef = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
let chartInstance: echarts.ECharts | null = null

function initChart() {
  const el = props.type === 'line' ? lineChartRef.value : pieChartRef.value
  if (!el) return

  if (chartInstance) {
    chartInstance.dispose()
  }
  chartInstance = echarts.init(el)

  if (props.type === 'line') {
    renderLineChart()
  } else {
    renderPieChart()
  }
}

function renderLineChart() {
  if (!chartInstance) return
  const keys = Object.keys(props.data)
  const values = Object.values(props.data)

  chartInstance.setOption({
    title: {
      text: props.title || '',
      left: 'center',
      textStyle: { fontSize: 14, color: '#1a2e1a', fontWeight: 600 }
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#e8f2e5',
      textStyle: { color: '#1a2e1a', fontSize: 13 }
    },
    xAxis: {
      type: 'category',
      data: keys,
      axisLabel: { fontSize: 11, color: '#7c9472' },
      axisLine: { lineStyle: { color: '#d4e6d0' } }
    },
    yAxis: {
      type: 'value',
      minInterval: 1,
      axisLabel: { color: '#7c9472' },
      splitLine: { lineStyle: { color: '#e8f2e5' } }
    },
    series: [{
      type: 'line',
      data: values,
      smooth: true,
      areaStyle: {
        opacity: 0.15,
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#4caf50' },
          { offset: 1, color: '#f1f8e9' }
        ])
      },
      lineStyle: { color: '#2d8c3c', width: 2.5 },
      itemStyle: { color: '#2d8c3c' },
      symbolSize: 6
    }],
    grid: { left: 40, right: 20, top: 40, bottom: 30 }
  })
}

function renderPieChart() {
  if (!chartInstance) return
  const stageNames: Record<string, string> = {
    '1': '种子', '2': '发芽', '3': '成长', '4': '开花', '5': '结果'
  }
  const colors = ['#8d6e63', '#66bb6a', '#43a047', '#e91e63', '#ff5722']
  const pieData = Object.entries(props.data).map(([key, value], index) => ({
    name: stageNames[key] || `阶段${key}`,
    value,
    itemStyle: { color: colors[parseInt(key) - 1] || colors[index % colors.length] }
  }))

  chartInstance.setOption({
    title: {
      text: props.title || '',
      left: 'center',
      textStyle: { fontSize: 14, color: '#1a2e1a', fontWeight: 600 }
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)',
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#e8f2e5',
      textStyle: { color: '#1a2e1a', fontSize: 13 }
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: pieData,
      label: { fontSize: 12, color: '#4a6741' },
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(45,140,60,0.2)' }
      }
    }]
  })
}

function handleResize() {
  chartInstance?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose()
})

watch(() => props.data, () => {
  initChart()
}, { deep: true })
</script>

<style scoped lang="scss">
.chart-container {
  width: 100%;
  height: 300px;
}

@media (max-width: 768px) {
  .chart-container {
    height: 250px;
  }
}
</style>
