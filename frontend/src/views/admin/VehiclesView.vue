<template>
  <div class="grid">
    <div class="glass card">
      <div class="head">
        <div class="h">车辆管理</div>
        <div class="s">维护车辆信息与可用状态</div>
      </div>

      <div class="tools">
        <el-input v-model="keyword" style="width: 240px" placeholder="搜索车牌" clearable />
        <el-select v-model="type" style="width: 160px" placeholder="车型">
          <el-option label="全部" value="all" />
          <el-option label="C1" value="C1" />
          <el-option label="C2" value="C2" />
        </el-select>
        <el-button type="primary" plain>查询</el-button>
        <el-button type="primary">新增车辆</el-button>
      </div>

      <el-table :data="rows" style="width: 100%" class="table">
        <el-table-column prop="plate" label="车牌" width="160" />
        <el-table-column prop="carType" label="车型" width="100" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === '可用' ? 'success' : 'warning'" effect="dark">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" min-width="220">
          <template #default>
            <el-button size="small" type="primary" plain>编辑</el-button>
            <el-button size="small" type="warning" plain>维修</el-button>
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
const type = ref('all')

const rows = ref([
  { plate: '粤B·A12345', carType: 'C1', status: '可用', remark: '' },
  { plate: '粤B·B23456', carType: 'C2', status: '维修中', remark: '更换轮胎' },
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

.tools :deep(.el-input__wrapper),
.tools :deep(.el-select__wrapper) {
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
