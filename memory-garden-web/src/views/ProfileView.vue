<template>
  <div class="profile-view">
    <h2 class="section-title">个人中心</h2>

    <div v-loading="loading" class="profile-content">
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
            <span class="streak-item">
              <svg class="streak-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z" />
              </svg>
              连续 {{ userStore.userInfo.currentStreak }} 天
            </span>
            <span class="streak-item">
              <svg class="streak-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
              </svg>
              最长 {{ userStore.userInfo.maxStreak }} 天
            </span>
          </div>
        </div>
      </div>

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
  max-width: 700px;
}

.profile-card {
  display: flex;
  align-items: center;
  gap: var(--space-xl);
  padding: var(--space-xl);
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--space-xl);
}

.profile-info {
  h3 {
    margin: 0 0 var(--space-xs);
    font-size: var(--font-size-xl);
    color: var(--color-text-primary);
    font-weight: var(--font-weight-bold);
  }

  .profile-username {
    font-size: var(--font-size-sm);
    color: var(--color-text-muted);
    margin: 0 0 var(--space-sm);
  }

  .profile-stats {
    display: flex;
    gap: var(--space-lg);
    font-size: var(--font-size-sm);
    color: var(--color-primary);
  }

  .streak-item {
    display: flex;
    align-items: center;
    gap: var(--space-xs);
  }

  .streak-icon {
    width: 14px;
    height: 14px;
  }
}

.profile-form {
  background: var(--color-bg-card);
  padding: var(--space-xl);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--space-xl);
}

.profile-badges {
  background: var(--color-bg-card);
  padding: var(--space-xl);
  border-radius: var(--radius-lg);
  border: 1px solid var(--color-border-light);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--space-xl);

  h3 {
    margin: 0 0 var(--space-base);
    font-size: var(--font-size-md);
    color: var(--color-text-primary);
    font-weight: var(--font-weight-semibold);
  }
}

.badge-grid {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-sm);
  margin-bottom: var(--space-base);
}

.badge-mini {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--space-sm);
  border-radius: var(--radius-md);
  background: var(--color-bg-hover);
  min-width: 60px;
  transition: all var(--transition-fast);

  &:hover {
    background: var(--color-bg-active);
    transform: translateY(-1px);
  }

  .badge-emoji {
    font-size: 24px;
  }

  .badge-name {
    font-size: 11px;
    color: var(--color-text-secondary);
    margin-top: var(--space-xs);
    text-align: center;
  }
}

.profile-logout {
  text-align: center;
}

@media (max-width: 768px) {
  .profile-card {
    flex-direction: column;
    text-align: center;
  }

  .profile-info .profile-stats {
    justify-content: center;
  }
}
</style>
