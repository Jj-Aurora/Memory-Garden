<template>
  <div class="card-list-view">
    <div class="list-header">
      <div>
        <h2 class="section-title">知识卡片列表</h2>
        <p class="list-subtitle">管理你的知识库</p>
      </div>
      <el-button type="primary" size="large" @click="router.push('/card/create')">
        <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
        </svg>
        新建卡片
      </el-button>
    </div>

    <div class="list-filter">
      <el-select v-model="filterCategoryId" placeholder="按分类筛选" clearable style="width: 200px"
        @change="loadCards">
        <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
      </el-select>
    </div>

    <div class="table-wrapper">
      <el-table :data="cards" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="frontContent" label="正面内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="backContent" label="背面内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="120">
          <template #default="{ row }">
            <CategoryTag v-if="row.categoryName" :name="row.categoryName" effect="plain" />
            <span v-else class="no-category">未分类</span>
          </template>
        </el-table-column>
        <el-table-column prop="sourceType" label="来源" width="100">
          <template #default="{ row }">
            {{ row.sourceType === 1 ? '知识库' : '手动' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="router.push(`/card/${row.id}/edit`)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { cardApi, type CardVO } from '@/api/card'
import { categoryApi, type Category } from '@/api/category'
import CategoryTag from '@/components/CategoryTag.vue'

const router = useRouter()
const loading = ref(false)
const cards = ref<CardVO[]>([])
const categories = ref<Category[]>([])
const filterCategoryId = ref<number | undefined>(undefined)

async function loadCards() {
  loading.value = true
  try {
    const res = await cardApi.list(filterCategoryId.value)
    cards.value = res.data
  } finally {
    loading.value = false
  }
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('删除后关联的植物也会被移除，确定要删除吗？', '确认删除', { type: 'warning' })
  await cardApi.delete(id)
  ElMessage.success('卡片已删除')
  loadCards()
}

onMounted(async () => {
  const catRes = await categoryApi.list()
  categories.value = catRes.data
  loadCards()
})
</script>

<style scoped lang="scss">
.card-list-view {
  .list-header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    margin-bottom: var(--space-xl);
    gap: var(--space-base);
  }

  .list-subtitle {
    font-size: var(--font-size-sm);
    color: var(--color-text-muted);
    margin: var(--space-xs) 0 0;
  }

  .btn-icon {
    width: 16px;
    height: 16px;
    margin-right: 4px;
  }

  .list-filter {
    margin-bottom: var(--space-lg);
  }

  .table-wrapper {
    background: var(--color-bg-card);
    border-radius: var(--radius-lg);
    border: 1px solid var(--color-border-light);
    overflow: hidden;
  }

  .no-category {
    color: var(--color-text-muted);
    font-size: var(--font-size-sm);
  }
}

@media (max-width: 768px) {
  .card-list-view .list-header {
    flex-direction: column;
  }
}
</style>
