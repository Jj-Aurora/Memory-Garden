<template>
  <div class="review-card" :class="{ 'is-flipped': isFlipped }" @click="flip">
    <div class="review-card-inner">
      <div class="review-card-front">
        <div class="card-label">正面 - 问题</div>
        <div class="card-content">{{ frontContent }}</div>
        <div class="card-hint">点击翻转查看答案</div>
      </div>
      <div class="review-card-back">
        <div class="card-label">背面 - 答案</div>
        <div class="card-content">{{ backContent }}</div>
        <div v-if="note" class="card-note">备注：{{ note }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineProps<{
  frontContent: string
  backContent: string
  note?: string
}>()

const isFlipped = ref(false)

function flip() {
  isFlipped.value = !isFlipped.value
}

function reset() {
  isFlipped.value = false
}

defineExpose({ reset, isFlipped })
</script>

<style scoped lang="scss">
.review-card {
  perspective: 1000px;
  cursor: pointer;
  height: 320px;

  &-inner {
    position: relative;
    width: 100%;
    height: 100%;
    transition: transform 0.6s ease;
    transform-style: preserve-3d;
  }

  &.is-flipped .review-card-inner {
    transform: rotateY(180deg);
  }

  &-front,
  &-back {
    position: absolute;
    width: 100%;
    height: 100%;
    backface-visibility: hidden;
    border-radius: 16px;
    padding: 32px 24px;
    display: flex;
    flex-direction: column;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  }

  &-front {
    background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
    color: #2e7d32;
  }

  &-back {
    background: linear-gradient(135deg, #fff3e0, #ffe0b2);
    color: #e65100;
    transform: rotateY(180deg);
  }
}

.card-label {
  font-size: 13px;
  font-weight: 600;
  opacity: 0.7;
  margin-bottom: 16px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.card-content {
  flex: 1;
  font-size: 18px;
  line-height: 1.8;
  overflow-y: auto;
  word-break: break-word;
}

.card-note {
  margin-top: 12px;
  font-size: 13px;
  color: #795548;
  opacity: 0.8;
}

.card-hint {
  margin-top: 12px;
  font-size: 12px;
  opacity: 0.5;
  text-align: center;
}
</style>
