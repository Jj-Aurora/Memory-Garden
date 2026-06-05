<template>
  <div class="category-manage-view">
    <div class="page-header">
      <div>
        <h2 class="section-title">分类管理</h2>
        <p class="header-desc">整理知识领域，让花园井然有序</p>
      </div>
      <el-button type="primary" size="large" @click="showAddDialog">
        <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
        </svg>
        新增分类
      </el-button>
    </div>

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

      <el-empty v-if="categories.length === 0 && !loading" description="还没有分类，点击右上角创建第一个吧" />
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="420px"
      :close-on-click-modal="false">
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
    margin-bottom: var(--space-xl);
    gap: var(--space-base);
  }

  .header-desc {
    font-size: var(--font-size-sm);
    color: var(--color-text-muted);
    margin: var(--space-xs) 0 0;
  }

  .btn-icon {
    width: 16px;
    height: 16px;
    margin-right: 4px;
  }

  .stats-bar {
    display: flex;
    gap: var(--space-xl);
    margin-bottom: var(--space-xl);
    padding: var(--space-base) var(--space-xl);
    background: var(--color-primary-bg);
    border-radius: var(--radius-lg);
    border: 1px solid var(--color-border-light);

    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      min-width: 80px;

      .stat-value {
        font-size: var(--font-size-2xl);
        font-weight: var(--font-weight-bold);
        color: var(--color-primary);
        line-height: var(--line-height-tight);
      }

      .stat-label {
        font-size: var(--font-size-xs);
        color: var(--color-text-muted);
        margin-top: var(--space-xs);
      }
    }
  }

  .category-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: var(--space-base);
    min-height: 120px;
  }

  .category-card {
    display: flex;
    align-items: center;
    gap: var(--space-base);
    padding: var(--space-base);
    background: var(--color-bg-card);
    border: 1px solid var(--color-border-light);
    border-radius: var(--radius-lg);
    transition: all var(--transition-normal);

    &:hover {
      border-color: var(--color-primary-lighter);
      box-shadow: var(--shadow-primary);
      transform: translateY(-1px);
    }

    .card-icon {
      flex-shrink: 0;
      width: 44px;
      height: 44px;
      border-radius: var(--radius-md);
      display: flex;
      align-items: center;
      justify-content: center;

      .icon-text {
        color: #fff;
        font-size: var(--font-size-lg);
        font-weight: var(--font-weight-bold);
      }
    }

    .card-body {
      flex: 1;
      min-width: 0;

      .card-name {
        margin: 0;
        font-size: var(--font-size-base);
        font-weight: var(--font-weight-semibold);
        color: var(--color-text-primary);
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .card-meta {
        font-size: var(--font-size-xs);
        color: var(--color-text-muted);
        margin-top: 2px;
        display: inline-block;
      }
    }

    .card-actions {
      flex-shrink: 0;
      display: flex;
      gap: 2px;
      opacity: 0;
      transition: opacity var(--transition-fast);
    }

    &:hover .card-actions {
      opacity: 1;
    }
  }
}

@media (max-width: 768px) {
  .category-manage-view .page-header {
    flex-direction: column;
  }

  .category-manage-view .category-grid {
    grid-template-columns: 1fr;
  }
}
</style>
