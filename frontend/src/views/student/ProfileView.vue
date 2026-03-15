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

    <div class="glass card">
      <div class="head">
        <div class="h">修改密码</div>
        <div class="s">定期修改密码可以提高账户安全性</div>
      </div>

      <el-form :model="passwordForm" label-width="96px" class="form" :rules="passwordRules" ref="passwordFormRef">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>

        <div class="actions">
          <el-button type="primary" @click="onChangePassword" :loading="passwordLoading">修改密码</el-button>
          <el-button @click="onResetPassword">重置</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import http from '../../api/http'

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

const passwordFormRef = ref(null)
const passwordLoading = ref(false)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' },
  ],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }],
}

async function onChangePassword() {
  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true
    const res = await http.put('/api/user/student/password', {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    })
    if (res.data.code === 200) {
      ElMessage.success('密码修改成功')
      onResetPassword()
    } else {
      ElMessage.error(res.data.message || '密码修改失败')
    }
  } catch (error) {
    if (error.response?.data?.message) {
      ElMessage.error(error.response.data.message)
    } else if (error !== false) {
      ElMessage.error('密码修改失败，请稍后重试')
    }
  } finally {
    passwordLoading.value = false
  }
}

function onResetPassword() {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.resetFields()
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
