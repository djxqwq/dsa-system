<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><Van /></el-icon>
        </div>
        <div class="h">车辆管理</div>
        <div class="s">管理负责的车辆，记录保养和维修信息</div>
      </div>

      <el-tabs v-model="activeTab" class="tabs">
        <el-tab-pane label="我的车辆" name="vehicles">
          <div class="tools">
            <el-button type="primary" plain @click="refreshVehicles">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
          <el-table :data="vehicles" style="width: 100%" class="table" v-loading="vehiclesLoading">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="vehicleType" label="车型" width="80" />
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)" effect="dark">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="150" />
            <el-table-column label="操作" width="520">
              <template #default="scope">
                <el-button size="small" type="success" plain @click="showUsage(scope.row)">
                  <el-icon><Clock /></el-icon>
                  使用记录
                </el-button>
                <el-button size="small" type="primary" plain @click="showMaintenanceRecords(scope.row)">
                  <el-icon><Tools /></el-icon>
                  保养记录
                </el-button>
                <el-button size="small" type="warning" plain @click="showRepairRecords(scope.row)">
                  <el-icon><Setting /></el-icon>
                  维修记录
                </el-button>
                <el-button size="small" type="primary" @click="showMaintenance(scope.row)">
                  <el-icon><Plus /></el-icon>
                  添加保养
                </el-button>
                <el-button size="small" type="warning" @click="showRepair(scope.row)">
                  <el-icon><Plus /></el-icon>
                  添加维修
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="使用记录" name="usage">
          <div class="tools">
            <el-select v-model="selectedVehicleId" placeholder="选择车辆筛选" clearable style="width: 200px">
              <el-option v-for="v in vehicles" :key="v.id" :label="v.plateNumber" :value="v.id" />
            </el-select>
          </div>
          <el-table :data="filteredUsageRecords" style="width: 100%" class="table" v-loading="usageLoading">
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
          <div class="tools">
            <el-select v-model="selectedMaintenanceVehicleId" placeholder="选择车辆筛选" clearable style="width: 200px">
              <el-option v-for="v in vehicles" :key="v.id" :label="v.plateNumber" :value="v.id" />
            </el-select>
            <el-button type="primary" plain @click="openMaintenanceDialog()">
              <el-icon><Plus /></el-icon>
              新增保养记录
            </el-button>
          </div>
          <el-table :data="filteredMaintenanceRecords" style="width: 100%" class="table" v-loading="maintenanceLoading">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="maintenanceType" label="保养类型" width="120" />
            <el-table-column prop="maintenanceDate" label="保养日期" width="120" />
            <el-table-column label="费用" width="100">
              <template #default="scope">
                {{ scope.row.cost ? `¥${scope.row.cost}` : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="mileage" label="里程(km)" width="100" />
            <el-table-column prop="description" label="描述" min-width="150" />
            <el-table-column label="下次保养" width="180">
              <template #default="scope">
                <div v-if="scope.row.nextMaintenanceDate || scope.row.nextMaintenanceMileage">
                  <span v-if="scope.row.nextMaintenanceDate">{{ scope.row.nextMaintenanceDate }}</span>
                  <span v-if="scope.row.nextMaintenanceMileage"> / {{ scope.row.nextMaintenanceMileage }}km</span>
                </div>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" type="primary" plain @click="openMaintenanceDialog(scope.row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button size="small" type="danger" plain @click="deleteMaintenance(scope.row)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="维修记录" name="repair">
          <div class="tools">
            <el-select v-model="selectedRepairVehicleId" placeholder="选择车辆筛选" clearable style="width: 200px">
              <el-option v-for="v in vehicles" :key="v.id" :label="v.plateNumber" :value="v.id" />
            </el-select>
            <el-button type="primary" plain @click="openRepairDialog()">
              <el-icon><Plus /></el-icon>
              新增维修记录
            </el-button>
          </div>
          <el-table :data="filteredRepairRecords" style="width: 100%" class="table" v-loading="repairLoading">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="repairType" label="维修类型" width="120" />
            <el-table-column prop="repairDate" label="维修日期" width="120" />
            <el-table-column label="费用" width="100">
              <template #default="scope">
                {{ scope.row.cost ? `¥${scope.row.cost}` : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="mileage" label="里程(km)" width="100" />
            <el-table-column prop="repairShop" label="维修店" width="120" />
            <el-table-column prop="description" label="描述" min-width="150" />
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="dark">
                  {{ scope.row.status === 1 ? '已完成' : '进行中' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" type="primary" plain @click="openRepairDialog(scope.row)">
                  <el-icon><Edit /></el-icon>
                </el-button>
                <el-button size="small" type="danger" plain @click="deleteRepair(scope.row)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog v-model="maintenanceDialogVisible" :title="isEditMaintenance ? '编辑保养记录' : '新增保养记录'" width="500px">
      <el-form :model="maintenanceForm" label-width="100px">
        <el-form-item label="车辆" v-if="!isEditMaintenance">
          <el-select v-model="maintenanceForm.vehicleId" placeholder="选择车辆" style="width: 100%">
            <el-option v-for="v in vehicles" :key="v.id" :label="v.plateNumber" :value="v.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="保养类型">
          <el-select v-model="maintenanceForm.maintenanceType" placeholder="选择类型" style="width: 100%">
            <el-option label="常规保养" value="常规保养" />
            <el-option label="更换机油" value="更换机油" />
            <el-option label="更换轮胎" value="更换轮胎" />
            <el-option label="更换刹车片" value="更换刹车片" />
            <el-option label="更换空调滤芯" value="更换空调滤芯" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="保养日期">
          <el-date-picker v-model="maintenanceForm.maintenanceDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="费用(元)">
          <el-input-number v-model="maintenanceForm.cost" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="里程(km)">
          <el-input-number v-model="maintenanceForm.mileage" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="maintenanceForm.description" type="textarea" :rows="3" placeholder="保养详情描述" />
        </el-form-item>
        <el-form-item label="下次保养日期">
          <el-date-picker v-model="maintenanceForm.nextMaintenanceDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="下次保养里程">
          <el-input-number v-model="maintenanceForm.nextMaintenanceMileage" :min="0" style="width: 100%" placeholder="公里数" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="maintenanceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitMaintenance" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="repairDialogVisible" :title="isEditRepair ? '编辑维修记录' : '新增维修记录'" width="500px">
      <el-form :model="repairForm" label-width="100px">
        <el-form-item label="车辆" v-if="!isEditRepair">
          <el-select v-model="repairForm.vehicleId" placeholder="选择车辆" style="width: 100%">
            <el-option v-for="v in vehicles" :key="v.id" :label="v.plateNumber" :value="v.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="维修类型">
          <el-select v-model="repairForm.repairType" placeholder="选择类型" style="width: 100%">
            <el-option label="发动机维修" value="发动机维修" />
            <el-option label="变速箱维修" value="变速箱维修" />
            <el-option label="电路维修" value="电路维修" />
            <el-option label="空调维修" value="空调维修" />
            <el-option label="刹车系统维修" value="刹车系统维修" />
            <el-option label="车身修复" value="车身修复" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="维修日期">
          <el-date-picker v-model="repairForm.repairDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="费用(元)">
          <el-input-number v-model="repairForm.cost" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="里程(km)">
          <el-input-number v-model="repairForm.mileage" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="维修店">
          <el-input v-model="repairForm.repairShop" placeholder="维修店名称" />
        </el-form-item>
        <el-form-item label="更换配件">
          <el-input v-model="repairForm.parts" placeholder="更换的配件" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="repairForm.description" type="textarea" :rows="3" placeholder="维修详情描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="repairForm.status">
            <el-radio :value="1">已完成</el-radio>
            <el-radio :value="0">进行中</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="repairDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRepair" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Van, Refresh, Tools, Setting, Plus, Edit, Delete, Clock } from '@element-plus/icons-vue'
import { vehicleApi, maintenanceApi, repairApi, appointmentApi } from '../../api'

const activeTab = ref('vehicles')
const vehicles = ref([])
const vehiclesLoading = ref(false)
const maintenanceRecords = ref([])
const maintenanceLoading = ref(false)
const repairRecords = ref([])
const repairLoading = ref(false)
const usageRecords = ref([])
const usageLoading = ref(false)
const selectedVehicleId = ref(null)
const selectedMaintenanceVehicleId = ref(null)
const selectedRepairVehicleId = ref(null)
const submitting = ref(false)

const maintenanceDialogVisible = ref(false)
const isEditMaintenance = ref(false)
const maintenanceForm = ref({
  id: null,
  vehicleId: null,
  maintenanceType: '',
  maintenanceDate: '',
  cost: null,
  mileage: null,
  description: '',
  nextMaintenanceDate: '',
  nextMaintenanceMileage: null
})

const repairDialogVisible = ref(false)
const isEditRepair = ref(false)
const repairForm = ref({
  id: null,
  vehicleId: null,
  repairType: '',
  repairDate: '',
  cost: null,
  mileage: null,
  description: '',
  parts: '',
  repairShop: '',
  status: 1
})

function getStatusType(status) {
  switch (status) {
    case 1: return 'success'
    case 2: return 'warning'
    default: return 'info'
  }
}

function getStatusText(status) {
  switch (status) {
    case 1: return '可用'
    case 2: return '维修中'
    default: return '停用'
  }
}

function getAppointmentStatusType(status) {
  switch (status) {
    case 0: return 'warning'
    case 1: return 'success'
    case 2: return 'info'
    case 3: return 'danger'
    case 4: return 'danger'
    case 5: return 'danger'
    default: return 'info'
  }
}

function getAppointmentStatusText(status) {
  switch (status) {
    case 0: return '待确认'
    case 1: return '已确认'
    case 2: return '已完成'
    case 3: return '已取消'
    case 4: return '爽约'
    case 5: return '已拒绝'
    default: return '未知'
  }
}

async function loadVehicles() {
  vehiclesLoading.value = true
  try {
    const res = await vehicleApi.getByCoach()
    if (res.data.code === 200) {
      vehicles.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    vehiclesLoading.value = false
  }
}

async function loadMaintenanceRecords() {
  maintenanceLoading.value = true
  try {
    const res = await maintenanceApi.getByCoach()
    if (res.data.code === 200) {
      maintenanceRecords.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    maintenanceLoading.value = false
  }
}

async function loadRepairRecords() {
  repairLoading.value = true
  try {
    const res = await repairApi.getByCoach()
    if (res.data.code === 200) {
      repairRecords.value = res.data.data || []
    } else {
      ElMessage.error(res.data.msg || '加载失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    repairLoading.value = false
  }
}

async function loadUsageRecords() {
  usageLoading.value = true
  try {
    const promises = vehicles.value.map(v => appointmentApi.getByVehicle(v.id))
    const results = await Promise.all(promises)
    usageRecords.value = results.flatMap(res => {
      if (res.data.code === 200) {
        return res.data.data || []
      }
      return []
    })
    usageRecords.value.sort((a, b) => {
      if (a.appointmentDate !== b.appointmentDate) {
        return b.appointmentDate.localeCompare(a.appointmentDate)
      }
      return a.startTime.localeCompare(b.startTime)
    })
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    usageLoading.value = false
  }
}

const filteredUsageRecords = computed(() => {
  if (!selectedVehicleId.value) {
    return usageRecords.value
  }
  return usageRecords.value.filter(r => r.vehicleId === selectedVehicleId.value)
})

const filteredMaintenanceRecords = computed(() => {
  if (!selectedMaintenanceVehicleId.value) {
    return maintenanceRecords.value
  }
  return maintenanceRecords.value.filter(r => r.vehicleId === selectedMaintenanceVehicleId.value)
});

const filteredRepairRecords = computed(() => {
  if (!selectedRepairVehicleId.value) {
    return repairRecords.value
  }
  return repairRecords.value.filter(r => r.vehicleId === selectedRepairVehicleId.value)
})

function filterUsageRecords() {
}

function refreshVehicles() {
  loadVehicles()
}

function showUsage(row) {
  selectedVehicleId.value = row.id
  activeTab.value = 'usage'
}

function showMaintenanceRecords(row) {
  selectedMaintenanceVehicleId.value = row.id
  activeTab.value = 'maintenance'
}

function showRepairRecords(row) {
  selectedRepairVehicleId.value = row.id
  activeTab.value = 'repair'
}

function showMaintenance(row) {
  activeTab.value = 'maintenance'
  maintenanceForm.value.vehicleId = row.id
  maintenanceDialogVisible.value = true
}

function showRepair(row) {
  activeTab.value = 'repair'
  repairForm.value.vehicleId = row.id
  repairDialogVisible.value = true
}

function openMaintenanceDialog(row = null) {
  if (row) {
    isEditMaintenance.value = true
    maintenanceForm.value = {
      id: row.id,
      vehicleId: row.vehicleId,
      maintenanceType: row.maintenanceType,
      maintenanceDate: row.maintenanceDate,
      cost: row.cost,
      mileage: row.mileage,
      description: row.description,
      nextMaintenanceDate: row.nextMaintenanceDate,
      nextMaintenanceMileage: row.nextMaintenanceMileage
    }
  } else {
    isEditMaintenance.value = false
    maintenanceForm.value = {
      id: null,
      vehicleId: null,
      maintenanceType: '',
      maintenanceDate: '',
      cost: null,
      mileage: null,
      description: '',
      nextMaintenanceDate: '',
      nextMaintenanceMileage: null
    }
  }
  maintenanceDialogVisible.value = true
}

function openRepairDialog(row = null) {
  if (row) {
    isEditRepair.value = true
    repairForm.value = {
      id: row.id,
      vehicleId: row.vehicleId,
      repairType: row.repairType,
      repairDate: row.repairDate,
      cost: row.cost,
      mileage: row.mileage,
      description: row.description,
      parts: row.parts,
      repairShop: row.repairShop,
      status: row.status
    }
  } else {
    isEditRepair.value = false
    repairForm.value = {
      id: null,
      vehicleId: null,
      repairType: '',
      repairDate: '',
      cost: null,
      mileage: null,
      description: '',
      parts: '',
      repairShop: '',
      status: 1
    }
  }
  repairDialogVisible.value = true
}

async function submitMaintenance() {
  if (!maintenanceForm.value.vehicleId) {
    ElMessage.warning('请选择车辆')
    return
  }
  if (!maintenanceForm.value.maintenanceType) {
    ElMessage.warning('请选择保养类型')
    return
  }
  if (!maintenanceForm.value.maintenanceDate) {
    ElMessage.warning('请选择保养日期')
    return
  }

  submitting.value = true
  try {
    let res
    if (isEditMaintenance.value) {
      res = await maintenanceApi.update(maintenanceForm.value)
    } else {
      res = await maintenanceApi.add(maintenanceForm.value)
    }
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '操作成功')
      maintenanceDialogVisible.value = false
      loadMaintenanceRecords()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    submitting.value = false
  }
}

async function submitRepair() {
  if (!repairForm.value.vehicleId) {
    ElMessage.warning('请选择车辆')
    return
  }
  if (!repairForm.value.repairType) {
    ElMessage.warning('请选择维修类型')
    return
  }
  if (!repairForm.value.repairDate) {
    ElMessage.warning('请选择维修日期')
    return
  }

  submitting.value = true
  try {
    let res
    if (isEditRepair.value) {
      res = await repairApi.update(repairForm.value)
    } else {
      res = await repairApi.add(repairForm.value)
    }
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '操作成功')
      repairDialogVisible.value = false
      loadRepairRecords()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    submitting.value = false
  }
}

async function deleteMaintenance(row) {
  try {
    await ElMessageBox.confirm('确定要删除该保养记录吗？', '提示', { type: 'warning' })
    const res = await maintenanceApi.delete(row.id)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadMaintenanceRecords()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('网络错误')
    }
  }
}

async function deleteRepair(row) {
  try {
    await ElMessageBox.confirm('确定要删除该维修记录吗？', '提示', { type: 'warning' })
    const res = await repairApi.delete(row.id)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadRepairRecords()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('网络错误')
    }
  }
}

onMounted(async () => {
  await loadVehicles()
  loadMaintenanceRecords()
  loadRepairRecords()
  loadUsageRecords()
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

.tabs {
  margin-top: 16px;
}

.tabs :deep(.el-tabs__item) {
  color: rgba(255, 255, 255, 0.7);
}

.tabs :deep(.el-tabs__item.is-active) {
  color: var(--app-primary);
}

.tabs :deep(.el-tabs__nav-wrap::after) {
  background-color: rgba(255, 255, 255, 0.1);
}

.tools {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
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

.table :deep(.el-table td.el-table__cell) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
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
