<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">学时查询</div>
        <div class="s">查看你的累计学时、剩余学时与近期记录</div>
      </div>

      <div class="stats">
        <div class="stat glass">
          <div class="k">累计学时</div>
          <div class="v">{{ total }} h</div>
        </div>
        <div class="stat glass">
          <div class="k">已完成</div>
          <div class="v">{{ done }} h</div>
        </div>
        <div class="stat glass">
          <div class="k">剩余</div>
          <div class="v">{{ total - done }} h</div>
        </div>
      </div>

      <el-divider border-style="dashed" />

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="coach" label="教练" width="120" />
        <el-table-column prop="hours" label="学时" width="100" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const total = 40
const done = ref(12)

const rows = ref([
  { date: '2026-03-12', coach: '王教练', hours: 2, remark: '倒库练习' },
  { date: '2026-03-10', coach: '李教练', hours: 2, remark: '直角转弯' },
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

.stats {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.stat {
  padding: 12px 14px;
}

.k {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.v {
  margin-top: 8px;
  color: rgba(255, 255, 255, 0.92);
  font-weight: 800;
  font-size: 18px;
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
