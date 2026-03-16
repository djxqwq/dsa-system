<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><Clock /></el-icon>
        </div>
        <div class="h">预约历史记录</div>
        <div class="s">可筛选查看你的全部预约状态</div>
      </div>

      <div class="filters">
        <el-button type="primary" plain @click="loadAppointments">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

      <el-table :data="appointments" style="width: 100%" class="table" v-loading="loading">
        <el-table-column prop="appointmentDate" label="日期" width="120" />
        <el-table-column label="时间段" width="140">
          <template #default="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="coachName" label="教练" width="100" />
        <el-table-column prop="plateNumber" label="车辆" width="120">
          <template #default="scope">
            {{ scope.row.plateNumber || '未指定' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark" class="status-tag">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button 
              size="small" 
              type="danger" 
              plain 
              @click="cancelAppointment(scope.row)" 
              v-if="scope.row.status === 0 || scope.row.status === 1"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Clock, Refresh } from '@element-plus/icons-vue'
import { appointmentApi } from '../../api'

const appointments = ref([])
const loading = ref(false)

const statusMap = {
  0: { text: '待确认', type: 'warning' },
  1: { text: '已确认', type: 'success' },
  2: { text: '已完成', type: 'info' },
  3: { text: '已取消', type: 'danger' },
  4: { text: '爽约', type: 'danger' }
}

function getStatusText(status) {
  return statusMap[status]?.text || '未知'
}

function getStatusType(status) {
  return statusMap[status]?.type || 'info'
}

async function loadAppointments() {
  loading.value = true
  try {
    const res = await appointmentApi.getStudentAppointments()
    if (res.data.code === 200) {
      appointments.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

async function cancelAppointment(row) {
  try {
    await ElMessageBox.confirm('确定要取消该预约吗？取消可能会影响您的信用记录。', '提示', { type: 'warning' })
    const res = await appointmentApi.cancelAppointment(row.id)
    if (res.data.code === 200) {
      ElMessage.success('预约已取消')
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '取消失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('网络错误')
    }
  }
}

onMounted(() => {
  loadAppointments()
})
</script>

<style scoped>
.grid {
  display: grid;
  gap: 16px;
}

.card {
  padding: 18px;
  animation: fadeInScale 0.6s ease-out;
}

.head {
  margin-bottom: 14px;
  text-align: center;
}

.icon-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: var(--gradient-primary);
  margin-bottom: 10px;
  box-shadow: 0 4px 20px rgba(79, 140, 255, 0.3);
}

.head-icon {
  font-size: 24px;
  color: white;
}

.h {
  color: rgba(255, 255, 255, 0.92);
  font-weight: 800;
  font-size: 18px;
}

.s {
  margin-top: 6px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 12px;
}

.filters :deep(.el-input__wrapper),
.filters :deep(.el-date-editor),
.filters :deep(.el-select__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
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

.status-tag {
  font-weight: 600;
}

.animate-in {
  animation: fadeInScale 0.6s ease-out;
}

@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
