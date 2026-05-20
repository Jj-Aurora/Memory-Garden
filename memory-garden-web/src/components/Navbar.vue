<template>
  <div class="navbar">
    <div class="navbar-left">
      <span class="navbar-title" @click="router.push('/')">记忆花园</span>
    </div>
    <div class="navbar-center">
      <router-link to="/" class="nav-link" active-class="active">
        <el-icon><House /></el-icon> 花园
      </router-link>
      <router-link to="/card/list" class="nav-link" active-class="active">
        <el-icon><Document /></el-icon> 卡片
      </router-link>
      <router-link to="/category" class="nav-link" active-class="active">
        <el-icon><Menu /></el-icon> 分类
      </router-link>
      <router-link to="/review" class="nav-link" active-class="active">
        <el-icon><Reading /></el-icon> 复习
        <el-badge v-if="pendingCount > 0" :value="pendingCount" :max="99" class="nav-badge" />
      </router-link>
      <router-link to="/badge" class="nav-link" active-class="active">
        <el-icon><Trophy /></el-icon> 徽章
      </router-link>
      <router-link to="/stats" class="nav-link" active-class="active">
        <el-icon><DataLine /></el-icon> 统计
      </router-link>
      <router-link to="/study-pack" class="nav-link" active-class="active">
        <el-icon><Box /></el-icon> 知识库
      </router-link>
    </div>
    <div class="navbar-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="userStore.userInfo?.avatarUrl || undefined">
            {{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}
          </el-avatar>
          <span class="user-name">{{ userStore.userInfo?.nickname || '用户' }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon> 个人中心
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <el-icon><SwitchButton /></el-icon> 退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  House, Document, Reading, Trophy, DataLine, Box,
  User, Menu, SwitchButton
} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useReviewStore } from '@/stores/review'

const router = useRouter()
const userStore = useUserStore()
const reviewStore = useReviewStore()

const pendingCount = computed(() => reviewStore.pendingCount)

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
})
</script>

<style scoped lang="scss">
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 24px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;

  &-left {
    display: flex;
    align-items: center;
  }

  &-title {
    font-size: 20px;
    font-weight: 600;
    color: #2d8c3c;
    cursor: pointer;
  }

  &-center {
    display: flex;
    align-items: center;
    gap: 4px;
  }

  &-right {
    display: flex;
    align-items: center;
  }
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 14px;
  border-radius: 8px;
  font-size: 14px;
  color: #606266;
  text-decoration: none;
  transition: all 0.2s;
  position: relative;

  &:hover {
    background: #f0f9eb;
    color: #2d8c3c;
  }

  &.active {
    background: #e8f5e9;
    color: #2d8c3c;
    font-weight: 500;
  }
}

.nav-badge {
  position: absolute;
  top: 2px;
  right: 4px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;

  &:hover {
    background: #f5f7fa;
  }
}

.user-name {
  font-size: 14px;
  color: #303133;
}
</style>
