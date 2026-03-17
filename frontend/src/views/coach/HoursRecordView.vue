<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">学时记录</div>
        <div class="s">查看学员训练学时记录，形成可追溯明细</div>
      </div>

      <div class="stats">
        <div class="stat glass">
          <div class="k">总教学时长</div>
          <div class="v">{{ totalHours }} h</div>
        </div>
        <div class="stat glass">
          <div class="k">教学场次</div>
          <div class="v">{{ records.length }} 次</div>
        </div>
      </div>

      <el-divider border-style="dashed" />

      <div class="table-header">
        <span class="table-title">学时明细</span>
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索学员姓名" 
          clearable 
          style="width: 200px"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <el-table 
        :data="filteredRecords" 
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
        <el-table-column prop="studentName" label="学员" width="140" />
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
        <el-table-column prop="statusText" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ scope.row.statusText }}
            </el-tag>
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
import { Search } from '@element-plus/icons-vue'

const loading = ref(false)
const records = ref([])
const searchKeyword = ref('')

const filteredRecords = computed(() => {
  if (!searchKeyword.value) return records.value
  const keyword = searchKeyword.value.toLowerCase()
  return records.value.filter(record => 
    record.studentName && record.studentName.toLowerCase().includes(keyword)
  )
})

const totalHours = computed(() => {
  return records.value.reduce((sum, record) => sum + (record.hours || 0), 0)
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

function getStatusType(status) {
  switch (status) {
    case 2:
      return 'success'
    case 1:
      return 'warning'
    case 0:
      return 'info'
    default:
      return ''
  }
}

async function fetchRecords() {
  loading.value = true
  try {
    const res = await hoursApi.getCoachRecords()
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
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 16px;
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

.table :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}
</style>
