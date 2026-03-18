<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">车辆管理</div>
        <div class="s">维护车辆信息与可用状态</div>
      </div>

      <div class="tools">
        <el-input v-model="keyword" style="width: 200px" placeholder="搜索车牌" clearable @keyup.enter="fetchList" />
        <el-select v-model="vehicleType" style="width: 120px" placeholder="车型" @change="fetchList" clearable>
          <el-option label="全部" value="all" />
          <el-option label="C1" value="C1" />
          <el-option label="C2" value="C2" />
        </el-select>
        <el-select v-model="selectedCoachId" style="width: 160px" placeholder="选择教练" @change="fetchList" clearable filterable>
          <el-option v-for="coach in coaches" :key="coach.id" :label="`${coach.name} (${coach.coachNo})`" :value="coach.id" />
        </el-select>
        <el-button type="primary" plain @click="fetchList">查询</el-button>
        <el-button type="primary" @click="openDialog()">新增车辆</el-button>
        <el-button type="success" @click="showAllRecords">全部记录</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table" v-loading="loading">
        <el-table-column prop="plateNumber" label="车牌" width="140" />
        <el-table-column prop="vehicleType" label="车型" width="80" />
        <el-table-column prop="coachName" label="关联教练" width="120">
          <template #default="scope">
            {{ scope.row.coachName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" min-width="380">
          <template #default="scope">
            <el-button size="small" type="success" plain @click="showUsage(scope.row)">使用记录</el-button>
            <el-button size="small" type="primary" plain @click="showMaintenance(scope.row)">保养记录</el-button>
            <el-button size="small" type="warning" plain @click="showRepair(scope.row)">维修记录</el-button>
            <el-button size="small" type="primary" plain @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchList"
          @current-change="fetchList"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑车辆' : '新增车辆'" width="500">
      <el-form :model="form" label-width="80px">
        <el-form-item label="车牌号" required>
          <el-input v-model="form.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车型" required>
          <el-select v-model="form.vehicleType" style="width: 100%" placeholder="请选择车型">
            <el-option label="C1" value="C1" />
            <el-option label="C2" value="C2" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联教练">
          <el-select v-model="form.coachId" style="width: 100%" placeholder="请选择教练" clearable filterable>
            <el-option
              v-for="coach in coaches"
              :key="coach.id"
              :label="`${coach.name} (${coach.coachNo})`"
              :value="coach.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%" placeholder="请选择状态">
            <el-option label="可用" :value="1" />
            <el-option label="维修中" :value="2" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="usageDialogVisible" title="使用记录" width="900px">
      <el-table :data="usageRecords" style="width: 100%" v-loading="usageLoading" max-height="400">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="coachName" label="教练" width="100" />
        <el-table-column prop="studentName" label="学员" width="100" />
        <el-table-column prop="appointmentDate" label="日期" width="120" />
        <el-table-column label="时间段" width="130">
          <template #default="scope">
            {{ scope.row.startTime?.substring(0, 5) }} - {{ scope.row.endTime?.substring(0, 5) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="getAppointmentStatusType(scope.row.status)" effect="dark" size="small">
              {{ getAppointmentStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" />
      </el-table>
    </el-dialog>

    <el-dialog v-model="maintenanceDialogVisible" title="保养记录" width="900px">
      <el-table :data="maintenanceRecords" style="width: 100%" v-loading="maintenanceLoading" max-height="400">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="coachName" label="教练" width="100" />
        <el-table-column prop="maintenanceType" label="保养类型" width="120" />
        <el-table-column prop="maintenanceDate" label="保养日期" width="120" />
        <el-table-column label="费用" width="100">
          <template #default="scope">
            {{ scope.row.cost ? `¥${scope.row.cost}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="mileage" label="里程(km)" width="100" />
        <el-table-column prop="description" label="描述" min-width="150" />
      </el-table>
    </el-dialog>

    <el-dialog v-model="repairDialogVisible" title="维修记录" width="900px">
      <el-table :data="repairRecords" style="width: 100%" v-loading="repairLoading" max-height="400">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="coachName" label="教练" width="100" />
        <el-table-column prop="repairType" label="维修类型" width="120" />
        <el-table-column prop="repairDate" label="维修日期" width="120" />
        <el-table-column label="费用" width="100">
          <template #default="scope">
            {{ scope.row.cost ? `¥${scope.row.cost}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="repairShop" label="维修店" width="120" />
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="dark" size="small">
              {{ scope.row.status === 1 ? '已完成' : '进行中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="150" />
      </el-table>
    </el-dialog>

    <el-dialog v-model="allRecordsDialogVisible" title="全部车辆记录" width="1100px" top="5vh">
      <el-tabs v-model="activeRecordTab">
        <el-tab-pane label="使用记录" name="usage">
          <el-table :data="allUsageRecords" style="width: 100%" v-loading="allRecordsLoading" max-height="450">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="coachName" label="教练" width="100" />
            <el-table-column prop="studentName" label="学员" width="100" />
            <el-table-column prop="appointmentDate" label="日期" width="120" />
            <el-table-column label="时间段" width="130">
              <template #default="scope">
                {{ scope.row.startTime?.substring(0, 5) }} - {{ scope.row.endTime?.substring(0, 5) }}
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="getAppointmentStatusType(scope.row.status)" effect="dark" size="small">
                  {{ getAppointmentStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="150" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="保养记录" name="maintenance">
          <el-table :data="allMaintenanceRecords" style="width: 100%" v-loading="allRecordsLoading" max-height="450">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="coachName" label="教练" width="100" />
            <el-table-column prop="maintenanceType" label="保养类型" width="120" />
            <el-table-column prop="maintenanceDate" label="保养日期" width="120" />
            <el-table-column label="费用" width="100">
              <template #default="scope">
                {{ scope.row.cost ? `¥${scope.row.cost}` : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="mileage" label="里程(km)" width="100" />
            <el-table-column prop="description" label="描述" min-width="150" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="维修记录" name="repair">
          <el-table :data="allRepairRecords" style="width: 100%" v-loading="allRecordsLoading" max-height="450">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="coachName" label="教练" width="100" />
            <el-table-column prop="repairType" label="维修类型" width="120" />
            <el-table-column prop="repairDate" label="维修日期" width="120" />
            <el-table-column label="费用" width="100">
              <template #default="scope">
                {{ scope.row.cost ? `¥${scope.row.cost}` : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="repairShop" label="维修店" width="120" />
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="dark" size="small">
                  {{ scope.row.status === 1 ? '已完成' : '进行中' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" min-width="150" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import http from '../../api/http'
import { ElMessage, ElMessageBox } from 'element-plus'

const keyword = ref('')
const vehicleType = ref('all')
const selectedCoachId = ref(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const coaches = ref([])

const usageDialogVisible = ref(false)
const usageRecords = ref([])
const usageLoading = ref(false)

const maintenanceDialogVisible = ref(false)
const maintenanceRecords = ref([])
const maintenanceLoading = ref(false)

const repairDialogVisible = ref(false)
const repairRecords = ref([])
const repairLoading = ref(false)

const allRecordsDialogVisible = ref(false)
const activeRecordTab = ref('usage')
const allUsageRecords = ref([])
const allMaintenanceRecords = ref([])
const allRepairRecords = ref([])
const allRecordsLoading = ref(false)

const form = ref({
  id: null,
  plateNumber: '',
  vehicleType: '',
  coachId: null,
  status: 1,
  remark: ''
})

const getStatusType = (status) => {
  const map = { 0: 'info', 1: 'success', 2: 'warning' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { 0: '停用', 1: '可用', 2: '维修中' }
  return map[status] || '未知'
}

const getAppointmentStatusType = (status) => {
  const map = { 0: 'warning', 1: 'success', 2: 'info', 3: 'danger', 4: 'danger', 5: 'danger' }
  return map[status] || 'info'
}

const getAppointmentStatusText = (status) => {
  const map = { 0: '待确认', 1: '已确认', 2: '已完成', 3: '已取消', 4: '爽约', 5: '已拒绝' }
  return map[status] || '未知'
}

const fetchCoaches = async () => {
  try {
    const res = await http.get('/api/coach/all')
    if (res.data.code === 200) {
      coaches.value = res.data.data
    }
  } catch (e) {
    console.error('获取教练列表失败')
  }
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await http.get('/api/vehicle/list', {
      params: {
        keyword: keyword.value,
        vehicleType: vehicleType.value,
        coachId: selectedCoachId.value,
        page: page.value,
        size: size.value
      }
    })
    if (res.data.code === 200) {
      rows.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const openDialog = (row) => {
  if (row) {
    isEdit.value = true
    form.value = { ...row }
  } else {
    isEdit.value = false
    form.value = { id: null, plateNumber: '', vehicleType: '', coachId: null, status: 1, remark: '' }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.value.plateNumber || !form.value.vehicleType) {
    ElMessage.warning('请填写完整信息')
    return
  }
  submitting.value = true
  try {
    let res
    if (isEdit.value) {
      res = await http.put('/api/vehicle', form.value)
    } else {
      res = await http.post('/api/vehicle', form.value)
    }
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg)
      dialogVisible.value = false
      fetchList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该车辆吗？', '提示', { type: 'warning' })
    const res = await http.delete(`/api/vehicle/${id}`)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg)
      fetchList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const showUsage = async (row) => {
  usageDialogVisible.value = true
  usageLoading.value = true
  try {
    const res = await http.get(`/api/appointment/vehicle/${row.id}`)
    if (res.data.code === 200) {
      usageRecords.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error('获取使用记录失败')
  } finally {
    usageLoading.value = false
  }
}

const showMaintenance = async (row) => {
  maintenanceDialogVisible.value = true
  maintenanceLoading.value = true
  try {
    const res = await http.get(`/api/maintenance/vehicle/${row.id}`)
    if (res.data.code === 200) {
      maintenanceRecords.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error('获取保养记录失败')
  } finally {
    maintenanceLoading.value = false
  }
}

const showRepair = async (row) => {
  repairDialogVisible.value = true
  repairLoading.value = true
  try {
    const res = await http.get(`/api/repair/vehicle/${row.id}`)
    if (res.data.code === 200) {
      repairRecords.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    ElMessage.error('获取维修记录失败')
  } finally {
    repairLoading.value = false
  }
}

const showAllRecords = async () => {
  allRecordsDialogVisible.value = true
  allRecordsLoading.value = true
  try {
    const [usageRes, maintenanceRes, repairRes] = await Promise.all([
      http.get('/api/appointment/all'),
      http.get('/api/maintenance/all'),
      http.get('/api/repair/all')
    ])
    allUsageRecords.value = usageRes.data.code === 200 ? (usageRes.data.data || []) : []
    allMaintenanceRecords.value = maintenanceRes.data.code === 200 ? (maintenanceRes.data.data || []) : []
    allRepairRecords.value = repairRes.data.code === 200 ? (repairRes.data.data || []) : []
  } catch (e) {
    ElMessage.error('获取记录失败')
  } finally {
    allRecordsLoading.value = false
  }
}

onMounted(() => {
  fetchList()
  fetchCoaches()
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

.tools {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 12px;
}

.tools :deep(.el-input__wrapper),
.tools :deep(.el-select__wrapper) {
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

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
