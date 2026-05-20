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
      textStyle: { fontSize: 14 }
    },
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: keys,
      axisLabel: { fontSize: 11 }
    },
    yAxis: {
      type: 'value',
      minInterval: 1
    },
    series: [{
      type: 'line',
      data: values,
      smooth: true,
      areaStyle: { opacity: 0.15 },
      lineStyle: { color: '#67c23a', width: 2 },
      itemStyle: { color: '#67c23a' }
    }],
    grid: { left: 40, right: 20, top: 40, bottom: 30 }
  })
}

function renderPieChart() {
  if (!chartInstance) return
  const stageNames: Record<string, string> = {
    '1': '种子', '2': '发芽', '3': '成长', '4': '开花', '5': '结果'
  }
  const colors = ['#a5d6a7', '#66bb6a', '#43a047', '#e91e63', '#ff5722']
  const pieData = Object.entries(props.data).map(([key, value], index) => ({
    name: stageNames[key] || `阶段${key}`,
    value,
    itemStyle: { color: colors[parseInt(key) - 1] || colors[index % colors.length] }
  }))

  chartInstance.setOption({
    title: {
      text: props.title || '',
      left: 'center',
      textStyle: { fontSize: 14 }
    },
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: pieData,
      label: { fontSize: 12 },
      emphasis: {
        itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.2)' }
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
</style>
