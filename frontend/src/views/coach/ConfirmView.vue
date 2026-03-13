<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">预约确认</div>
        <div class="s">对学员提交的预约进行确认/拒绝</div>
      </div>

      <div class="filters">
        <el-select v-model="status" style="width: 160px" placeholder="状态">
          <el-option label="待处理" value="pending" />
          <el-option label="已确认" value="confirmed" />
          <el-option label="已拒绝" value="rejected" />
        </el-select>
        <el-input v-model="keyword" style="width: 220px" placeholder="搜索学员/手机号" clearable />
        <el-button type="primary" plain>查询</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="student" label="学员" width="140" />
        <el-table-column prop="mobile" label="手机号" width="140" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="slot" label="时间段" width="140" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.type" effect="dark">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="210">
          <template #default="scope">
            <el-button size="small" type="success" plain :disabled="scope.row.status !== '待处理'" @click="approve(scope.row)">
              确认
            </el-button>
            <el-button size="small" type="danger" plain :disabled="scope.row.status !== '待处理'" @click="reject(scope.row)">
              拒绝
            </el-button>
            <el-button size="small" type="primary" plain>详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const status = ref('pending')
const keyword = ref('')

const rows = ref([
  { student: '张三', mobile: '13800138000', date: '2026-03-14', slot: '14:00-15:00', status: '待处理', type: 'warning' },
  { student: '李四', mobile: '13800138001', date: '2026-03-12', slot: '09:00-10:00', status: '已确认', type: 'success' },
])

function approve(row) {
  row.status = '已确认'
  row.type = 'success'
}

function reject(row) {
  row.status = '已拒绝'
  row.type = 'danger'
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

.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 12px;
}

.filters :deep(.el-input__wrapper),
.filters :deep(.el-select__wrapper) {
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
</style>
