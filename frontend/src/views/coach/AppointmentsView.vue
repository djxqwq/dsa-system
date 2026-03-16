<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><List /></el-icon>
        </div>
        <div class="h">预约记录</div>
        <div class="s">查看所有预约历史记录</div>
      </div>

      <div class="filters">
        <el-select v-model="filterStatus" style="width: 140px" placeholder="全部状态" @change="loadAppointments" clearable>
          <el-option label="待确认" :value="0" />
          <el-option label="已确认" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="已取消" :value="3" />
          <el-option label="爽约" :value="4" />
        </el-select>
        <el-date-picker
          v-model="filterDate"
          type="date"
          placeholder="选择日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="loadAppointments"
          clearable
          style="width: 160px"
        />
        <el-button type="primary" plain @click="loadAppointments">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

      <el-table :data="appointments" style="width: 100%" class="table" v-loading="loading">
        <el-table-column prop="studentName" label="学员" width="100" />
        <el-table-column prop="appointmentDate" label="日期" width="120" />
        <el-table-column label="时间段" width="140">
          <template #default="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="plateNumber" label="车辆" width="120">
          <template #default="scope">
            {{ scope.row.plateNumber || '未指定' }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120">
          <template #default="scope">
            {{ scope.row.remark || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark" class="status-tag">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              plain 
              @click="viewDetail(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="stats" v-if="appointments.length > 0">
        <div class="stat-item">
          <span class="stat-label">总计</span>
          <span class="stat-value">{{ appointments.length }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">已完成</span>
          <span class="stat-value success">{{ completedCount }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">已取消</span>
          <span class="stat-value danger">{{ cancelledCount }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">爽约</span>
          <span class="stat-value warning">{{ noShowCount }}</span>
        </div>
      </div>
    </div>

    <el-dialog v-model="detailVisible" title="预约详情" width="400px">
      <div class="detail-list" v-if="currentAppointment">
        <div class="detail-item">
          <span class="detail-label">学员</span>
          <span class="detail-value">{{ currentAppointment.studentName }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">日期</span>
          <span class="detail-value">{{ currentAppointment.appointmentDate }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">时间段</span>
          <span class="detail-value">{{ currentAppointment.startTime }} - {{ currentAppointment.endTime }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">车辆</span>
          <span class="detail-value">{{ currentAppointment.plateNumber || '未指定' }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">状态</span>
          <span class="detail-value">
            <el-tag :type="getStatusType(currentAppointment.status)" effect="dark" size="small">
              {{ getStatusText(currentAppointment.status) }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="detail-label">备注</span>
          <span class="detail-value">{{ currentAppointment.remark || '无' }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { List, Refresh } from '@element-plus/icons-vue'
import { appointmentApi } from '../../api'

const appointments = ref([])
const loading = ref(false)
const filterStatus = ref(null)
const filterDate = ref(null)
const detailVisible = ref(false)
const currentAppointment = ref(null)

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

const completedCount = computed(() => appointments.value.filter(a => a.status === 2).length)
const cancelledCount = computed(() => appointments.value.filter(a => a.status === 3).length)
const noShowCount = computed(() => appointments.value.filter(a => a.status === 4).length)

async function loadAppointments() {
  loading.value = true
  try {
    const res = await appointmentApi.getCoachAppointments(filterStatus.value)
    if (res.data.code === 200) {
      let list = res.data.data || []
      if (filterDate.value) {
        list = list.filter(a => a.appointmentDate === filterDate.value)
      }
      appointments.value = list
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

function viewDetail(row) {
  currentAppointment.value = row
  detailVisible.value = true
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
.filters :deep(.el-select__wrapper),
.filters :deep(.el-date-editor) {
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

.stats {
  display: flex;
  gap: 24px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.stat-value {
  font-size: 18px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.9);
}

.stat-value.success {
  color: #67c23a;
}

.stat-value.danger {
  color: #f56c6c;
}

.stat-value.warning {
  color: #e6a23c;
}

.detail-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-label {
  color: #909399;
  font-size: 14px;
}

.detail-value {
  color: #303133;
  font-size: 14px;
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
