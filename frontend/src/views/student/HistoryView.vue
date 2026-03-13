<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">预约历史记录</div>
        <div class="s">可筛选查看你的全部预约状态</div>
      </div>

      <div class="filters">
        <el-select v-model="status" placeholder="状态" style="width: 160px">
          <el-option label="全部" value="all" />
          <el-option label="待确认" value="pending" />
          <el-option label="已确认" value="confirmed" />
          <el-option label="已完成" value="done" />
          <el-option label="已取消" value="canceled" />
        </el-select>
        <el-date-picker v-model="range" type="daterange" start-placeholder="开始日期" end-placeholder="结束日期" />
        <el-button type="primary" plain>查询</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="slot" label="时间段" width="140" />
        <el-table-column prop="coach" label="教练" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.type" effect="dark">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const status = ref('all')
const range = ref('')

const rows = ref([
  { date: '2026-03-12', slot: '09:00-10:00', coach: '王教练', status: '已确认', type: 'success', remark: '' },
  { date: '2026-03-11', slot: '14:00-15:00', coach: '李教练', status: '待确认', type: 'warning', remark: '想练倒库' },
  { date: '2026-03-05', slot: '10:00-11:00', coach: '王教练', status: '已取消', type: 'info', remark: '' },
])
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
.filters :deep(.el-date-editor),
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
