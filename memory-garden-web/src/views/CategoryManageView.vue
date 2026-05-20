<template>
  <div class="category-manage-view">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-info">
        <h2>分类管理</h2>
        <p class="header-desc">整理知识领域，让花园井然有序</p>
      </div>
      <el-button type="primary" class="add-btn" @click="showAddDialog">
        <el-icon><Plus /></el-icon> 新增分类
      </el-button>
    </div>

    <!-- 统计概览 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-value">{{ categories.length }}</span>
        <span class="stat-label">分类总数</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ categoriesWithIcon }}</span>
        <span class="stat-label">已设图标</span>
      </div>
    </div>

    <!-- 分类卡片列表 -->
    <div v-loading="loading" class="category-grid">
      <div v-for="(cat, index) in categories" :key="cat.id" class="category-card">
        <div class="card-icon" :style="{ background: getCardColor(index) }">
          <span class="icon-text">{{ cat.icon || cat.name.charAt(0) }}</span>
        </div>
        <div class="card-body">
          <h3 class="card-name">{{ cat.name }}</h3>
          <span class="card-meta">排序 #{{ cat.sortOrder }}</span>
        </div>
        <div class="card-actions">
          <el-tooltip content="编辑" placement="top">
            <el-button text circle @click="showEditDialog(cat)">
              <el-icon :size="16"><Edit /></el-icon>
            </el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button text circle type="danger" @click="handleDelete(cat)">
              <el-icon :size="16"><Delete /></el-icon>
            </el-button>
          </el-tooltip>
        </div>
      </div>

      <!-- 空状态 -->
      <el-empty v-if="categories.length === 0 && !loading" description="还没有分类，点击右上角创建第一个吧" />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="420px"
      :close-on-click-modal="false" class="category-dialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="72px" label-position="left">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="输入分类名称" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon" placeholder="图标标识（可选）" maxlength="20" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" controls-position="right" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          {{ isEdit ? '保存修改' : '创建分类' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { categoryApi, type Category } from '@/api/category'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref<number>(0)
const categories = ref<Category[]>([])
const formRef = ref<FormInstance>()

const form = reactive({
  name: '',
  icon: '',
  sortOrder: 0
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const categoriesWithIcon = computed(() => categories.value.filter(c => c.icon).length)

const CARD_COLORS = [
  '#2d8c3c', '#409eff', '#e6a23c', '#f56c6c',
  '#67c23a', '#909399', '#b37feb', '#36cfc9'
]

function getCardColor(index: number): string {
  return CARD_COLORS[index % CARD_COLORS.length]
}

async function loadCategories() {
  loading.value = true
  try {
    const res = await categoryApi.list()
    categories.value = res.data
  } finally {
    loading.value = false
  }
}

function showAddDialog() {
  isEdit.value = false
  form.name = ''
  form.icon = ''
  form.sortOrder = categories.value.length
  dialogVisible.value = true
}

function showEditDialog(row: Category) {
  isEdit.value = true
  editId.value = row.id
  form.name = row.name
  form.icon = row.icon || ''
  form.sortOrder = row.sortOrder
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      await categoryApi.update(editId.value, {
        name: form.name,
        icon: form.icon || undefined,
        sortOrder: form.sortOrder
      })
      ElMessage.success('修改成功')
    } else {
      await categoryApi.create({
        name: form.name,
        icon: form.icon || undefined,
        sortOrder: form.sortOrder
      })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadCategories()
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row: Category) {
  await ElMessageBox.confirm(
    `删除分类「${row.name}」后，该分类下的卡片将变为未分类状态。确定删除吗？`,
    '确认删除',
    { type: 'warning', confirmButtonText: '删除', cancelButtonText: '取消' }
  )
  await categoryApi.delete(row.id)
  ElMessage.success('删除成功')
  loadCategories()
}

onMounted(loadCategories)
</script>

<style scoped lang="scss">
.category-manage-view {
  .page-header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      color: #303133;
      font-size: 22px;
    }

    .header-desc {
      margin: 4px 0 0;
      font-size: 14px;
      color: #909399;
    }

    .add-btn {
      border-radius: 8px;
      font-weight: 500;
    }
  }

  .stats-bar {
    display: flex;
    gap: 24px;
    margin-bottom: 24px;
    padding: 16px 20px;
    background: #f6ffed;
    border-radius: 10px;
    border: 1px solid #e8f5e9;

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      min-width: 80px;

      .stat-value {
        font-size: 24px;
        font-weight: 700;
        color: #2d8c3c;
        line-height: 1.2;
      }

      .stat-label {
        font-size: 12px;
        color: #909399;
        margin-top: 2px;
      }
    }
  }

  .category-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 14px;
    min-height: 120px;
  }

  .category-card {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 16px;
    background: #fff;
    border: 1px solid #ebeef5;
    border-radius: 12px;
    transition: all 0.25s ease;

    &:hover {
      border-color: #c2e7b0;
      box-shadow: 0 4px 12px rgba(45, 140, 60, 0.08);
      transform: translateY(-1px);
    }

    .card-icon {
      flex-shrink: 0;
      width: 44px;
      height: 44px;
      border-radius: 10px;
      display: flex;
      align-items: center;
      justify-content: center;

      .icon-text {
        color: #fff;
        font-size: 18px;
        font-weight: 600;
      }
    }

    .card-body {
      flex: 1;
      min-width: 0;

      .card-name {
        margin: 0;
        font-size: 15px;
        font-weight: 600;
        color: #303133;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .card-meta {
        font-size: 12px;
        color: #b0b3b8;
        margin-top: 2px;
        display: inline-block;
      }
    }

    .card-actions {
      flex-shrink: 0;
      display: flex;
      gap: 2px;
      opacity: 0;
      transition: opacity 0.2s;
    }

    &:hover .card-actions {
      opacity: 1;
    }
  }
}
</style>
