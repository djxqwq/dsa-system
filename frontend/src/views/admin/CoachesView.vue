<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">教练管理</div>
        <div class="s">维护教练账号与状态，与数据库同步</div>
      </div>

      <div class="tools">
        <el-input v-model="keyword" style="width: 240px" placeholder="搜索姓名/手机号/工号" clearable @keyup.enter="search" />
        <el-button type="primary" plain @click="search">查询</el-button>
        <el-button type="primary" @click="openAdd">新增教练</el-button>
      </div>

      <el-table v-loading="loading" :data="rows" style="width: 100%" class="table">
        <el-table-column prop="coachNo" label="工号" width="120" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="mobile" label="手机号" width="140" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="启用状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" effect="dark">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="workStatus" label="在岗状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.workStatus === 1 ? 'success' : 'warning'" effect="dark">
              {{ scope.row.workStatus === 1 ? '在岗' : '休假' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="320" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" plain @click="openEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" plain @click="resetPwd(scope.row)">重置密码</el-button>
            <el-button v-if="scope.row.status === 1" size="small" type="warning" plain @click="setStatus(scope.row, 0)">停用</el-button>
            <el-button v-else size="small" type="success" plain @click="setStatus(scope.row, 1)">启用</el-button>
            <el-button v-if="scope.row.workStatus === 1" size="small" type="warning" plain @click="setWorkStatus(scope.row, 0)">休假</el-button>
            <el-button v-else size="small" type="success" plain @click="setWorkStatus(scope.row, 1)">在岗</el-button>
            <el-button size="small" type="danger" plain @click="remove(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @current-change="fetch"
          @size-change="onSizeChange"
        />
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑教练' : '新增教练'"
      width="480px"
      destroy-on-close
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="工号" prop="coachNo">
          <el-input v-model="form.coachNo" placeholder="请输入教练工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="form.mobile" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item v-if="!form.id" label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="留空则默认 12345678" show-password />
        </el-form-item>
        <el-form-item v-if="form.id" label="新密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="不修改请留空" show-password />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../../api/http.js'

const keyword = ref('')
const rows = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  coachNo: '',
  name: '',
  mobile: '',
  password: '',
  status: 1
})

const rules = {
  coachNo: [{ required: true, message: '请输入教练工号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

function formatTime(val) {
  if (!val) return '-'
  if (Array.isArray(val)) return val.slice(0, 10).join('-') + ' ' + (val.slice(11, 19).join(':') || '')
  const s = String(val)
  return s.replace('T', ' ').slice(0, 19)
}

async function fetch() {
  loading.value = true
  try {
    const res = await http.get('/api/coach/list', {
      params: { keyword: keyword.value, page: page.value, size: pageSize.value }
    })
    if (res.data.code === 200) {
      rows.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    } else {
      ElMessage.error(res.data.msg || '获取列表失败')
    }
  } catch (e) {
    let msg = e?.response?.data?.msg || e?.message
    if (e?.response?.status === 403) {
      msg = '无权限访问，请使用管理员账号重新登录'
    } else if (e?.code === 'ERR_NETWORK') {
      msg = '无法连接后端，请确认后端已启动（默认 http://localhost:8080）'
    }
    if (!msg) msg = '网络错误'
    ElMessage.error(msg)
  } finally {
    loading.value = false
  }
}

function search() {
  page.value = 1
  fetch()
}

function onSizeChange() {
  page.value = 1
  fetch()
}

function openAdd() {
  form.id = null
  form.coachNo = ''
  form.name = ''
  form.mobile = ''
  form.password = ''
  form.status = 1
  dialogVisible.value = true
}

function openEdit(row) {
  form.id = row.id
  form.coachNo = row.coachNo || ''
  form.name = row.name || ''
  form.mobile = row.mobile || ''
  form.password = ''
  form.status = row.status !== undefined ? row.status : 1
  dialogVisible.value = true
}

function resetForm() {
  formRef.value?.resetFields()
}

async function submit() {
  await formRef.value?.validate().catch(() => {})
  submitLoading.value = true
  try {
    const url = form.id ? '/api/coach' : '/api/coach'
    const method = form.id ? 'put' : 'post'
    const body = {
      id: form.id,
      coachNo: form.coachNo,
      name: form.name,
      mobile: form.mobile,
      status: form.status
    }
    if (form.password) body.password = form.password
    const res = await http[method](url, body)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '操作成功')
      dialogVisible.value = false
      fetch()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    if (e?.message) ElMessage.error(e.message)
  } finally {
    submitLoading.value = false
  }
}

async function resetPwd(row) {
  try {
    await ElMessageBox.confirm('确定将该教练密码重置为 12345678？', '重置密码')
  } catch {
    return
  }
  try {
    const res = await http.put(`/api/coach/resetPassword/${row.id}`)
    if (res.data.code === 200) {
      ElMessage.success(res.data.msg || '重置成功')
    } else {
      ElMessage.error(res.data.msg || '重置失败')
    }
  } catch (e) {
    const msg = e?.response?.status === 403 ? '无权限访问，请使用管理员账号重新登录'
      : (e?.response?.data?.msg || e?.message || (e?.code === 'ERR_NETWORK' ? '无法连接后端，请确认后端已启动' : '网络错误'))
    ElMessage.error(msg)
  }
}

async function setStatus(row, status) {
  const action = status === 1 ? '启用' : '停用'
  try {
    await ElMessageBox.confirm(`确定${action}该教练？`, action)
  } catch {
    return
  }
  try {
    const res = await http.put('/api/coach', {
      id: row.id,
      coachNo: row.coachNo,
      name: row.name,
      mobile: row.mobile,
      status
    })
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      fetch()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    const msg = e?.response?.status === 403 ? '无权限访问，请使用管理员账号重新登录'
      : (e?.response?.data?.msg || e?.message || (e?.code === 'ERR_NETWORK' ? '无法连接后端，请确认后端已启动' : '网络错误'))
    ElMessage.error(msg)
  }
}

async function setWorkStatus(row, workStatus) {
  const action = workStatus === 1 ? '在岗' : '休假'
  try {
    await ElMessageBox.confirm(`确定将该教练设为${action}？`, action)
  } catch {
    return
  }
  try {
    const res = await http.put('/api/coach/workStatus', {
      id: row.id,
      workStatus
    })
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      fetch()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    const msg = e?.response?.status === 403 ? '无权限访问，请使用管理员账号重新登录'
      : (e?.response?.data?.msg || e?.message || (e?.code === 'ERR_NETWORK' ? '无法连接后端，请确认后端已启动' : '网络错误'))
    ElMessage.error(msg)
  }
}

async function remove(row) {
  try {
    await ElMessageBox.confirm('确定删除该教练？删除后不可恢复。', '删除')
  } catch {
    return
  }
  try {
    const res = await http.delete(`/api/coach/${row.id}`)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      fetch()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (e) {
    const msg = e?.response?.status === 403 ? '无权限访问，请使用管理员账号重新登录'
      : (e?.response?.data?.msg || e?.message || (e?.code === 'ERR_NETWORK' ? '无法连接后端，请确认后端已启动' : '网络错误'))
    ElMessage.error(msg)
  }
}

onMounted(fetch)
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

.tools :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.pagination :deep(.el-pagination) {
  --el-pagination-bg-color: rgba(255, 255, 255, 0.06);
  --el-pagination-button-color: rgba(255, 255, 255, 0.8);
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
</style>
