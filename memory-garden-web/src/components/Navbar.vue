<template>
  <nav class="navbar" :class="{ 'is-scrolled': isScrolled }">
    <div class="navbar-inner">
      <div class="navbar-left">
        <span class="navbar-brand" @click="router.push('/')">
          <svg class="brand-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10 10-4.5 10-10S17.5 2 12 2z" />
            <path d="M12 6c-2 0-4 2-4 4s2 4 4 4 4-2 4-4-2-4-4-4z" />
            <path d="M12 14v4" />
            <path d="M10 16h4" />
          </svg>
          <span class="brand-text">记忆花园</span>
        </span>
      </div>

      <!-- Desktop Navigation -->
      <div class="navbar-center" :class="{ 'is-open': mobileMenuOpen }">
        <router-link to="/" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
            <polyline points="9 22 9 12 15 12 15 22" />
          </svg>
          <span>花园</span>
        </router-link>
        <router-link to="/card/list" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="2" y="3" width="20" height="18" rx="2" />
            <line x1="8" y1="3" x2="8" y2="21" />
          </svg>
          <span>卡片</span>
        </router-link>
        <router-link to="/category" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="8" y1="6" x2="21" y2="6" />
            <line x1="8" y1="12" x2="21" y2="12" />
            <line x1="8" y1="18" x2="21" y2="18" />
            <line x1="3" y1="6" x2="3.01" y2="6" />
            <line x1="3" y1="12" x2="3.01" y2="12" />
            <line x1="3" y1="18" x2="3.01" y2="18" />
          </svg>
          <span>分类</span>
        </router-link>
        <router-link to="/review" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
            <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
          </svg>
          <span>复习</span>
          <span v-if="pendingCount > 0" class="nav-badge">{{ pendingCount > 99 ? '99+' : pendingCount }}</span>
        </router-link>
        <router-link to="/badge" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="8" r="7" />
            <polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88" />
          </svg>
          <span>徽章</span>
        </router-link>
        <router-link to="/stats" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="20" x2="18" y2="10" />
            <line x1="12" y1="20" x2="12" y2="4" />
            <line x1="6" y1="20" x2="6" y2="14" />
          </svg>
          <span>统计</span>
        </router-link>
        <router-link to="/study-pack" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
            <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
            <line x1="12" y1="22.08" x2="12" y2="12" />
          </svg>
          <span>知识库</span>
        </router-link>
      </div>

      <div class="navbar-right">
        <el-dropdown trigger="click" @command="handleCommand">
          <div class="user-info" tabindex="0" role="button" aria-label="用户菜单">
            <el-avatar :size="34" :src="userStore.userInfo?.avatarUrl || undefined">
              {{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <span class="user-name">{{ userStore.userInfo?.nickname || '用户' }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <svg class="dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                  <circle cx="12" cy="7" r="4" />
                </svg>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <svg class="dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                  <polyline points="16 17 21 12 16 7" />
                  <line x1="21" y1="12" x2="9" y2="12" />
                </svg>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <!-- Mobile menu toggle -->
        <button class="mobile-toggle" :class="{ 'is-active': mobileMenuOpen }" @click="toggleMobileMenu"
          aria-label="切换导航菜单">
          <span class="hamburger-line"></span>
          <span class="hamburger-line"></span>
          <span class="hamburger-line"></span>
        </button>
      </div>
    </div>

    <!-- Mobile overlay -->
    <div v-if="mobileMenuOpen" class="mobile-overlay" @click="closeMobileMenu"></div>
  </nav>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useReviewStore } from '@/stores/review'

const router = useRouter()
const userStore = useUserStore()
const reviewStore = useReviewStore()
const mobileMenuOpen = ref(false)
const isScrolled = ref(false)

const pendingCount = computed(() => reviewStore.pendingCount)

function toggleMobileMenu() {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

function closeMobileMenu() {
  mobileMenuOpen.value = false
}

function handleScroll() {
  isScrolled.value = window.scrollY > 10
}

async function handleCommand(command: string) {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    await ElMessageBox.confirm('确定要退出登录吗？', '确认', { type: 'warning' })
    userStore.logout()
    router.push('/login')
  }
}

onMounted(() => {
  if (userStore.token) {
    userStore.getCurrentUser()
    reviewStore.fetchPendingCount()
  }
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})

// Close mobile menu on route change
router.afterEach(() => {
  closeMobileMenu()
})
</script>

<style scoped lang="scss">
.navbar {
  position: sticky;
  top: 0;
  z-index: var(--z-sticky);
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid transparent;
  transition: all var(--transition-normal);

  &.is-scrolled {
    background: rgba(255, 255, 255, 0.95);
    border-bottom-color: var(--color-border-light);
    box-shadow: var(--shadow-sm);
  }
}

.navbar-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--navbar-height);
  padding: 0 var(--content-padding);
  max-width: var(--content-max-width);
  margin: 0 auto;
  width: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  cursor: pointer;
  transition: opacity var(--transition-fast);

  &:hover {
    opacity: 0.85;
  }
}

.brand-icon {
  width: 28px;
  height: 28px;
  color: var(--color-primary);
}

.brand-text {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--color-primary);
  letter-spacing: -0.02em;
}

.navbar-center {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 14px;
  border-radius: var(--radius-md);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: all var(--transition-fast);
  position: relative;
  cursor: pointer;
  white-space: nowrap;

  &:hover {
    background: var(--color-bg-hover);
    color: var(--color-primary);
  }

  &.active {
    background: var(--color-bg-active);
    color: var(--color-primary);
    font-weight: var(--font-weight-semibold);
  }
}

.nav-icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}

.nav-badge {
  position: absolute;
  top: 2px;
  right: 4px;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  font-size: 10px;
  font-weight: var(--font-weight-bold);
  line-height: 18px;
  text-align: center;
  color: #fff;
  background: var(--color-accent-danger);
  border-radius: var(--radius-full);
  animation: badge-pulse 2s ease-in-out infinite;
}

@keyframes badge-pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  cursor: pointer;
  padding: var(--space-xs) var(--space-sm);
  border-radius: var(--radius-md);
  transition: background var(--transition-fast);

  &:hover {
    background: var(--color-bg-hover);
  }
}

.user-name {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-primary);
}

.dropdown-icon {
  width: 16px;
  height: 16px;
  margin-right: 4px;
  vertical-align: -2px;
}

/* Mobile Toggle */
.mobile-toggle {
  display: none;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: var(--touch-min);
  height: var(--touch-min);
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  gap: 5px;

  .hamburger-line {
    display: block;
    width: 20px;
    height: 2px;
    background: var(--color-text-secondary);
    border-radius: 1px;
    transition: all var(--transition-normal);
  }

  &.is-active {
    .hamburger-line:nth-child(1) {
      transform: translateY(7px) rotate(45deg);
    }
    .hamburger-line:nth-child(2) {
      opacity: 0;
    }
    .hamburger-line:nth-child(3) {
      transform: translateY(-7px) rotate(-45deg);
    }
  }
}

.mobile-overlay {
  display: none;
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: calc(var(--z-sticky) - 1);
}

/* Mobile Responsive */
@media (max-width: 768px) {
  .mobile-toggle {
    display: flex;
  }

  .mobile-overlay {
    display: block;
  }

  .navbar-center {
    position: fixed;
    top: var(--navbar-height);
    left: 0;
    right: 0;
    bottom: 0;
    flex-direction: column;
    align-items: stretch;
    gap: 0;
    padding: var(--space-base);
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(12px);
    transform: translateX(100%);
    transition: transform var(--transition-normal);
    z-index: var(--z-overlay);
    overflow-y: auto;

    &.is-open {
      transform: translateX(0);
    }

    .nav-link {
      padding: var(--space-md) var(--space-base);
      font-size: var(--font-size-md);
      border-radius: var(--radius-md);

      .nav-icon {
        width: 20px;
        height: 20px;
      }
    }
  }

  .user-name {
    display: none;
  }
}

@media (max-width: 1024px) and (min-width: 769px) {
  .nav-link span:not(.nav-badge) {
    display: none;
  }

  .nav-link {
    padding: 10px;
  }

  .nav-icon {
    width: 20px;
    height: 20px;
  }
}
</style>
