<template>
  <div class="profile-view">
    <h2>个人中心</h2>

    <div v-loading="loading" class="profile-content">
      <!-- 用户信息卡片 -->
      <div v-if="userStore.userInfo" class="profile-card">
        <div class="profile-avatar">
          <el-avatar :size="80" :src="userStore.userInfo.avatarUrl || undefined">
            {{ userStore.userInfo.nickname?.charAt(0) || 'U' }}
          </el-avatar>
        </div>
        <div class="profile-info">
          <h3>{{ userStore.userInfo.nickname || userStore.userInfo.username }}</h3>
          <p class="profile-username">@{{ userStore.userInfo.username }}</p>
          <div class="profile-stats">
            <span>连续打卡 {{ userStore.userInfo.currentStreak }} 天</span>
            <span>最长 {{ userStore.userInfo.maxStreak }} 天</span>
          </div>
        </div>
      </div>

      <!-- 修改信息 -->
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" style="max-width: 500px"
        class="profile-form">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="修改昵称" />
        </el-form-item>
        <el-form-item label="头像URL">
          <el-input v-model="form.avatarUrl" placeholder="输入头像图片URL" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>

      <!-- 徽章概览 -->
      <div class="profile-badges">
        <h3>我的徽章</h3>
        <div v-loading="badgesLoading" class="badge-grid">
          <div v-for="badge in earnedBadges" :key="badge.id" class="badge-mini" :title="badge.name">
            <span class="badge-emoji">{{ getBadgeEmoji(badge.icon) }}</span>
            <span class="badge-name">{{ badge.name }}</span>
          </div>
          <el-empty v-if="earnedBadges.length === 0 && !badgesLoading" description="还没有获得徽章" :image-size="60" />
        </div>
        <el-button text type="primary" @click="router.push('/badge')">查看全部徽章</el-button>
      </div>

      <!-- 退出登录 -->
      <div class="profile-logout">
        <el-button type="danger" @click="handleLogout">退出登录</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { badgeApi, type BadgeVO } from '@/api/badge'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const saving = ref(false)
const badgesLoading = ref(false)
const earnedBadges = ref<BadgeVO[]>([])

const form = reactive({
  nickname: '',
  avatarUrl: ''
})

const rules: FormRules = {
  nickname: [{ min: 1, max: 50, message: '昵称长度为1-50个字符', trigger: 'blur' }]
}

function getBadgeEmoji(icon: string): string {
  const map: Record<string, string> = {
    'first_plant': '🌱', 'streak_7': '🔥', 'streak_30': '🔥',
    'blooming_5': '🌸', 'first_fruit': '🍎', 'total_50': '🌳',
    'revive_1': '💚', 'category_3': '🏷️'
  }
  return map[icon] || '🏆'
}

async function handleSave() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    await userStore.updateProfile(form.nickname || undefined, form.avatarUrl || undefined)
    ElMessage.success('修改成功')
  } finally {
    saving.value = false
  }
}

async function handleLogout() {
  await ElMessageBox.confirm('确定要退出登录吗？', '确认', { type: 'warning' })
  userStore.logout()
  router.push('/login')
}

onMounted(async () => {
  loading.value = true
  try {
    const user = await userStore.getCurrentUser()
    form.nickname = user.nickname || ''
    form.avatarUrl = user.avatarUrl || ''

    badgesLoading.value = true
    const badgeRes = await badgeApi.getMyBadges()
    earnedBadges.value = badgeRes.data
    badgesLoading.value = false
  } finally {
    loading.value = false
  }
})
</script>

<style scoped lang="scss">
.profile-view {
  h2 {
    margin: 0 0 24px;
    color: #303133;
  }
}

.profile-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  margin-bottom: 24px;
}

.profile-info {
  h3 {
    margin: 0 0 4px;
    font-size: 20px;
    color: #303133;
  }

  .profile-username {
    font-size: 14px;
    color: #909399;
    margin: 0 0 8px;
  }

  .profile-stats {
    display: flex;
    gap: 16px;
    font-size: 13px;
    color: #67c23a;
  }
}

.profile-form {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  margin-bottom: 24px;
}

.profile-badges {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  margin-bottom: 24px;

  h3 {
    margin: 0 0 16px;
    font-size: 16px;
    color: #303133;
  }
}

.badge-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 12px;
}

.badge-mini {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px;
  border-radius: 8px;
  background: #f5f7fa;
  min-width: 60px;

  .badge-emoji {
    font-size: 24px;
  }

  .badge-name {
    font-size: 11px;
    color: #606266;
    margin-top: 4px;
    text-align: center;
  }
}

.profile-logout {
  text-align: center;
}
</style>
