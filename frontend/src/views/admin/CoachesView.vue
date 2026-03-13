<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">教练管理</div>
        <div class="s">维护教练账号、状态与擅长科目</div>
      </div>

      <div class="tools">
        <el-input v-model="keyword" style="width: 240px" placeholder="搜索姓名/手机号/工号" clearable />
        <el-button type="primary" plain>查询</el-button>
        <el-button type="primary">新增教练</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="name" label="姓名" width="140" />
        <el-table-column prop="mobile" label="手机号" width="160" />
        <el-table-column prop="code" label="工号" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === '在岗' ? 'success' : 'info'" effect="dark">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="skills" label="擅长" />
        <el-table-column label="操作" min-width="220">
          <template #default>
            <el-button size="small" type="primary" plain>编辑</el-button>
            <el-button size="small" type="warning" plain>重置密码</el-button>
            <el-button size="small" type="danger" plain>停用</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const keyword = ref('')

const rows = ref([
  { name: '王教练', mobile: '13900139000', code: 'C-0001', status: '在岗', skills: '科目二/科目三' },
  { name: '李教练', mobile: '13900139001', code: 'C-0002', status: '休息', skills: '科目二' },
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

.tools {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 12px;
}

.tools :deep(.el-input__wrapper) {
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
