<template>
  <div class="main-layout">
    <Navbar />
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="page-fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup lang="ts">
import Navbar from './Navbar.vue'
</script>

<style scoped lang="scss">
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: var(--color-bg-page);
}

.main-content {
  flex: 1;
  padding: var(--content-padding);
  padding-top: var(--space-xl);
  max-width: var(--content-max-width);
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}

.page-fade-enter-active,
.page-fade-leave-active {
  transition: opacity 0.2s ease;
}

.page-fade-enter-from,
.page-fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
  .main-content {
    padding: var(--space-base);
    padding-top: var(--space-lg);
  }
}
</style>
