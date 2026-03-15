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
              <el-date-picker v-model="form.date" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="时间段">
              <el-select v-model="form.slot" placeholder="请选择" style="width: 100%">
                <el-option label="09:00-10:00" value="09:00-10:00" />
                <el-option label="10:00-11:00" value="10:00-11:00" />
                <el-option label="14:00-15:00" value="14:00-15:00" />
                <el-option label="15:00-16:00" value="15:00-16:00" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="教练">
              <el-select v-model="form.coach" placeholder="请选择" style="width: 100%">
                <el-option label="李教练" value="李教练" />
                <el-option label="王教练" value="王教练" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="车辆">
              <el-select v-model="form.vehicle" placeholder="请选择" style="width: 100%">
                <el-option label="粤B·A12345（C1）" value="A12345" />
                <el-option label="粤B·B23456（C2）" value="B23456" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input v-model="form.remark" placeholder="可选：想练的项目" />
        </el-form-item>

        <div class="actions">
          <el-button type="primary" @click="onSubmit" class="btn-submit">
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
        <div class="h">我的预约（示例）</div>
        <div class="s">后续对接接口后展示实时数据</div>
      </div>

      <el-table :data="tableData" style="width: 100%" class="table">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="slot" label="时间段" width="140" />
        <el-table-column prop="coach" label="教练" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.statusType" effect="dark" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="140">
          <template #default>
            <el-button size="small" type="danger" plain @click="cancelBooking">
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
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Calendar, Check, Refresh, List, Close } from '@element-plus/icons-vue'

const form = reactive({
  date: '',
  slot: '',
  coach: '',
  vehicle: '',
  remark: '',
})

const tableData = ref([
  { date: '2026-03-14', slot: '14:00-15:00', coach: '李教练', status: '待确认', statusType: 'warning' },
  { date: '2026-03-12', slot: '09:00-10:00', coach: '王教练', status: '已确认', statusType: 'success' },
])

function onSubmit() {
  ElMessage.success('已提交预约（演示）')
}

function onReset() {
  form.date = ''
  form.slot = ''
  form.coach = ''
  form.vehicle = ''
  form.remark = ''
}
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

.form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.72);
}

.form :deep(.el-input__wrapper),
.form :deep(.el-select__wrapper),
.form :deep(.el-date-editor) {
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

.actions {
  display: flex;
  gap: 10px;
}
</style>
