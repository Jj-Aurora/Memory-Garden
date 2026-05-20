<template>
  <div class="card-create-view">
    <h2>创建知识卡片</h2>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width: 600px">
      <el-form-item label="正面内容" prop="frontContent">
        <el-input v-model="form.frontContent" type="textarea" :rows="4" placeholder="输入问题" />
      </el-form-item>
      <el-form-item label="背面内容" prop="backContent">
        <el-input v-model="form.backContent" type="textarea" :rows="4" placeholder="输入答案" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.categoryId" placeholder="选择分类（可选）" clearable style="width: 100%">
          <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.note" type="textarea" :rows="2" placeholder="补充说明（可选）" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleCreate">创建卡片</el-button>
        <el-button @click="router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { cardApi } from '@/api/card'
import { categoryApi, type Category } from '@/api/category'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)
const categories = ref<Category[]>([])

const form = reactive({
  frontContent: '',
  backContent: '',
  categoryId: undefined as number | undefined,
  note: ''
})

const rules: FormRules = {
  frontContent: [{ required: true, message: '请输入正面内容', trigger: 'blur' }],
  backContent: [{ required: true, message: '请输入背面内容', trigger: 'blur' }]
}

async function handleCreate() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await cardApi.create({
      frontContent: form.frontContent,
      backContent: form.backContent,
      categoryId: form.categoryId,
      note: form.note || undefined
    })
    ElMessage.success('卡片创建成功，种子已种下！')
    router.push('/')
  } catch {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const res = await categoryApi.list()
  categories.value = res.data
})
</script>

<style scoped lang="scss">
.card-create-view {
  h2 {
    margin: 0 0 24px;
    color: #303133;
  }
}
</style>
