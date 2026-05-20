<template>
  <div class="study-pack-view">
    <h2>预设知识库</h2>

    <div v-loading="loading" class="pack-list">
      <el-card v-for="pack in packs" :key="pack.id" class="pack-card" shadow="hover">
        <template #header>
          <div class="pack-header">
            <span class="pack-name">{{ pack.name }}</span>
            <el-tag size="small">{{ pack.cardCount }} 张卡片</el-tag>
          </div>
        </template>
        <p class="pack-desc">{{ pack.description }}</p>
        <p v-if="pack.categoryName" class="pack-category">
          建议分类：<CategoryTag :name="pack.categoryName" effect="plain" />
        </p>
        <div class="pack-actions">
          <el-button size="small" @click="showDetail(pack.id)">预览详情</el-button>
          <el-button type="primary" size="small" :loading="importingId === pack.id"
            @click="handleImport(pack.id, pack.name)">
            一键导入
          </el-button>
        </div>
      </el-card>

      <el-empty v-if="packs.length === 0 && !loading" description="暂无预设知识包" />
    </div>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="知识包详情" width="600px">
      <div v-loading="detailLoading">
        <div v-if="detailPack" class="detail-info">
          <h3>{{ detailPack.name }}</h3>
          <p>{{ detailPack.description }}</p>
        </div>
        <el-table :data="packItems" stripe style="width: 100%">
          <el-table-column prop="frontContent" label="问题" show-overflow-tooltip />
          <el-table-column prop="backContent" label="答案" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { studyPackApi, type StudyPack, type StudyPackItem } from '@/api/studyPack'
import CategoryTag from '@/components/CategoryTag.vue'

const loading = ref(false)
const detailLoading = ref(false)
const detailVisible = ref(false)
const importingId = ref<number>(0)
const packs = ref<StudyPack[]>([])
const detailPack = ref<StudyPack | null>(null)
const packItems = ref<StudyPackItem[]>([])

async function loadPacks() {
  loading.value = true
  try {
    const res = await studyPackApi.list()
    packs.value = res.data
  } finally {
    loading.value = false
  }
}

async function showDetail(id: number) {
  detailVisible.value = true
  detailLoading.value = true
  try {
    const [packRes, itemsRes] = await Promise.all([
      studyPackApi.getDetail(id),
      studyPackApi.getPackItems(id)
    ])
    detailPack.value = packRes.data
    packItems.value = itemsRes.data
  } finally {
    detailLoading.value = false
  }
}

async function handleImport(id: number, name: string) {
  await ElMessageBox.confirm(`确定要导入知识包「${name}」吗？将创建对应的卡片和植物。`, '确认导入', { type: 'info' })
  importingId.value = id
  try {
    const res = await studyPackApi.importPack(id)
    ElMessage.success(`导入成功，共创建 ${res.data} 张卡片`)
    loadPacks()
  } finally {
    importingId.value = 0
  }
}

onMounted(loadPacks)
</script>

<style scoped lang="scss">
.study-pack-view {
  h2 {
    margin: 0 0 24px;
    color: #303133;
  }
}

.pack-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 16px;
}

.pack-card {
  .pack-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .pack-name {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }

  .pack-desc {
    font-size: 14px;
    color: #606266;
    margin: 0 0 8px;
    line-height: 1.6;
  }

  .pack-category {
    font-size: 13px;
    color: #909399;
    margin: 0 0 12px;
  }

  .pack-actions {
    display: flex;
    gap: 8px;
  }
}

.detail-info {
  margin-bottom: 16px;

  h3 {
    margin: 0 0 8px;
  }

  p {
    color: #606266;
    font-size: 14px;
  }
}
</style>
