<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">学时查询</div>
        <div class="s">查看你的累计学时、剩余学时与近期记录</div>
      </div>

      <div class="stats">
        <div class="stat glass">
          <div class="k">要求学时</div>
          <div class="v">{{ stats.totalHours || 0 }} h</div>
        </div>
        <div class="stat glass">
          <div class="k">已完成</div>
          <div class="v">{{ stats.completedHours || 0 }} h</div>
        </div>
        <div class="stat glass">
          <div class="k">剩余</div>
          <div class="v">{{ stats.remainingHours || 0 }} h</div>
        </div>
      </div>

      <div class="progress-section">
        <div class="progress-label">
          <span>学习进度</span>
          <span>{{ progressPercent }}%</span>
        </div>
        <el-progress 
          :percentage="progressPercent" 
          :stroke-width="12"
          :show-text="false"
          status="success"
        />
      </div>

      <el-divider border-style="dashed" />

      <div class="table-header">
        <span class="table-title">学时记录</span>
        <span class="table-count">共 {{ records.length }} 条记录</span>
      </div>

      <el-table 
        :data="records" 
        style="width: 100%" 
        class="table"
        v-loading="loading"
        empty-text="暂无学时记录"
      >
        <el-table-column prop="date" label="日期" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.date) }}
          </template>
        </el-table-column>
        <el-table-column prop="coachName" label="教练" width="120" />
        <el-table-column prop="hours" label="学时" width="100">
          <template #default="scope">
            {{ scope.row.hours }} h
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { hoursApi } from '../../api'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const stats = ref({
  totalHours: 0,
  completedHours: 0,
  remainingHours: 0,
  completedSessions: 0
})
const records = ref([])

const progressPercent = computed(() => {
  if (!stats.value.totalHours || stats.value.totalHours === 0) return 0
  const percent = (stats.value.completedHours / stats.value.totalHours) * 100
  return Math.min(Math.round(percent), 100)
})

function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).replace(/\//g, '-')
}

async function fetchStats() {
  try {
    const res = await hoursApi.getStudentStats()
    if (res.code === 200) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('获取学时统计失败:', error)
  }
}

async function fetchRecords() {
  loading.value = true
  try {
    const res = await hoursApi.getStudentRecords()
    if (res.code === 200) {
      records.value = res.data || []
    }
  } catch (error) {
    console.error('获取学时记录失败:', error)
    ElMessage.error('获取学时记录失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStats()
  fetchRecords()
})
</script>

<style scoped>
.grid {
  display: grid;
  gap: 16px;
}

.card {
  padding: 18px;
}

.head {
  margin-bottom: 14px;
}

.h {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 800;
}

.s {
  margin-top: 6px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.stat {
  padding: 12px 14px;
}

.k {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.v {
  margin-top: 8px;
  color: rgba(255, 255, 255, 0.92);
  font-weight: 800;
  font-size: 18px;
}

.progress-section {
  margin-top: 16px;
  padding: 12px 14px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
}

.progress-label {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.table-title {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 600;
}

.table-count {
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}

.table :deep(.el-table) {
  background: transparent;
}

.table :deep(.el-table__row) {
  background: transparent;
}

.table :deep(.el-table th.el-table__cell) {
  background: rgba(255, 255, 255, 0.06);
}

.table :deep(.el-table__empty-text) {
  color: rgba(255, 255, 255, 0.5);
}
</style>
