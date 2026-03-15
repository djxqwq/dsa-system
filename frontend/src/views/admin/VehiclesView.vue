<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">车辆管理</div>
        <div class="s">维护车辆信息与可用状态</div>
      </div>

      <div class="tools">
        <el-input v-model="keyword" style="width: 240px" placeholder="搜索车牌" clearable @keyup.enter="fetchList" />
        <el-select v-model="vehicleType" style="width: 160px" placeholder="车型" @change="fetchList">
          <el-option label="全部" value="all" />
          <el-option label="C1" value="C1" />
          <el-option label="C2" value="C2" />
        </el-select>
        <el-button type="primary" plain @click="fetchList">查询</el-button>
        <el-button type="primary" @click="openDialog()">新增车辆</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table" v-loading="loading">
        <el-table-column prop="plateNumber" label="车牌" width="160" />
        <el-table-column prop="vehicleType" label="车型" width="100" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" min-width="220">
          <template #default="scope">
            <el-button size="small" type="primary" plain @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" plain @click="handleStatus(scope.row, 2)" v-if="scope.row.status !== 2">维修</el-button>
            <el-button size="small" type="success" plain @click="handleStatus(scope.row, 1)" v-if="scope.row.status !== 1">启用</el-button>
            <el-button size="small" type="danger" plain @click="handleStatus(scope.row, 0)" v-if="scope.row.status === 1">停用</el-button>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import http from '../../api/http'
import { ElMessage, ElMessageBox } from 'element-plus'

const keyword = ref('')
const vehicleType = ref('all')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)

const form = ref({
  id: null,
  plateNumber: '',
  vehicleType: '',
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

const fetchList = async () => {
  loading.value = true
  try {
    const res = await http.get('/api/vehicle/list', {
      params: {
        keyword: keyword.value,
        vehicleType: vehicleType.value,
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
    form.value = { id: null, plateNumber: '', vehicleType: '', status: 1, remark: '' }
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

const handleStatus = async (row, status) => {
  const actionText = status === 1 ? '启用' : (status === 2 ? '设为维修中' : '停用')
  try {
    await ElMessageBox.confirm(`确定要${actionText}该车辆吗？`, '提示', { type: 'warning' })
    const res = await http.put(`/api/vehicle/status/${row.id}?status=${status}`)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg)
      fetchList()
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('操作失败')
    }
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

onMounted(() => {
  fetchList()
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
