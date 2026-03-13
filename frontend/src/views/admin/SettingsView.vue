<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">系统配置</div>
        <div class="s">统一管理预约规则、提示与基础参数（演示）</div>
      </div>

      <el-form :model="form" label-width="140px" class="form">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="开放预约天数">
              <el-input-number v-model="form.openDays" :min="1" :max="30" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="单人每日预约上限">
              <el-input-number v-model="form.dailyLimit" :min="1" :max="5" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="取消截止（小时）">
              <el-input-number v-model="form.cancelDeadline" :min="0" :max="48" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="预约确认超时（分钟）">
              <el-input-number v-model="form.confirmTimeout" :min="5" :max="120" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="系统公告">
          <el-input v-model="form.notice" type="textarea" :rows="4" placeholder="例如：节假日排班说明" />
        </el-form-item>

        <div class="actions">
          <el-button type="primary" @click="save">保存配置</el-button>
          <el-button @click="reset">重置</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const form = reactive({
  openDays: 7,
  dailyLimit: 1,
  cancelDeadline: 2,
  confirmTimeout: 30,
  notice: '欢迎使用系统。请提前预约，按时到场。',
})

function save() {
  ElMessage.success('已保存（演示）')
}

function reset() {
  form.openDays = 7
  form.dailyLimit = 1
  form.cancelDeadline = 2
  form.confirmTimeout = 30
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
.form :deep(.el-textarea__inner),
.form :deep(.el-input-number__decrease),
.form :deep(.el-input-number__increase) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.actions {
  display: flex;
  gap: 10px;
}
</style>
