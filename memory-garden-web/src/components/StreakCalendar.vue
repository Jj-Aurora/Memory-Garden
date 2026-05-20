<template>
  <div class="streak-calendar">
    <div class="calendar-header">
      <el-button :icon="ArrowLeft" size="small" @click="prevMonth" />
      <span class="calendar-title">{{ currentYear }}年{{ currentMonth }}月</span>
      <el-button :icon="ArrowRight" size="small" @click="nextMonth" />
    </div>
    <div class="calendar-weekdays">
      <span v-for="d in weekdays" :key="d" class="weekday">{{ d }}</span>
    </div>
    <div class="calendar-grid">
      <div
        v-for="(day, index) in calendarDays"
        :key="index"
        class="calendar-day"
        :class="{
          'is-empty': !day.date,
          'is-checked': day.checked,
          'is-today': day.isToday
        }"
      >
        <span v-if="day.date" class="day-number">{{ day.day }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

const props = defineProps<{
  checkedDates: string[]
  lastCheckIn?: string
}>()

const currentYear = ref(new Date().getFullYear())
const currentMonth = ref(new Date().getMonth() + 1)

const weekdays = ['一', '二', '三', '四', '五', '六', '日']

const checkedSet = computed(() => new Set(props.checkedDates))

interface CalendarDay {
  date: string
  day: number
  checked: boolean
  isToday: boolean
}

const calendarDays = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value
  const firstDay = new Date(year, month - 1, 1)
  const lastDay = new Date(year, month, 0)
  const daysInMonth = lastDay.getDate()

  let startWeekday = firstDay.getDay()
  if (startWeekday === 0) startWeekday = 7
  startWeekday -= 1

  const today = new Date()
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`

  const days: CalendarDay[] = []

  for (let i = 0; i < startWeekday; i++) {
    days.push({ date: '', day: 0, checked: false, isToday: false })
  }

  for (let d = 1; d <= daysInMonth; d++) {
    const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(d).padStart(2, '0')}`
    days.push({
      date: dateStr,
      day: d,
      checked: checkedSet.value.has(dateStr),
      isToday: dateStr === todayStr
    })
  }

  return days
})

function prevMonth() {
  if (currentMonth.value === 1) {
    currentMonth.value = 12
    currentYear.value--
  } else {
    currentMonth.value--
  }
}

function nextMonth() {
  if (currentMonth.value === 12) {
    currentMonth.value = 1
    currentYear.value++
  } else {
    currentMonth.value++
  }
}
</script>

<style scoped lang="scss">
.streak-calendar {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.calendar-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.calendar-weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
  margin-bottom: 8px;
}

.weekday {
  text-align: center;
  font-size: 12px;
  color: #909399;
  font-weight: 500;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  font-size: 13px;
  transition: all 0.2s;

  &.is-empty {
    background: transparent;
  }

  &.is-checked {
    background: #67c23a;
    color: #fff;

    .day-number {
      font-weight: 600;
    }
  }

  &.is-today:not(.is-checked) {
    border: 2px solid #67c23a;
  }

  &:not(.is-empty):not(.is-checked) {
    background: #f5f7fa;
  }

  .day-number {
    line-height: 1;
  }
}
</style>
