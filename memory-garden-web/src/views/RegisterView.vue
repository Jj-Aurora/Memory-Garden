<template>
  <div class="register-page">
    <div class="register-bg-decoration">
      <div class="deco-circle deco-1"></div>
      <div class="deco-circle deco-2"></div>
    </div>
    <div class="register-card">
      <div class="register-brand">
        <svg class="brand-logo" viewBox="0 0 48 48" fill="none">
          <circle cx="24" cy="24" r="22" stroke="var(--color-primary)" stroke-width="2.5" />
          <path d="M24 10c-5 0-10 5-10 10s5 10 10 10 10-5 10-10-5-10-10-10z" fill="var(--color-primary-bg)"
            stroke="var(--color-primary)" stroke-width="1.5" />
          <path d="M24 30v8M20 34h8" stroke="var(--color-primary)" stroke-width="2" stroke-linecap="round" />
        </svg>
        <h2 class="register-title">注册记忆花园</h2>
      </div>
      <p class="register-subtitle">开始你的知识花园之旅</p>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" @submit.prevent="handleRegister">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large"
            show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" :prefix-icon="Lock"
            size="large" show-password />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="form.nickname" placeholder="昵称（选填）" :prefix-icon="UserFilled" size="large" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" class="register-btn" @click="handleRegister">
            注 册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-footer">
        已有账号？<router-link to="/login" class="link">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

const validateConfirmPassword = (_rule: any, value: string, callback: any) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度为4-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

async function handleRegister() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.register({
      username: form.username,
      password: form.password,
      nickname: form.nickname || undefined
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.register-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(145deg, #e8f5e9 0%, #c8e6c9 40%, #a5d6a7 100%);
  position: relative;
  overflow: hidden;
}

.register-bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;

  &.deco-1 {
    width: 350px;
    height: 350px;
    background: var(--color-primary);
    bottom: -100px;
    right: -80px;
  }

  &.deco-2 {
    width: 250px;
    height: 250px;
    background: var(--color-primary-light);
    top: -60px;
    left: -60px;
  }
}

.register-card {
  width: 420px;
  padding: var(--space-3xl);
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  border: 1px solid rgba(255, 255, 255, 0.6);
  position: relative;
  z-index: 1;
}

.register-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-sm);
  margin-bottom: var(--space-sm);
}

.brand-logo {
  width: 36px;
  height: 36px;
}

.register-title {
  text-align: center;
  font-size: var(--font-size-xl);
  color: var(--color-primary);
  margin: 0;
  font-weight: var(--font-weight-bold);
}

.register-subtitle {
  text-align: center;
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  margin: 0 0 var(--space-2xl);
}

.register-btn {
  width: 100%;
  height: var(--touch-min);
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  border-radius: var(--radius-md);
}

.register-footer {
  text-align: center;
  font-size: var(--font-size-sm);
  color: var(--color-text-muted);
  margin-top: var(--space-lg);

  .link {
    color: var(--color-primary);
    font-weight: var(--font-weight-semibold);
    transition: opacity var(--transition-fast);

    &:hover {
      opacity: 0.8;
    }
  }
}

@media (max-width: 480px) {
  .register-card {
    width: calc(100% - 32px);
    padding: var(--space-xl);
    margin: var(--space-base);
  }
}
</style>
