<template>
  <div class="grid">
    <div class="glass card animate-in">
      <div class="head">
        <div class="icon-wrapper">
          <el-icon class="head-icon"><DataAnalysis /></el-icon>
        </div>
        <div class="h">数据统计分析</div>
        <div class="s">关键指标一屏掌握，后续可接图表库</div>
      </div>

      <div class="stats">
        <div class="stat glass animate-in" style="--delay: 0.1s">
          <div class="stat-icon">
            <el-icon><CalendarCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="k">今日预约</div>
            <div class="v">{{ todayBookings }}</div>
          </div>
          <div class="stat-trend trend-up">
            <el-icon><Top /></el-icon>
            +12%
          </div>
        </div>
        <div class="stat glass animate-in" style="--delay: 0.2s">
          <div class="stat-icon">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="k">待确认</div>
            <div class="v">{{ pendingConfirm }}</div>
          </div>
          <div class="stat-trend trend-down">
            <el-icon><Bottom /></el-icon>
            -5%
          </div>
        </div>
        <div class="stat glass animate-in" style="--delay: 0.3s">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="k">活跃教练</div>
            <div class="v">{{ activeCoaches }}</div>
          </div>
          <div class="stat-trend trend-neutral">
            <el-icon><Minus /></el-icon>
            0%
          </div>
        </div>
        <div class="stat glass animate-in" style="--delay: 0.4s">
          <div class="stat-icon">
            <el-icon><Van /></el-icon>
          </div>
          <div class="stat-content">
            <div class="k">车辆可用</div>
            <div class="v">{{ availableVehicles }}</div>
          </div>
          <div class="stat-trend trend-up">
            <el-icon><Top /></el-icon>
            +8%
          </div>
        </div>
      </div>

      <el-divider border-style="dashed" class="divider" />

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="name" label="指标" width="200" />
        <el-table-column prop="value" label="数值" width="150" />
        <el-table-column prop="trend" label="趋势" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { DataAnalysis, CalendarCheck, Clock, User, Van, Top, Bottom, Minus } from '@element-plus/icons-vue'

const todayBookings = ref(128)
const pendingConfirm = ref(23)
const activeCoaches = ref(18)
const availableVehicles = ref(26)

const rows = ref([
  { name: '预约确认平均耗时', value: '8.5 min', trend: '↓ 持续优化' },
  { name: '高峰时段', value: '14:00-16:00', trend: '↑ 需求集中' },
  { name: '取消率', value: '3.2%', trend: '→ 稳定' },
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
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

@media (max-width: 960px) {
  .stats {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
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
