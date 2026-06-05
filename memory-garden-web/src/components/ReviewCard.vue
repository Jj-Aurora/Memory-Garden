<template>
  <div class="review-card" :class="{ 'is-flipped': isFlipped }" @click="flip" tabindex="0"
    role="button" :aria-label="isFlipped ? '点击查看问题' : '点击查看答案'">
    <div class="review-card-inner">
      <div class="review-card-front">
        <div class="card-label">
          <svg class="label-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10" /><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3" />
            <line x1="12" y1="17" x2="12.01" y2="17" />
          </svg>
          问题
        </div>
        <div class="card-content">{{ frontContent }}</div>
        <div class="card-hint">
          <svg class="hint-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="15 3 21 3 21 9" /><path d="M9 21H3v-6" /><path d="M21 3l-7 7" />
            <path d="M3 21l7-7" />
          </svg>
          点击翻转查看答案
        </div>
      </div>
      <div class="review-card-back">
        <div class="card-label">
          <svg class="label-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
            <polyline points="22 4 12 14.01 9 11.01" />
          </svg>
          答案
        </div>
        <div class="card-content">{{ backContent }}</div>
        <div v-if="note" class="card-note">
          <svg class="note-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
            <polyline points="14 2 14 8 20 8" /><line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
          </svg>
          {{ note }}
        </div>
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
  perspective: 1200px;
  cursor: pointer;
  height: 340px;

  &-inner {
    position: relative;
    width: 100%;
    height: 100%;
    transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
    transform-style: preserve-3d;
  }

  &.is-flipped .review-card-inner {
    transform: rotateY(180deg);
  }

  &:focus-visible {
    outline: 2px solid var(--color-primary);
    outline-offset: 4px;
    border-radius: var(--radius-xl);
  }

  &-front,
  &-back {
    position: absolute;
    width: 100%;
    height: 100%;
    backface-visibility: hidden;
    border-radius: var(--radius-xl);
    padding: var(--space-2xl) var(--space-xl);
    display: flex;
    flex-direction: column;
    box-shadow: var(--shadow-lg);
  }

  &-front {
    background: linear-gradient(145deg, #e8f5e9, #c8e6c9);
    color: #1b5e20;
  }

  &-back {
    background: linear-gradient(145deg, #fff8e1, #ffecb3);
    color: #e65100;
    transform: rotateY(180deg);
  }
}

.card-label {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-semibold);
  opacity: 0.6;
  margin-bottom: var(--space-lg);
  text-transform: uppercase;
  letter-spacing: 1.5px;
}

.label-icon {
  width: 14px;
  height: 14px;
}

.card-content {
  flex: 1;
  font-size: var(--font-size-lg);
  line-height: var(--line-height-relaxed);
  overflow-y: auto;
  word-break: break-word;
}

.card-note {
  display: flex;
  align-items: flex-start;
  gap: var(--space-xs);
  margin-top: var(--space-base);
  padding-top: var(--space-base);
  border-top: 1px dashed rgba(121, 85, 72, 0.2);
  font-size: var(--font-size-sm);
  color: #795548;
  opacity: 0.85;
}

.note-icon {
  width: 14px;
  height: 14px;
  flex-shrink: 0;
  margin-top: 2px;
}

.card-hint {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-xs);
  margin-top: var(--space-base);
  font-size: var(--font-size-xs);
  opacity: 0.4;
}

.hint-icon {
  width: 12px;
  height: 12px;
}

@media (max-width: 768px) {
  .review-card {
    height: 300px;
  }

  .card-content {
    font-size: var(--font-size-md);
  }
}
</style>
