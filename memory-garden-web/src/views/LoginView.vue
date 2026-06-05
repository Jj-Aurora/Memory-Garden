<template>
  <div class="login-page">
    <div class="login-bg-decoration">
      <div class="deco-circle deco-1"></div>
      <div class="deco-circle deco-2"></div>
      <div class="deco-circle deco-3"></div>
    </div>
    <div class="login-card">
      <div class="login-brand">
        <svg class="brand-logo" viewBox="0 0 48 48" fill="none">
          <circle cx="24" cy="24" r="22" stroke="var(--color-primary)" stroke-width="2.5" />
          <path d="M24 10c-5 0-10 5-10 10s5 10 10 10 10-5 10-10-5-10-10-10z" fill="var(--color-primary-bg)"
            stroke="var(--color-primary)" stroke-width="1.5" />
          <path d="M24 30v8M20 34h8" stroke="var(--color-primary)" stroke-width="2" stroke-linecap="round" />
        </svg>
        <h2 class="login-title">记忆花园</h2>
      </div>
      <p class="login-subtitle">用知识浇灌，让记忆开花</p>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large"
            show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" class="login-btn" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        还没有账号？<router-link to="/register" class="link">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度为4-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ]
}

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/')
  } catch {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(145deg, #e8f5e9 0%, #c8e6c9 40%, #a5d6a7 100%);
  position: relative;
  overflow: hidden;
}

.login-bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;

  &.deco-1 {
    width: 400px;
    height: 400px;
    background: var(--color-primary);
    top: -100px;
    right: -100px;
  }

  &.deco-2 {
    width: 300px;
    height: 300px;
    background: var(--color-primary-light);
    bottom: -80px;
    left: -80px;
  }

  &.deco-3 {
    width: 150px;
    height: 150px;
    background: var(--color-accent-warm);
    top: 30%;
    left: 10%;
  }
}

.login-card {
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

.login-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-sm);
  margin-bottom: var(--space-sm);
}

.brand-logo {
  width: 40px;
  height: 40px;
}

.login-title {
  text-align: center;
  font-size: var(--font-size-2xl);
  color: var(--color-primary);
  margin: 0;
  font-weight: var(--font-weight-bold);
  letter-spacing: -0.02em;
}

.login-subtitle {
  text-align: center;
  color: var(--color-text-muted);
  font-size: var(--font-size-sm);
  margin: 0 0 var(--space-2xl);
}

.login-btn {
  width: 100%;
  height: var(--touch-min);
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  border-radius: var(--radius-md);
}

.login-footer {
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
  .login-card {
    width: calc(100% - 32px);
    padding: var(--space-xl);
    margin: var(--space-base);
  }
}
</style>
