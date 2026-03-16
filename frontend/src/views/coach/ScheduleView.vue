<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><Calendar /></el-icon>
        </div>
        <div class="h">排班管理</div>
        <div class="s">设置可预约时段，最少排班两小时</div>
      </div>

      <div class="tools">
        <el-date-picker v-model="filterDate" type="date" placeholder="选择日期" class="tool-item" @change="loadSchedules" />
        <el-button type="primary" plain @click="openAddDialog" class="tool-item">
          <el-icon><Plus /></el-icon>
          新增时段
        </el-button>
      </div>

      <el-table :data="schedules" style="width: 100%" class="table" v-loading="loading">
        <el-table-column prop="scheduleDate" label="日期" width="120" />
        <el-table-column label="时间段" width="160">
          <template #default="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column label="名额" width="120">
          <template #default="scope">
            {{ scope.row.bookedCount }} / {{ scope.row.capacity }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="dark" class="status-tag">
              <el-icon v-if="scope.row.status === 1"><Check /></el-icon>
              <el-icon v-else><Close /></el-icon>
              {{ scope.row.status === 1 ? '开放' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="200">
          <template #default="scope">
            <el-button size="small" type="primary" plain @click="toggleStatus(scope.row)" class="action-btn">
              <el-icon v-if="scope.row.status === 1"><Close /></el-icon>
              <el-icon v-else><Check /></el-icon>
              {{ scope.row.status === 1 ? '关闭' : '开放' }}
            </el-button>
            <el-button size="small" type="warning" plain @click="openEditDialog(scope.row)" class="action-btn">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button size="small" type="danger" plain @click="deleteSchedule(scope.row)" class="action-btn" :disabled="scope.row.bookedCount > 0">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑时段' : '新增时段'" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="日期">
          <el-date-picker v-model="form.scheduleDate" type="date" placeholder="选择日期" style="width: 100%" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-select v-model="form.startTime" placeholder="选择开始时间" style="width: 100%" :disabled="isEdit" @change="onStartTimeChange">
            <el-option-group label="上午">
              <el-option v-for="t in morningStartTimes" :key="t" :label="t" :value="t" />
            </el-option-group>
            <el-option-group label="下午">
              <el-option v-for="t in afternoonStartTimes" :key="t" :label="t" :value="t" />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-select v-model="form.endTime" placeholder="选择结束时间" style="width: 100%" :disabled="isEdit">
            <el-option-group label="上午">
              <el-option v-for="t in morningEndTimes" :key="t" :label="t" :value="t" :disabled="!isValidEndTime(t)" />
            </el-option-group>
            <el-option-group label="下午">
              <el-option v-for="t in afternoonEndTimes" :key="t" :label="t" :value="t" :disabled="!isValidEndTime(t)" />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="时段时长">
          <div class="duration-info" v-if="form.startTime && form.endTime">
            {{ calculateDuration() }} 小时
          </div>
          <div class="duration-info warning" v-else-if="form.startTime">
            请选择结束时间（最少2小时）
          </div>
        </el-form-item>
        <el-form-item label="可约名额">
          <el-input-number v-model="form.capacity" :min="1" :max="10" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.statusActive" active-text="开放" inactive-text="关闭" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Calendar, Plus, Check, Close, Delete, Edit } from '@element-plus/icons-vue'
import { scheduleApi } from '../../api'

const filterDate = ref('')
const schedules = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)

const allTimeSlots = []
for (let h = 7; h <= 18; h++) {
  allTimeSlots.push(`${String(h).padStart(2, '0')}:00`)
  if (h < 18) {
    allTimeSlots.push(`${String(h).padStart(2, '0')}:30`)
  }
}
allTimeSlots.push('18:00')

const morningTimes = allTimeSlots.filter(t => {
  const h = parseInt(t.split(':')[0])
  return h >= 7 && h < 12
})

const afternoonTimes = allTimeSlots.filter(t => {
  const h = parseInt(t.split(':')[0])
  return h >= 13 && h <= 18
})

const morningStartTimes = computed(() => morningTimes.slice(0, -1))
const afternoonStartTimes = computed(() => afternoonTimes.slice(0, -1))
const morningEndTimes = computed(() => morningTimes.slice(1))
const afternoonEndTimes = computed(() => afternoonTimes.slice(1))

const form = ref({
  id: null,
  scheduleDate: '',
  startTime: '',
  endTime: '',
  capacity: 1,
  statusActive: true
})

function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function timeToMinutes(time) {
  const [h, m] = time.split(':').map(Number)
  return h * 60 + m
}

function isValidEndTime(endTime) {
  if (!form.value.startTime) return false
  
  const startMinutes = timeToMinutes(form.value.startTime)
  const endMinutes = timeToMinutes(endTime)
  
  if (endMinutes <= startMinutes) return false
  
  const duration = (endMinutes - startMinutes) / 60
  return duration >= 2
}

function calculateDuration() {
  if (!form.value.startTime || !form.value.endTime) return 0
  const start = timeToMinutes(form.value.startTime)
  const end = timeToMinutes(form.value.endTime)
  return (end - start) / 60
}

function onStartTimeChange() {
  form.value.endTime = ''
}

async function loadSchedules() {
  loading.value = true
  try {
    const startDate = filterDate.value ? formatDate(filterDate.value) : formatDate(new Date())
    const res = await scheduleApi.getCoachSchedules(startDate)
    if (res.data.code === 200) {
      schedules.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}

function openAddDialog() {
  isEdit.value = false
  form.value = {
    id: null,
    scheduleDate: filterDate.value || new Date(),
    startTime: '',
    endTime: '',
    capacity: 1,
    statusActive: true
  }
  dialogVisible.value = true
}

function openEditDialog(row) {
  isEdit.value = true
  form.value = {
    id: row.id,
    scheduleDate: row.scheduleDate,
    startTime: row.startTime,
    endTime: row.endTime,
    capacity: row.capacity,
    statusActive: row.status === 1
  }
  dialogVisible.value = true
}

async function submitForm() {
  if (!form.value.scheduleDate || !form.value.startTime || !form.value.endTime) {
    ElMessage.warning('请填写完整信息')
    return
  }

  const duration = calculateDuration()
  if (duration < 2) {
    ElMessage.warning('排班时长最少2小时')
    return
  }

  submitting.value = true
  try {
    const data = {
      id: form.value.id,
      scheduleDate: formatDate(form.value.scheduleDate),
      startTime: form.value.startTime,
      endTime: form.value.endTime,
      capacity: form.value.capacity,
      status: form.value.statusActive ? 1 : 0
    }

    let res
    if (isEdit.value) {
      res = await scheduleApi.updateSchedule(data)
    } else {
      res = await scheduleApi.createSchedule(data)
    }

    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
      dialogVisible.value = false
      loadSchedules()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    submitting.value = false
  }
}

async function toggleStatus(row) {
  try {
    const res = await scheduleApi.toggleStatus(row.id)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg)
      loadSchedules()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  }
}

async function deleteSchedule(row) {
  if (row.bookedCount > 0) {
    ElMessage.warning('该时段已有预约，无法删除')
    return
  }

  try {
    await ElMessageBox.confirm('确定要删除该时段吗？', '提示', { type: 'warning' })
    const res = await scheduleApi.deleteSchedule(row.id)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadSchedules()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('网络错误')
    }
  }
}

onMounted(() => {
  loadSchedules()
})
</script>

<style scoped>
.grid {
  display: grid;
  gap: 16px;
}

.card {
  padding: 24px;
  animation: fadeInScale 0.6s ease-out;
}

.head {
  margin-bottom: 20px;
  text-align: center;
}

.icon-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: var(--gradient-primary);
  margin-bottom: 12px;
  box-shadow: 0 4px 20px rgba(79, 140, 255, 0.3);
}

.head-icon {
  font-size: 28px;
  color: white;
}

.h {
  color: rgba(255, 255, 255, 0.95);
  font-weight: 800;
  font-size: 20px;
  margin-bottom: 6px;
}

.s {
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.tools {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.tool-item {
  min-width: 140px;
}

.tools :deep(.el-input__wrapper),
.tools :deep(.el-date-editor) {
  background: rgba(255, 255, 255, 0.08);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.12);
  transition: all 0.3s ease;
}

.tools :deep(.el-input__wrapper:hover),
.tools :deep(.el-date-editor:hover) {
  border-color: var(--app-primary-light);
  box-shadow: 0 0 8px rgba(79, 140, 255, 0.2);
}

.table :deep(.el-table) {
  background: transparent;
  border-radius: 8px;
  overflow: hidden;
}

.table :deep(.el-table__row) {
  background: transparent;
  transition: all 0.3s ease;
}

.table :deep(.el-table__row:hover) {
  background: rgba(255, 255, 255, 0.05);
}

.table :deep(.el-table th.el-table__cell) {
  background: rgba(255, 255, 255, 0.08);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.status-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 600;
}

.action-btn {
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(79, 140, 255, 0.2);
}

.duration-info {
  color: rgba(255, 255, 255, 0.9);
  font-weight: 600;
  padding: 8px 12px;
  background: rgba(79, 140, 255, 0.2);
  border-radius: 6px;
}

.duration-info.warning {
  background: rgba(230, 162, 60, 0.2);
  color: #e6a23c;
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
