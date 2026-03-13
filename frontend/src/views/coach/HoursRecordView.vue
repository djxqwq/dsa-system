<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">学时记录</div>
        <div class="s">记录学员训练学时，形成可追溯明细</div>
      </div>

      <div class="tools">
        <el-input v-model="form.student" style="width: 220px" placeholder="学员姓名/手机号" clearable />
        <el-date-picker v-model="form.date" type="date" placeholder="日期" />
        <el-input-number v-model="form.hours" :min="1" :max="8" />
        <el-button type="primary" @click="add">新增记录</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="student" label="学员" width="140" />
        <el-table-column prop="hours" label="学时" width="100" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" type="danger" plain @click="remove(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'

const form = reactive({
  student: '',
  date: '',
  hours: 2,
})

const rows = ref([
  { date: '2026-03-12', student: '张三', hours: 2, remark: '倒库练习' },
])

function add() {
  rows.value.unshift({
    date: form.date || '2026-03-14',
    student: form.student || '学员',
    hours: form.hours,
    remark: '',
  })
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
.tools :deep(.el-date-editor) {
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
