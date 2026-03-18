<template>
  <div class="stats-page">
    <div class="page-header">
      <div class="header-left">
        <el-icon class="header-icon"><DataAnalysis /></el-icon>
        <div class="header-text">
          <h1>数据统计分析</h1>
          <p>关键指标一屏掌握，实时数据驱动决策</p>
        </div>
      </div>
      <el-button @click="refreshData" :loading="loading" type="primary" plain>
        <el-icon><Refresh /></el-icon>
        刷新数据
      </el-button>
    </div>

    <div class="stats-grid">
      <div class="stat-card glass animate-in" style="--delay: 0.1s">
        <div class="stat-icon today">
          <el-icon><Calendar /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">今日预约</div>
          <div class="stat-value">{{ stats.todayBookings || 0 }}</div>
          <div class="stat-sub">今日学时 {{ stats.todayHours || 0 }} 小时</div>
        </div>
      </div>

      <div class="stat-card glass animate-in" style="--delay: 0.2s">
        <div class="stat-icon pending">
          <el-icon><Clock /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">待确认</div>
          <div class="stat-value">{{ stats.pendingConfirm || 0 }}</div>
          <div class="stat-sub">等待教练确认</div>
        </div>
      </div>

      <div class="stat-card glass animate-in" style="--delay: 0.3s">
        <div class="stat-icon coach">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">活跃教练</div>
          <div class="stat-value">{{ stats.activeCoaches || 0 }}</div>
          <div class="stat-sub">总教练 {{ stats.totalCoaches || 0 }} 人</div>
        </div>
      </div>

      <div class="stat-card glass animate-in" style="--delay: 0.4s">
        <div class="stat-icon vehicle">
          <el-icon><Van /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">可用车辆</div>
          <div class="stat-value">{{ stats.availableVehicles || 0 }}</div>
          <div class="stat-sub">总车辆 {{ stats.totalVehicles || 0 }} 辆</div>
        </div>
      </div>
    </div>

    <div class="summary-row">
      <div class="summary-card glass animate-in" style="--delay: 0.5s">
        <div class="summary-item">
          <el-icon class="summary-icon"><UserFilled /></el-icon>
          <div class="summary-content">
            <div class="summary-value">{{ stats.totalStudents || 0 }}</div>
            <div class="summary-label">注册学员</div>
          </div>
        </div>
      </div>
      <div class="summary-card glass animate-in" style="--delay: 0.6s">
        <div class="summary-item">
          <el-icon class="summary-icon"><Timer /></el-icon>
          <div class="summary-content">
            <div class="summary-value">{{ stats.totalCompletedHours || 0 }}</div>
            <div class="summary-label">累计学时(小时)</div>
          </div>
        </div>
      </div>
      <div class="summary-card glass animate-in" style="--delay: 0.7s">
        <div class="summary-item">
          <el-icon class="summary-icon"><Finished /></el-icon>
          <div class="summary-content">
            <div class="summary-value">{{ stats.totalCompletedSessions || 0 }}</div>
            <div class="summary-label">完成课程</div>
          </div>
        </div>
      </div>
      <div class="summary-card glass animate-in" style="--delay: 0.8s">
        <div class="summary-item">
          <el-icon class="summary-icon"><CircleCheck /></el-icon>
          <div class="summary-content">
            <div class="summary-value">{{ (stats.completionRate || 0).toFixed(1) }}%</div>
            <div class="summary-label">完成率</div>
          </div>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card glass animate-in" style="--delay: 0.9s">
        <div class="chart-header">
          <h3>近7天预约趋势</h3>
        </div>
        <div ref="trendChartRef" class="chart-container"></div>
      </div>

      <div class="chart-card glass animate-in" style="--delay: 1s">
        <div class="chart-header">
          <h3>热门时段分布</h3>
        </div>
        <div ref="hourlyChartRef" class="chart-container"></div>
      </div>
    </div>

    <div class="bottom-row">
      <div class="rank-card glass animate-in" style="--delay: 1.1s">
        <div class="rank-header">
          <h3>教练排行榜</h3>
          <span class="rank-subtitle">按完成课程数排序</span>
        </div>
        <div class="rank-list">
          <div v-for="(coach, index) in stats.topCoaches" :key="coach.coachId" class="rank-item">
            <div class="rank-number" :class="'rank-' + (index + 1)">{{ index + 1 }}</div>
            <div class="rank-info">
              <div class="rank-name">{{ coach.coachName }}</div>
              <div class="rank-stats">
                <span>完成 {{ coach.completedSessions }} 节</span>
                <span>学时 {{ coach.totalHours }} 小时</span>
              </div>
            </div>
            <div class="rank-rate">
              <el-progress 
                :percentage="coach.completionRate || 0" 
                :stroke-width="6"
                :color="getProgressColor(coach.completionRate)"
              />
            </div>
          </div>
          <div v-if="!stats.topCoaches || stats.topCoaches.length === 0" class="empty-state">
            暂无数据
          </div>
        </div>
      </div>

      <div class="metrics-card glass animate-in" style="--delay: 1.2s">
        <div class="metrics-header">
          <h3>关键指标</h3>
        </div>
        <div class="metrics-list">
          <div class="metric-item">
            <div class="metric-label">确认率</div>
            <div class="metric-bar">
              <el-progress 
                :percentage="stats.confirmRate || 0" 
                :stroke-width="10"
                color="#67c23a"
              />
            </div>
            <div class="metric-value">{{ (stats.confirmRate || 0).toFixed(1) }}%</div>
          </div>
          <div class="metric-item">
            <div class="metric-label">完成率</div>
            <div class="metric-bar">
              <el-progress 
                :percentage="stats.completionRate || 0" 
                :stroke-width="10"
                color="#409eff"
              />
            </div>
            <div class="metric-value">{{ (stats.completionRate || 0).toFixed(1) }}%</div>
          </div>
          <div class="metric-item">
            <div class="metric-label">爽约率</div>
            <div class="metric-bar">
              <el-progress 
                :percentage="stats.noShowRate || 0" 
                :stroke-width="10"
                color="#f56c6c"
              />
            </div>
            <div class="metric-value">{{ (stats.noShowRate || 0).toFixed(1) }}%</div>
          </div>
        </div>

        <el-divider border-style="dashed" />

        <div class="peak-info">
          <el-icon><AlarmClock /></el-icon>
          <div class="peak-content">
            <div class="peak-label">高峰时段</div>
            <div class="peak-value">{{ peakHours }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { 
  DataAnalysis, Calendar, Clock, User, Van, Refresh,
  UserFilled, Timer, Finished, CircleCheck, AlarmClock
} from '@element-plus/icons-vue'
import { statsApi } from '../../api/index'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const stats = ref({
  todayBookings: 0,
  pendingConfirm: 0,
  activeCoaches: 0,
  availableVehicles: 0,
  totalStudents: 0,
  totalCoaches: 0,
  totalVehicles: 0,
  todayHours: 0,
  totalCompletedHours: 0,
  totalCompletedSessions: 0,
  confirmRate: 0,
  completionRate: 0,
  noShowRate: 0,
  weeklyTrend: [],
  topCoaches: [],
  hourlyDistribution: []
})

const trendChartRef = ref(null)
const hourlyChartRef = ref(null)
let trendChart = null
let hourlyChart = null

const peakHours = computed(() => {
  const distribution = stats.value.hourlyDistribution || []
  if (distribution.length === 0) return '暂无数据'
  
  const sorted = [...distribution].sort((a, b) => b.count - a.count)
  const top3 = sorted.slice(0, 3).map(item => `${item.hour}:00`)
  
  if (top3.length === 0) return '暂无数据'
  return top3.join('、')
})

const getProgressColor = (rate) => {
  if (rate >= 80) return '#67c23a'
  if (rate >= 60) return '#e6a23c'
  return '#f56c6c'
}

const refreshData = async () => {
  loading.value = true
  try {
    const res = await statsApi.getDashboardStats()
    if (res.data && res.data.data) {
      stats.value = res.data.data
      updateCharts()
    } else {
      ElMessage.error('获取统计数据失败：返回数据格式错误')
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    if (error.response) {
      if (error.response.status === 403) {
        ElMessage.error('权限不足：请确认您以管理员身份登录')
      } else if (error.response.status === 401) {
        ElMessage.error('登录已过期，请重新登录')
      } else {
        ElMessage.error(`获取统计数据失败：${error.response.data?.message || error.message}`)
      }
    } else if (error.request) {
      ElMessage.error('网络错误：无法连接到服务器，请检查后端服务是否启动')
    } else {
      ElMessage.error(`请求失败：${error.message}`)
    }
  } finally {
    loading.value = false
  }
}

const initCharts = () => {
  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value)
  }
  if (hourlyChartRef.value) {
    hourlyChart = echarts.init(hourlyChartRef.value)
  }
}

const updateCharts = () => {
  updateTrendChart()
  updateHourlyChart()
}

const updateTrendChart = () => {
  if (!trendChart) return

  const trend = stats.value.weeklyTrend || []
  const dates = trend.map(item => item.date)
  const bookings = trend.map(item => item.bookings)
  const hours = trend.map(item => item.hours)
  const completed = trend.map(item => item.completed)

  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0, 0, 0, 0.7)',
      borderColor: 'transparent',
      textStyle: { color: '#fff' }
    },
    legend: {
      data: ['预约数', '学时数', '完成数'],
      textStyle: { color: 'rgba(255, 255, 255, 0.7)' },
      top: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.2)' } },
      axisLabel: { color: 'rgba(255, 255, 255, 0.7)' }
    },
    yAxis: [
      {
        type: 'value',
        axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.2)' } },
        axisLabel: { color: 'rgba(255, 255, 255, 0.7)' },
        splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
      },
      {
        type: 'value',
        axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.2)' } },
        axisLabel: { color: 'rgba(255, 255, 255, 0.7)' },
        splitLine: { show: false }
      }
    ],
    series: [
      {
        name: '预约数',
        type: 'bar',
        data: bookings,
        itemStyle: { 
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#409eff' },
            { offset: 1, color: '#79bbff' }
          ]),
          borderRadius: [4, 4, 0, 0]
        }
      },
      {
        name: '学时数',
        type: 'line',
        yAxisIndex: 1,
        data: hours,
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { color: '#67c23a', width: 2 },
        itemStyle: { color: '#67c23a' }
      },
      {
        name: '完成数',
        type: 'line',
        yAxisIndex: 0,
        data: completed,
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        lineStyle: { color: '#e6a23c', width: 2 },
        itemStyle: { color: '#e6a23c' }
      }
    ]
  }

  trendChart.setOption(option)
}

const updateHourlyChart = () => {
  if (!hourlyChart) return

  const distribution = stats.value.hourlyDistribution || []
  const hours = distribution.map(item => `${item.hour}:00`)
  const counts = distribution.map(item => item.count)

  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0, 0, 0, 0.7)',
      borderColor: 'transparent',
      textStyle: { color: '#fff' },
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '8%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: hours,
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.2)' } },
      axisLabel: { 
        color: 'rgba(255, 255, 255, 0.7)',
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.2)' } },
      axisLabel: { color: 'rgba(255, 255, 255, 0.7)' },
      splitLine: { lineStyle: { color: 'rgba(255, 255, 255, 0.1)' } }
    },
    series: [
      {
        type: 'bar',
        data: counts,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#f56c6c' },
            { offset: 1, color: '#fab6b6' }
          ]),
          borderRadius: [4, 4, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#f56c6c' },
              { offset: 1, color: '#f56c6c' }
            ])
          }
        }
      }
    ]
  }

  hourlyChart.setOption(option)
}

const handleResize = () => {
  trendChart?.resize()
  hourlyChart?.resize()
}

onMounted(() => {
  initCharts()
  refreshData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChart?.dispose()
  hourlyChart?.dispose()
})
</script>

<style scoped>
.stats-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  font-size: 32px;
  color: #409eff;
}

.header-text h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.95);
}

.header-text p {
  margin: 4px 0 0;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-radius: 12px;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.today {
  background: linear-gradient(135deg, #409eff 0%, #79bbff 100%);
  color: white;
}

.stat-icon.pending {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
  color: white;
}

.stat-icon.coach {
  background: linear-gradient(135deg, #67c23a 0%, #95d475 100%);
  color: white;
}

.stat-icon.vehicle {
  background: linear-gradient(135deg, #f56c6c 0%, #fab6b6 100%);
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.95);
  margin: 4px 0;
}

.stat-sub {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.summary-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

@media (max-width: 960px) {
  .summary-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

.summary-card {
  padding: 16px 20px;
  border-radius: 12px;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.summary-icon {
  font-size: 24px;
  color: #409eff;
}

.summary-value {
  font-size: 22px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.95);
}

.summary-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.charts-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

@media (max-width: 960px) {
  .charts-row {
    grid-template-columns: 1fr;
  }
}

.chart-card {
  padding: 20px;
  border-radius: 12px;
}

.chart-header {
  margin-bottom: 16px;
}

.chart-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.chart-container {
  height: 280px;
}

.bottom-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

@media (max-width: 960px) {
  .bottom-row {
    grid-template-columns: 1fr;
  }
}

.rank-card {
  padding: 20px;
  border-radius: 12px;
}

.rank-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.rank-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.rank-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.rank-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
}

.rank-number {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  background: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.7);
}

.rank-number.rank-1 {
  background: linear-gradient(135deg, #ffd700 0%, #ffec8b 100%);
  color: #8b6914;
}

.rank-number.rank-2 {
  background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
  color: #666;
}

.rank-number.rank-3 {
  background: linear-gradient(135deg, #cd7f32 0%, #daa06d 100%);
  color: #5c3d1e;
}

.rank-info {
  flex: 1;
}

.rank-name {
  font-size: 14px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.rank-stats {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 2px;
}

.rank-stats span {
  margin-right: 12px;
}

.rank-rate {
  width: 100px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
}

.metrics-card {
  padding: 20px;
  border-radius: 12px;
}

.metrics-header {
  margin-bottom: 16px;
}

.metrics-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.metrics-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.metric-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.metric-label {
  width: 60px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
}

.metric-bar {
  flex: 1;
}

.metric-value {
  width: 50px;
  text-align: right;
  font-size: 13px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.peak-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
}

.peak-info .el-icon {
  font-size: 24px;
  color: #e6a23c;
}

.peak-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.peak-value {
  font-size: 14px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
  margin-top: 2px;
}

.animate-in {
  animation: fadeInUp 0.5s ease forwards;
  opacity: 0;
  animation-delay: var(--delay, 0s);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
