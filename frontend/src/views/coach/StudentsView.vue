<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">学员学时管理</div>
        <div class="s">查看学员列表并设置要求学时</div>
      </div>

      <div class="tools">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索学员姓名/手机号/学号" 
          clearable 
          style="width: 220px"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>

      <el-table 
        :data="students" 
        style="width: 100%" 
        class="table"
        v-loading="loading"
        empty-text="暂无学员数据"
      >
        <el-table-column prop="studentNo" label="学号" width="120">
          <template #default="scope">
            {{ scope.row.studentNo || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="学员姓名" width="120" />
        <el-table-column prop="mobile" label="手机号" width="140" />
        <el-table-column prop="carType" label="车型" width="80">
          <template #default="scope">
            {{ scope.row.carType || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="requiredHours" label="要求学时" width="120">
          <template #default="scope">
            {{ scope.row.requiredHours || 40 }} h
          </template>
        </el-table-column>
        <el-table-column prop="completedHours" label="已完成学时" width="120">
          <template #default="scope">
            <span :class="{ 'warning': getProgress(scope.row) >= 100 }">
              {{ scope.row.completedHours || 0 }} h
            </span>
          </template>
        </el-table-column>
        <el-table-column label="进度" width="150">
          <template #default="scope">
            <el-progress 
              :percentage="getProgress(scope.row)" 
              :stroke-width="8"
              :show-text="true"
              :status="getProgressStatus(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              plain
              @click="openEditDialog(scope.row)"
            >
              设置学时
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchStudents"
          @current-change="fetchStudents"
        />
      </div>
    </div>

    <el-dialog 
      v-model="editDialogVisible" 
      title="设置要求学时"
      width="400px"
    >
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="学员姓名">
          <span>{{ editForm.userName }}</span>
        </el-form-item>
        <el-form-item label="手机号">
          <span>{{ editForm.mobile }}</span>
        </el-form-item>
        <el-form-item label="要求学时">
          <el-input-number 
            v-model="editForm.requiredHours" 
            :min="0" 
            :max="200"
            :step="5"
          />
          <span style="margin-left: 8px; color: rgba(255,255,255,0.6);">小时</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRequiredHours" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import http from '../../api/http'
import { hoursApi } from '../../api'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const loading = ref(false)
const saving = ref(false)
const students = ref([])
const searchKeyword = ref('')
const editDialogVisible = ref(false)
const editForm = reactive({
  id: null,
  userName: '',
  mobile: '',
  requiredHours: 40
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

function getProgress(student) {
  const required = student.requiredHours || 40
  const completed = student.completedHours || 0
  if (required === 0) return 0
  return Math.min(Math.round((completed / required) * 100), 100)
}

function getProgressStatus(student) {
  const progress = getProgress(student)
  if (progress >= 100) return 'success'
  if (progress >= 80) return 'warning'
  return ''
}

async function fetchStudents() {
  loading.value = true
  try {
    const params = new URLSearchParams()
    if (searchKeyword.value) {
      params.append('keyword', searchKeyword.value)
    }
    params.append('page', pagination.page)
    params.append('size', pagination.size)
    
    const res = await http.get(`/api/coach/students?${params}`)
    console.log('学员列表响应:', res)
    if (res.data.code === 200) {
      const records = res.data.data.records || []
      pagination.total = res.data.data.total || 0
      
      const studentsWithHours = await Promise.all(
        records.map(async (student) => {
          try {
            const hoursRes = await http.get(`/api/appointment/hours/stats/byStudent?studentId=${student.id}`)
            return {
              ...student,
              completedHours: hoursRes.data.code === 200 ? hoursRes.data.data.completedHours : 0
            }
          } catch (e) {
            return {
              ...student,
              completedHours: 0
            }
          }
        })
      )
      students.value = studentsWithHours
      console.log('处理后的学员数据:', students.value)
    } else {
      ElMessage.error(res.data.msg || '获取学员列表失败')
    }
  } catch (error) {
    console.error('获取学员列表失败:', error)
    let msg = error?.response?.data?.msg || error?.message
    if (error?.response?.status === 403) {
      msg = '无权限访问'
    }
    ElMessage.error(msg || '获取学员列表失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  fetchStudents()
}

function openEditDialog(student) {
  editForm.id = student.id
  editForm.userName = student.userName
  editForm.mobile = student.mobile
  editForm.requiredHours = student.requiredHours || 40
  editDialogVisible.value = true
}

async function saveRequiredHours() {
  saving.value = true
  try {
    const res = await hoursApi.updateRequiredHours(editForm.id, editForm.requiredHours)
    if (res.data.code === 200) {
      ElMessage.success('要求学时设置成功')
      editDialogVisible.value = false
      fetchStudents()
    } else {
      ElMessage.error(res.data.msg || '设置失败')
    }
  } catch (error) {
    console.error('设置要求学时失败:', error)
    ElMessage.error('设置要求学时失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  fetchStudents()
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
  gap: 10px;
  margin-bottom: 16px;
}

.tools :deep(.el-input__wrapper) {
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

.table :deep(.el-table__empty-text) {
  color: rgba(255, 255, 255, 0.5);
}

.warning {
  color: #e6a23c;
  font-weight: 600;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-dialog) {
  background: rgba(30, 30, 40, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-dialog__title) {
  color: rgba(255, 255, 255, 0.92);
}

:deep(.el-dialog__body) {
  color: rgba(255, 255, 255, 0.85);
}

:deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.7);
}
</style>
