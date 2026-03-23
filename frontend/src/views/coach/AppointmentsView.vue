<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><List /></el-icon>
        </div>
        <div class="h">预约管理</div>
        <div class="s">管理所有预约记录</div>
      </div>

      <div class="filters">
        <el-select v-model="filterStatus" style="width: 140px" placeholder="全部状态" @change="loadAppointments" clearable>
          <el-option label="待确认" :value="0" />
          <el-option label="已确认" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="已取消" :value="3" />
          <el-option label="爽约" :value="4" />
          <el-option label="已拒绝" :value="5" />
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

      <el-table :data="pagedAppointments" style="width: 100%" class="table" v-loading="loading">
        <el-table-column label="学员" width="120">
          <template #default="scope">
            {{ scope.row.studentName }}
            <el-tag size="small" :type="scope.row.studentGender === 1 ? 'primary' : 'danger'" style="margin-left: 4px">
              {{ scope.row.studentGender === 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appointmentDate" label="日期" width="120" />
        <el-table-column label="时间段" width="140">
          <template #default="scope">
            {{ formatTime(scope.row.startTime) }} - {{ formatTime(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="车辆" width="150">
          <template #default="scope">
            <span v-if="scope.row.plateNumber">
              {{ scope.row.plateNumber }}
              <el-tag size="small" type="info" style="margin-left: 4px">{{ scope.row.vehicleType }}</el-tag>
            </span>
            <span v-else>未指定</span>
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
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <div class="action-btns">
              <template v-if="scope.row.status === 0">
                <el-button 
                  size="small" 
                  type="success" 
                  plain 
                  @click="handleConfirm(scope.row)"
                >
                  确认
                </el-button>
                <el-button 
                  size="small" 
                  type="danger" 
                  plain 
                  @click="handleReject(scope.row)"
                >
                  拒绝
                </el-button>
              </template>
              <template v-if="scope.row.status === 1">
                <el-button 
                  size="small" 
                  type="success" 
                  plain 
                  @click="handleComplete(scope.row)"
                >
                  完成
                </el-button>
                <el-button 
                  size="small" 
                  type="warning" 
                  plain 
                  @click="handleNoShow(scope.row)"
                >
                  爽约
                </el-button>
              </template>
              <template v-if="[2, 3, 4, 5].includes(scope.row.status)">
                <el-button 
                  size="small" 
                  type="warning" 
                  plain 
                  @click="handleUndo(scope.row)"
                >
                  撤销
                </el-button>
              </template>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper" v-if="filteredAppointments.length > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="filteredAppointments.length"
          layout="total, sizes, prev, pager, next, jumper"
          background
        />
      </div>

      <div class="stats" v-if="filteredAppointments.length > 0">
        <div class="stat-item">
          <span class="stat-label">总计</span>
          <span class="stat-value">{{ filteredAppointments.length }}</span>
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
        <div class="stat-item">
          <span class="stat-label">已拒绝</span>
          <span class="stat-value danger">{{ rejectedCount }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { List, Refresh } from '@element-plus/icons-vue'
import { appointmentApi } from '../../api'

const appointments = ref([])
const loading = ref(false)
const filterStatus = ref(null)
const filterDate = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)

const statusMap = {
  0: { text: '待确认', type: 'warning' },
  1: { text: '已确认', type: 'success' },
  2: { text: '已完成', type: 'info' },
  3: { text: '已取消', type: 'danger' },
  4: { text: '爽约', type: 'danger' },
  5: { text: '已拒绝', type: 'danger' }
}

function getStatusText(status) {
  return statusMap[status]?.text || '未知'
}

function getStatusType(status) {
  return statusMap[status]?.type || 'info'
}

function formatTime(time) {
  if (!time) return ''
  return time.substring(0, 5)
}

const filteredAppointments = computed(() => {
  let list = appointments.value
  if (filterDate.value) {
    list = list.filter(a => a.appointmentDate === filterDate.value)
  }
  return list
})

const pagedAppointments = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredAppointments.value.slice(start, end)
})

const completedCount = computed(() => appointments.value.filter(a => a.status === 2).length)
const cancelledCount = computed(() => appointments.value.filter(a => a.status === 3).length)
const noShowCount = computed(() => appointments.value.filter(a => a.status === 4).length)
const rejectedCount = computed(() => appointments.value.filter(a => a.status === 5).length)

async function loadAppointments() {
  loading.value = true
  try {
    const res = await appointmentApi.getCoachAppointments(filterStatus.value)
    if (res.data.code === 200) {
      appointments.value = res.data.data || []
      currentPage.value = 1
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

async function handleConfirm(row) {
  try {
    const res = await appointmentApi.confirmAppointment(row.id)
    if (res.data.code === 200) {
      ElMessage.success('预约已确认')
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
}

async function handleReject(row) {
  try {
    const res = await appointmentApi.rejectAppointment(row.id)
    if (res.data.code === 200) {
      ElMessage.success('已拒绝该预约')
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
}

async function handleComplete(row) {
  try {
    const res = await appointmentApi.completeAppointment(row.id)
    if (res.data.code === 200) {
      ElMessage.success('已标记为完成')
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
}

async function handleNoShow(row) {
  try {
    const res = await appointmentApi.markNoShow(row.id)
    if (res.data.code === 200) {
      ElMessage.success('已标记为爽约')
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
}

async function handleUndo(row) {
  try {
    const res = await appointmentApi.undoComplete(row.id)
    if (res.data.code === 200) {
      ElMessage.success('已撤销完成状态')
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
}

onMounted(() => {
  loadAppointments()
})

watch(filterDate, () => {
  currentPage.value = 1
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

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.pagination-wrapper :deep(.el-pagination) {
  --el-pagination-bg-color: rgba(255, 255, 255, 0.15);
  --el-pagination-button-bg-color: rgba(255, 255, 255, 0.15);
  --el-pagination-hover-color: #409eff;
  --el-pagination-button-color: rgba(255, 255, 255, 0.85);
  --el-pagination-font-size: 13px;
}

.pagination-wrapper :deep(.el-pagination .el-pager li) {
  background-color: rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.85);
  font-weight: 500;
}

.pagination-wrapper :deep(.el-pagination .el-pager li:hover) {
  color: #409eff;
}

.pagination-wrapper :deep(.el-pagination .el-pager li.is-active) {
  background-color: #409eff;
  color: #fff;
}

.pagination-wrapper :deep(.el-pagination button) {
  background-color: rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.85);
}

.pagination-wrapper :deep(.el-pagination button:hover) {
  color: #409eff;
}

.pagination-wrapper :deep(.el-pagination__total),
.pagination-wrapper :deep(.el-pagination__jump) {
  color: rgba(255, 255, 255, 0.7);
}

.pagination-wrapper :deep(.el-pagination__sizes .el-select .el-select__wrapper) {
  background-color: rgba(255, 255, 255, 0.15);
  box-shadow: none;
}

.pagination-wrapper :deep(.el-pagination__editor .el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.15);
  box-shadow: none;
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

.action-btns {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.action-btns .el-button {
  margin: 0;
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
