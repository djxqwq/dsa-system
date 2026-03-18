<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">学时统计</div>
        <div class="s">管理学员预约与学时记录</div>
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
        <span class="table-title">学时确认</span>
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
        <el-table-column prop="studentName" label="学员" width="120" />
        <el-table-column prop="plateNumber" label="车牌" width="100" />
        <el-table-column prop="vehicleType" label="车型" width="80" />
        <el-table-column prop="hours" label="学时" width="80">
          <template #default="scope">
            {{ scope.row.hours }} h
          </template>
        </el-table-column>
        <el-table-column label="时间段" width="110">
          <template #default="scope">
            {{ scope.row.startTime?.substring(0, 5) }} - {{ scope.row.endTime?.substring(0, 5) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="100">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="statusText" label="状态" width="90">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ scope.row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <template v-if="scope.row.status === 1">
              <el-button size="small" type="success" @click="handleComplete(scope.row)">完成</el-button>
              <el-button size="small" type="danger" @click="handleNoShow(scope.row)">爽约</el-button>
            </template>
            <template v-else-if="scope.row.status === 2">
              <el-button size="small" type="warning" @click="handleUndoComplete(scope.row)">撤销</el-button>
            </template>
            <template v-else-if="scope.row.status === 4">
              <el-button size="small" type="primary" @click="handleUndoNoShow(scope.row)">撤销</el-button>
            </template>
            <span v-else style="color: rgba(255,255,255,0.5)">-</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { hoursApi, appointmentApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'
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
  return records.value
    .filter(r => r.status === 2)
    .reduce((sum, record) => sum + (record.hours || 0), 0)
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
    case 4:
      return 'danger'
    default:
      return 'info'
  }
}

async function fetchRecords() {
  loading.value = true
  try {
    const res = await hoursApi.getCoachRecords()
    if (res.data && res.data.code === 200) {
      records.value = res.data.data || []
    }
  } catch (error) {
    console.error('获取学时记录失败:', error)
    ElMessage.error('获取学时记录失败')
  } finally {
    loading.value = false
  }
}

async function handleComplete(row) {
  try {
    await ElMessageBox.confirm('确定要完成该学员的学时吗？', '提示', { type: 'success' })
    const res = await appointmentApi.completeAppointment(row.id)
    if (res.data && res.data.code === 200) {
      ElMessage.success('学时已完成')
      fetchRecords()
    } else {
      ElMessage.error(res.data?.msg || '操作失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

async function handleNoShow(row) {
  try {
    await ElMessageBox.confirm('确定要标记该学员为爽约吗？', '警告', { type: 'warning' })
    const res = await appointmentApi.markNoShow(row.id)
    if (res.data && res.data.code === 200) {
      ElMessage.success('已标记为爽约')
      fetchRecords()
    } else {
      ElMessage.error(res.data?.msg || '操作失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

async function handleUndoComplete(row) {
  try {
    await ElMessageBox.confirm('确定要撤销完成状态吗？', '提示', { type: 'warning' })
    const res = await appointmentApi.undoComplete(row.id)
    if (res.data && res.data.code === 200) {
      ElMessage.success('已撤销完成状态')
      fetchRecords()
    } else {
      ElMessage.error(res.data?.msg || '操作失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

async function handleUndoNoShow(row) {
  try {
    await ElMessageBox.confirm('确定要撤销爽约状态吗？', '提示', { type: 'warning' })
    const res = await appointmentApi.undoComplete(row.id)
    if (res.data && res.data.code === 200) {
      ElMessage.success('已撤销爽约状态')
      fetchRecords()
    } else {
      ElMessage.error(res.data?.msg || '操作失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
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
