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

const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入新密码'))
  } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(value)) {
    callback(new Error('密码长度需8-20位，包含字母和数字'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, validator: validatePassword, trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }],
}

async function onChangePassword() {
  try {
    await passwordFormRef.value.validate()
    passwordLoading.value = true
    const res = await http.put('/api/user/coach/password', {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    })
    if (res.data.code === 200) {
      ElMessage.success('密码修改成功')
      onResetPassword()
    } else {
      ElMessage.error(res.data.msg || '密码修改失败')
    }
  } catch (error) {
    const msg = error.response?.data?.msg || error.response?.data?.message
    if (msg) {
      ElMessage.error(msg)
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
