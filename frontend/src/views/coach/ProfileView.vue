<template>
  <div class="wrap">
    <div class="glass card">
      <div class="head">
        <div class="h">个人信息</div>
        <div class="s">维护教练资料与联系方式</div>
      </div>

      <el-form :model="form" label-width="96px" class="form">
        <el-row :gutter="16">
          <el-col :xs="24" :sm="12">
            <el-form-item label="姓名">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="手机号">
              <el-input v-model="form.mobile" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="工号">
              <el-input v-model="form.code" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" style="width: 100%">
                <el-option label="在岗" value="on" />
                <el-option label="休息" value="off" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="擅长科目">
          <el-select v-model="form.skills" multiple style="width: 100%" placeholder="请选择">
            <el-option label="科目二" value="k2" />
            <el-option label="科目三" value="k3" />
          </el-select>
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
  name: '教练用户',
  mobile: '13900139000',
  code: 'C-0001',
  status: 'on',
  skills: ['k2'],
})

function onSave() {
  ElMessage.success('已保存（演示）')
}

function onReset() {
  form.status = 'on'
  form.skills = ['k2']
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
