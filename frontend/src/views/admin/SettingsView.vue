<template>
  <div class="settings-page">
    <div class="page-header">
      <div class="header-left">
        <el-icon class="header-icon"><Setting /></el-icon>
        <div class="header-text">
          <h1>系统配置</h1>
          <p>统一管理预约规则、提示与基础参数</p>
        </div>
      </div>
      <el-button @click="loadConfig" :loading="loading" plain>
        <el-icon><Refresh /></el-icon>
        刷新
      </el-button>
    </div>

    <el-tabs v-model="activeTab" class="config-tabs">
      <el-tab-pane label="预约规则" name="booking">
        <div class="config-section glass animate-in">
          <div class="section-header">
            <el-icon><Calendar /></el-icon>
            <h3>预约设置</h3>
          </div>
          <el-form :model="form" label-width="160px" class="config-form">
            <el-row :gutter="24">
              <el-col :xs="24" :sm="12">
                <el-form-item label="开放预约天数">
                  <el-input-number v-model="form.openDays" :min="1" :max="30" />
                  <div class="form-tip">学员可提前预约的最大天数</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="单人每日预约上限">
                  <el-input-number v-model="form.dailyLimit" :min="1" :max="5" />
                  <div class="form-tip">每个学员每天最多预约次数</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="最小预约时长">
                  <el-input-number v-model="form.minAppointmentHours" :min="1" :max="4" />
                  <span class="unit">小时</span>
                  <div class="form-tip">单次预约的最短时间</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="最大预约时长">
                  <el-input-number v-model="form.maxAppointmentHours" :min="1" :max="8" />
                  <span class="unit">小时</span>
                  <div class="form-tip">单次预约的最长时间</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="时间槽时长">
                  <el-input-number v-model="form.slotDuration" :min="15" :max="60" :step="15" />
                  <span class="unit">分钟</span>
                  <div class="form-tip">排班时间粒度</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="默认要求学时">
                  <el-input-number v-model="form.defaultRequiredHours" :min="1" :max="100" />
                  <span class="unit">小时</span>
                  <div class="form-tip">新学员默认要求完成的学时</div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>

        <div class="config-section glass animate-in" style="--delay: 0.1s">
          <div class="section-header">
            <el-icon><Clock /></el-icon>
            <h3>时间限制</h3>
          </div>
          <el-form :model="form" label-width="160px" class="config-form">
            <el-row :gutter="24">
              <el-col :xs="24" :sm="12">
                <el-form-item label="取消截止时间">
                  <el-input-number v-model="form.cancelDeadline" :min="0" :max="48" />
                  <span class="unit">小时</span>
                  <div class="form-tip">预约开始前多少小时内不可取消</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="确认超时时间">
                  <el-input-number v-model="form.confirmTimeout" :min="5" :max="120" />
                  <span class="unit">分钟</span>
                  <div class="form-tip">教练未确认自动取消的时间</div>
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="工作时间开始">
                  <el-time-select
                    v-model="form.workingStartTime"
                    start="06:00"
                    step="00:30"
                    end="12:00"
                    placeholder="选择时间"
                  />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="工作时间结束">
                  <el-time-select
                    v-model="form.workingEndTime"
                    start="12:00"
                    step="00:30"
                    end="22:00"
                    placeholder="选择时间"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="通知设置" name="notification">
        <div class="config-section glass animate-in">
          <div class="section-header">
            <el-icon><Bell /></el-icon>
            <h3>通知配置</h3>
          </div>
          <el-form :model="form" label-width="160px" class="config-form">
            <div class="switch-item">
              <div class="switch-info">
                <div class="switch-label">短信通知</div>
                <div class="switch-desc">预约确认、取消等操作发送短信通知</div>
              </div>
              <el-switch v-model="form.enableSmsNotify" />
            </div>
            <el-divider border-style="dashed" />
            <div class="switch-item">
              <div class="switch-info">
                <div class="switch-label">邮件通知</div>
                <div class="switch-desc">预约确认、取消等操作发送邮件通知</div>
              </div>
              <el-switch v-model="form.enableEmailNotify" />
            </div>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="系统公告" name="notice">
        <div class="config-section glass animate-in">
          <div class="section-header">
            <el-icon><Document /></el-icon>
            <h3>公告管理</h3>
          </div>
          <el-form :model="form" label-width="160px" class="config-form">
            <el-form-item label="系统公告">
              <el-input
                v-model="form.systemNotice"
                type="textarea"
                :rows="6"
                placeholder="请输入系统公告内容，支持换行..."
                maxlength="500"
                show-word-limit
              />
              <div class="form-tip">公告将显示在学员首页，可用于节假日安排、系统维护等通知</div>
            </el-form-item>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="高级设置" name="advanced">
        <div class="config-section glass animate-in">
          <div class="section-header">
            <el-icon><Tools /></el-icon>
            <h3>高级配置</h3>
          </div>
          <div class="advanced-list">
            <div v-for="config in allConfigs" :key="config.configKey" class="advanced-item">
              <div class="config-info">
                <div class="config-key">{{ config.configKey }}</div>
                <div class="config-desc">{{ config.description || '暂无描述' }}</div>
              </div>
              <div class="config-value">
                <el-tag :type="getConfigTypeTag(config.configType)">
                  {{ config.configType }}
                </el-tag>
                <el-input
                  v-if="config.configType === 'string'"
                  v-model="config.configValue"
                  size="small"
                  style="width: 200px"
                />
                <el-input-number
                  v-else-if="config.configType === 'number'"
                  v-model="config.configValue"
                  size="small"
                  style="width: 150px"
                />
                <el-switch
                  v-else-if="config.configType === 'boolean'"
                  v-model="config.configValue"
                  active-value="true"
                  inactive-value="false"
                />
                <span v-else>{{ config.configValue }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div class="action-bar glass animate-in" style="--delay: 0.2s">
      <div class="action-info">
        <el-icon><InfoFilled /></el-icon>
        <span>修改配置后请点击保存按钮生效</span>
      </div>
      <div class="action-buttons">
        <el-button @click="resetForm">重置</el-button>
        <el-button type="primary" @click="saveConfig" :loading="saving">
          <el-icon><Check /></el-icon>
          保存配置
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Setting, Refresh, Calendar, Clock, Bell, Document, 
  Tools, InfoFilled, Check
} from '@element-plus/icons-vue'
import { configApi } from '../../api/index'

const loading = ref(false)
const saving = ref(false)
const activeTab = ref('booking')
const allConfigs = ref([])

const form = reactive({
  openDays: 7,
  dailyLimit: 1,
  cancelDeadline: 2,
  confirmTimeout: 30,
  systemNotice: '',
  minAppointmentHours: 1,
  maxAppointmentHours: 4,
  slotDuration: 30,
  enableSmsNotify: false,
  enableEmailNotify: false,
  workingStartTime: '08:00',
  workingEndTime: '18:00',
  defaultRequiredHours: 12
})

const originalForm = reactive({})

const loadConfig = async () => {
  loading.value = true
  try {
    const res = await configApi.getAll()
    if (res.data && res.data.data) {
      const data = res.data.data
      form.openDays = data.openDays || 7
      form.dailyLimit = data.dailyLimit || 1
      form.cancelDeadline = data.cancelDeadline || 2
      form.confirmTimeout = data.confirmTimeout || 30
      form.systemNotice = data.systemNotice || ''
      form.minAppointmentHours = data.minAppointmentHours || 1
      form.maxAppointmentHours = data.maxAppointmentHours || 4
      form.slotDuration = data.slotDuration || 30
      form.enableSmsNotify = data.enableSmsNotify || false
      form.enableEmailNotify = data.enableEmailNotify || false
      form.workingStartTime = data.workingStartTime || '08:00'
      form.workingEndTime = data.workingEndTime || '18:00'
      form.defaultRequiredHours = data.defaultRequiredHours || 12
      allConfigs.value = data.allConfigs || []

      Object.assign(originalForm, form)
    }
  } catch (error) {
    console.error('加载配置失败:', error)
    ElMessage.error('加载配置失败')
  } finally {
    loading.value = false
  }
}

const saveConfig = async () => {
  saving.value = true
  try {
    const res = await configApi.update(form)
    if (res.data && res.data.code === 200) {
      ElMessage.success('配置已保存')
      Object.assign(originalForm, form)
    } else {
      ElMessage.error(res.data?.message || '保存失败')
    }
  } catch (error) {
    console.error('保存配置失败:', error)
    ElMessage.error('保存配置失败')
  } finally {
    saving.value = false
  }
}

const resetForm = () => {
  Object.assign(form, originalForm)
  ElMessage.info('已重置为上次保存的配置')
}

const getConfigTypeTag = (type) => {
  const typeMap = {
    string: '',
    number: 'success',
    boolean: 'warning',
    json: 'info'
  }
  return typeMap[type] || 'info'
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.settings-page {
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

.config-tabs {
  background: transparent;
}

.config-tabs :deep(.el-tabs__header) {
  margin-bottom: 16px;
}

.config-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.config-tabs :deep(.el-tabs__item) {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}

.config-tabs :deep(.el-tabs__item.is-active) {
  color: #409eff;
}

.config-tabs :deep(.el-tabs__active-bar) {
  background-color: #409eff;
}

.config-section {
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 16px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.section-header .el-icon {
  font-size: 20px;
  color: #409eff;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
}

.config-form :deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.72);
}

.config-form :deep(.el-input__wrapper),
.config-form :deep(.el-textarea__inner),
.config-form :deep(.el-input-number__decrease),
.config-form :deep(.el-input-number__increase) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.config-form :deep(.el-input__inner),
.config-form :deep(.el-textarea__inner) {
  color: rgba(255, 255, 255, 0.9);
}

.config-form :deep(.el-select .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06);
}

.form-tip {
  margin-top: 6px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.unit {
  margin-left: 8px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
}

.switch-info {
  flex: 1;
}

.switch-label {
  font-size: 14px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
}

.switch-desc {
  margin-top: 4px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.advanced-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.advanced-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.config-info {
  flex: 1;
}

.config-key {
  font-size: 14px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
  font-family: 'Consolas', monospace;
}

.config-desc {
  margin-top: 4px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.config-value {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-radius: 12px;
  position: sticky;
  bottom: 16px;
}

.action-info {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 13px;
}

.action-info .el-icon {
  color: #e6a23c;
}

.action-buttons {
  display: flex;
  gap: 12px;
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

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .action-bar {
    flex-direction: column;
    gap: 12px;
  }

  .action-buttons {
    width: 100%;
  }

  .action-buttons .el-button {
    flex: 1;
  }
}
</style>
