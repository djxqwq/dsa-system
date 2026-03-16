<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><Calendar /></el-icon>
        </div>
        <div class="h">练车预约</div>
        <div class="s">选择教练与时间段，提交后等待确认</div>
      </div>

      <el-form :model="form" label-width="96px" class="form">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="预约日期">
              <el-date-picker 
                v-model="form.date" 
                type="date" 
                placeholder="选择日期" 
                style="width: 100%" 
                :disabled-date="disabledDate"
                @change="onDateChange"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="教练">
              <el-select v-model="form.coachId" placeholder="请选择教练" style="width: 100%" @change="onCoachChange" clearable>
                <el-option 
                  v-for="coach in coaches" 
                  :key="coach.id" 
                  :label="coach.name" 
                  :value="coach.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="时间段">
              <el-select v-model="form.scheduleId" placeholder="请先选择日期" style="width: 100%" @change="onSlotChange">
                <el-option 
                  v-for="slot in availableSlots" 
                  :key="slot.id" 
                  :label="`${slot.startTime}-${slot.endTime} (${slot.coachName}，剩余${slot.remainingCapacity}位)`" 
                  :value="slot.id"
                  :disabled="slot.remainingCapacity <= 0"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="车辆">
              <el-select v-model="form.vehicleId" placeholder="请选择车辆" style="width: 100%" clearable>
                <el-option 
                  v-for="vehicle in vehicles" 
                  :key="vehicle.id" 
                  :label="`${vehicle.plateNumber}（${vehicle.vehicleType}）`" 
                  :value="vehicle.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input v-model="form.remark" placeholder="可选：想练的项目" />
        </el-form-item>

        <div class="actions">
          <el-button type="primary" @click="onSubmit" class="btn-submit" :loading="submitting">
            <el-icon><Check /></el-icon>
            提交预约
          </el-button>
          <el-button @click="onReset" class="btn-reset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
      </el-form>
    </div>

    <div class="glass card animate-in" style="--delay: 0.2s">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><List /></el-icon>
        </div>
        <div class="h">我的预约</div>
        <div class="s">查看预约状态，可取消未确认的预约</div>
      </div>

      <el-table :data="appointments" style="width: 100%" class="table" v-loading="loadingAppointments">
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
        <el-table-column label="操作" min-width="100">
          <template #default="scope">
            <el-button 
              size="small" 
              type="danger" 
              plain 
              @click="cancelAppointment(scope.row)" 
              v-if="scope.row.status === 0 || scope.row.status === 1"
            >
              <el-icon><Close /></el-icon>
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
import { Calendar, Check, Refresh, List, Close } from '@element-plus/icons-vue'
import { scheduleApi, appointmentApi, coachApi, vehicleApi } from '../../api'

const form = ref({
  date: '',
  coachId: null,
  scheduleId: null,
  vehicleId: null,
  remark: ''
})

const coaches = ref([])
const availableSlots = ref([])
const vehicles = ref([])
const appointments = ref([])
const submitting = ref(false)
const loadingAppointments = ref(false)

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

function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function disabledDate(date) {
  return date < new Date(new Date().setHours(0, 0, 0, 0))
}

async function loadCoaches() {
  try {
    const res = await coachApi.getAll()
    if (res.data.code === 200) {
      coaches.value = res.data.data || []
      if (coaches.value.length === 0) {
        ElMessage.warning('暂无可用教练，请联系管理员添加')
      }
    } else {
      ElMessage.warning(res.data.msg || '加载教练列表失败')
    }
  } catch (e) {
    console.error('加载教练列表失败', e)
    ElMessage.error('加载教练列表失败，请检查后端服务是否启动')
  }
}

async function onDateChange() {
  form.value.scheduleId = null
  availableSlots.value = []
  if (!form.value.date) return

  try {
    const dateStr = formatDate(form.value.date)
    const coachId = form.value.coachId || null
    const res = await scheduleApi.getAvailableSlots(dateStr, coachId)
    if (res.data.code === 200) {
      availableSlots.value = res.data.data || []
      if (availableSlots.value.length === 0) {
        ElMessage.info('该日期暂无可预约时段，请选择其他日期或联系教练添加排班')
      }
    } else {
      ElMessage.warning(res.data.msg || '加载失败')
    }
  } catch (e) {
    console.error('加载可预约时段失败', e)
    ElMessage.error('加载可预约时段失败，请检查后端服务是否启动')
  }
}

async function onCoachChange() {
  form.value.scheduleId = null
  form.value.vehicleId = null
  vehicles.value = []

  if (form.value.coachId) {
    await loadVehicles(form.value.coachId)
  }
  
  if (form.value.date) {
    onDateChange()
  }
}

async function loadVehicles(coachId) {
  try {
    const res = await vehicleApi.getList('', 1, 100)
    if (res.data.code === 200) {
      vehicles.value = (res.data.data?.records || res.data.data || []).filter(v => v.coachId === coachId)
    }
  } catch (e) {
    console.error('加载车辆列表失败', e)
  }
}

function onSlotChange() {
  const slot = availableSlots.value.find(s => s.id === form.value.scheduleId)
  if (slot && !form.value.coachId) {
    form.value.coachId = slot.coachId
    loadVehicles(slot.coachId)
  }
}

async function onSubmit() {
  if (!form.value.date || !form.value.scheduleId) {
    ElMessage.warning('请选择日期和时间段')
    return
  }

  const slot = availableSlots.value.find(s => s.id === form.value.scheduleId)
  if (!slot) {
    ElMessage.warning('请选择有效的时段')
    return
  }

  submitting.value = true
  try {
    const data = {
      scheduleId: form.value.scheduleId,
      coachId: slot.coachId,
      vehicleId: form.value.vehicleId,
      remark: form.value.remark
    }

    const res = await appointmentApi.createAppointment(data)
    if (res.data.code === 200) {
      ElMessage.success('预约成功，等待教练确认')
      onReset()
      loadAppointments()
    } else {
      ElMessage.error(res.data.msg || '预约失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    submitting.value = false
  }
}

function onReset() {
  form.value = {
    date: '',
    coachId: null,
    scheduleId: null,
    vehicleId: null,
    remark: ''
  }
  availableSlots.value = []
  vehicles.value = []
}

async function loadAppointments() {
  loadingAppointments.value = true
  try {
    const res = await appointmentApi.getStudentAppointments()
    if (res.data.code === 200) {
      appointments.value = res.data.data || []
    }
  } catch (e) {
    ElMessage.error('加载预约列表失败')
  } finally {
    loadingAppointments.value = false
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
  loadCoaches()
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

.form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.72);
}

.form :deep(.el-input__wrapper),
.form :deep(.el-select__wrapper),
.form :deep(.el-date-editor) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
  transition: all 0.3s ease;
}

.form :deep(.el-input__wrapper:hover),
.form :deep(.el-select__wrapper:hover),
.form :deep(.el-date-editor:hover) {
  border-color: var(--app-primary-light);
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

.actions {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 10px;
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
