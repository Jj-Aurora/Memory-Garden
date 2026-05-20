<template>
  <div class="card-edit-view">
    <h2>编辑知识卡片</h2>
    <el-form v-loading="pageLoading" ref="formRef" :model="form" :rules="rules" label-width="100px"
      style="max-width: 600px">
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
        <el-button type="primary" :loading="loading" @click="handleUpdate">保存修改</el-button>
        <el-button type="danger" @click="handleDelete">删除卡片</el-button>
        <el-button @click="router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { cardApi } from '@/api/card'
import { categoryApi, type Category } from '@/api/category'

const router = useRouter()
const route = useRoute()
const formRef = ref<FormInstance>()
const loading = ref(false)
const pageLoading = ref(true)
const categories = ref<Category[]>([])

const cardId = Number(route.params.id)

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

async function handleUpdate() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await cardApi.update(cardId, {
      frontContent: form.frontContent,
      backContent: form.backContent,
      categoryId: form.categoryId,
      note: form.note || undefined
    })
    ElMessage.success('修改成功')
    router.push('/')
  } catch {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

async function handleDelete() {
  await ElMessageBox.confirm('删除后关联的植物也会被移除，确定要删除吗？', '确认删除', {
    type: 'warning'
  })
  await cardApi.delete(cardId)
  ElMessage.success('卡片已删除')
  router.push('/')
}

onMounted(async () => {
  try {
    const [cardRes, catRes] = await Promise.all([
      cardApi.getById(cardId),
      categoryApi.list()
    ])
    const card = cardRes.data
    form.frontContent = card.frontContent
    form.backContent = card.backContent
    form.categoryId = card.categoryId ?? undefined
    form.note = card.note || ''
    categories.value = catRes.data
  } finally {
    pageLoading.value = false
  }
})
</script>

<style scoped lang="scss">
.card-edit-view {
  h2 {
    margin: 0 0 24px;
    color: #303133;
  }
}
</style>
