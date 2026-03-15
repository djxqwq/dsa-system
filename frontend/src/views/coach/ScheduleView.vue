<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><Calendar /></el-icon>
        </div>
        <div class="h">排班管理</div>
        <div class="s">设置可预约时段，确保学员能够顺利预约</div>
      </div>

      <div class="tools">
        <el-date-picker v-model="date" type="date" placeholder="选择日期" class="tool-item" />
        <el-button type="primary" plain @click="addSlot" class="tool-item">
          <el-icon><Plus /></el-icon>
          新增时段
        </el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="slot" label="时间段" width="160" />
        <el-table-column prop="capacity" label="可约名额" width="120" />
        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.enabled ? 'success' : 'info'" effect="dark" class="status-tag">
              <el-icon v-if="scope.row.enabled"><Check /></el-icon>
              <el-icon v-else><Close /></el-icon>
              {{ scope.row.enabled ? '开放' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="180">
          <template #default="scope">
            <el-button size="small" type="primary" plain @click="toggle(scope.row)" class="action-btn">
              <el-icon v-if="scope.row.enabled"><Close /></el-icon>
              <el-icon v-else><Check /></el-icon>
              {{ scope.row.enabled ? '关闭' : '开放' }}
            </el-button>
            <el-button size="small" type="danger" plain @click="remove(scope.$index)" class="action-btn">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Calendar, Plus, Check, Close, Delete } from '@element-plus/icons-vue'

const date = ref('')

const rows = ref([
  { date: '2026-03-14', slot: '09:00-10:00', capacity: 1, enabled: true },
  { date: '2026-03-14', slot: '10:00-11:00', capacity: 1, enabled: false },
])

function addSlot() {
  ElMessage.info('已新增时段（演示）')
}

function toggle(row) {
  row.enabled = !row.enabled
}

function remove(idx) {
  rows.value.splice(idx, 1)
}
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
