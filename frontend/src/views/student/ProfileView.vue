<template>
  <div class="wrap">
    <div class="glass card">
      <div class="head">
        <div class="h">个人信息</div>
        <div class="s">完善资料，方便教练确认与系统通知</div>
      </div>

      <el-form :model="form" label-width="96px" class="form">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="姓名">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="手机号">
              <el-input v-model="form.mobile" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="证件号">
              <el-input v-model="form.idNo" placeholder="可选" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="车型">
              <el-select v-model="form.carType" placeholder="请选择">
                <el-option label="C1" value="C1" />
                <el-option label="C2" value="C2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="可选" />
        </el-form-item>

        <div class="actions">
          <el-button type="primary" @click="onSave">保存</el-button>
          <el-button @click="onReset">重置</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

const form = reactive({
  name: '学员用户',
  mobile: '13800138000',
  idNo: '',
  carType: 'C1',
  remark: '',
})

function onSave() {
  ElMessage.success('已保存（演示）')
}

function onReset() {
  form.idNo = ''
  form.remark = ''
  form.carType = 'C1'
}
</script>

<style scoped>
.wrap {
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
.form :deep(.el-select__wrapper) {
  background: rgba(255, 255, 255, 0.06);
  box-shadow: none;
  border: 1px solid rgba(255, 255, 255, 0.10);
}

.actions {
  display: flex;
  gap: 10px;
}
</style>
