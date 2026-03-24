<template>
  <div class="messages-page">
    <div class="page-header">
      <div class="header-left">
        <el-icon class="header-icon"><Message /></el-icon>
        <div class="header-text">
          <h1>消息中心</h1>
          <p>查看系统通知和提醒</p>
        </div>
      </div>
      <div class="header-right" v-if="selectedMessages.length > 0">
        <el-button size="small" @click="markAsRead" :loading="batchLoading">
          <el-icon><Check /></el-icon> 标记已读
        </el-button>
        <el-button size="small" type="danger" @click="deleteMessages" :loading="batchLoading" style="margin-left: 8px">
          <el-icon><Delete /></el-icon> 删除
        </el-button>
      </div>
    </div>

    <div class="glass card animate-in">
      <div class="filter-section">
        <el-select v-model="filterType" placeholder="消息类型" size="small" style="width: 160px; margin-right: 12px">
          <el-option label="全部" value="all" />
          <el-option label="系统通知" value="system" />
          <el-option label="预约提醒" value="booking" />
          <el-option label="学时提醒" value="hours" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="消息状态" size="small" style="width: 120px">
          <el-option label="全部" value="all" />
          <el-option label="未读" value="unread" />
          <el-option label="已读" value="read" />
        </el-select>
      </div>

      <div class="messages-list" v-loading="loading">
        <div v-if="filteredMessages.length === 0" class="empty-state">
          <el-icon class="empty-icon"><MessageBox /></el-icon>
          <p>暂无消息</p>
        </div>
        <el-checkbox-group v-model="selectedMessages" class="messages-container">
          <div 
            v-for="message in filteredMessages" 
            :key="message.id" 
            class="message-item" 
            :class="{ 'unread': !message.read }"
            @click="viewMessage(message)"
          >
            <div class="message-checkbox">
              <el-checkbox :value="message.id" @click.stop />
            </div>
            <div class="message-content">
              <div class="message-header">
                <span class="message-title">{{ message.title }}</span>
                <span class="message-time">{{ formatTime(message.createdAt) }}</span>
                <el-tag v-if="message.type === 'system'" size="small" type="info" effect="plain" class="message-tag">系统</el-tag>
                <el-tag v-else-if="message.type === 'booking'" size="small" type="primary" effect="plain" class="message-tag">预约</el-tag>
                <el-tag v-else-if="message.type === 'hours'" size="small" type="success" effect="plain" class="message-tag">学时</el-tag>
              </div>
              <div class="message-body">{{ message.content }}</div>
            </div>
            <div v-if="!message.read" class="unread-indicator"></div>
          </div>
        </el-checkbox-group>
      </div>

      <div v-if="total > 0" class="pagination-section">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="currentMessage.title"
      width="500px"
    >
      <div class="message-detail">
        <div class="detail-meta">
          <span class="detail-time">{{ formatTime(currentMessage.createdAt) }}</span>
          <el-tag v-if="currentMessage.type === 'system'" size="small" type="info" effect="plain">系统通知</el-tag>
          <el-tag v-else-if="currentMessage.type === 'booking'" size="small" type="primary" effect="plain">预约提醒</el-tag>
          <el-tag v-else-if="currentMessage.type === 'hours'" size="small" type="success" effect="plain">学时提醒</el-tag>
        </div>
        <div class="detail-content">{{ currentMessage.content }}</div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElNotification } from 'element-plus'
import { Message, Check, Delete, MessageBox, InfoFilled } from '@element-plus/icons-vue'
import http from '../../api/http'

const loading = ref(false)
const batchLoading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterType = ref('all')
const filterStatus = ref('all')
const selectedMessages = ref([])
const messages = ref([])
const dialogVisible = ref(false)
const currentMessage = ref({})

const filteredMessages = computed(() => {
  return messages.value.filter(message => {
    const typeMatch = filterType.value === 'all' || message.type === filterType.value
    const statusMatch = filterStatus.value === 'all' || 
                      (filterStatus.value === 'unread' && !message.read) ||
                      (filterStatus.value === 'read' && message.read)
    return typeMatch && statusMatch
  })
})

async function loadMessages() {
  try {
    loading.value = true
    const res = await http.get('/api/messages', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    if (res.data.code === 200) {
      // 过滤掉内容为空的消息
      messages.value = res.data.data.records.filter(message => message.content && message.content.trim() !== '')
      total.value = messages.value.length
    }
  } catch (error) {
    console.error('加载消息失败', error)
    ElMessage.error('加载消息失败')
  } finally {
    loading.value = false
  }
}

async function markAsRead() {
  if (selectedMessages.value.length === 0) return
  
  try {
    batchLoading.value = true
    const res = await http.post('/api/messages/read', {
      ids: selectedMessages.value
    })
    if (res.data.code === 200) {
      messages.value = messages.value.map(msg => 
        selectedMessages.value.includes(msg.id) ? { ...msg, read: true } : msg
      )
      selectedMessages.value = []
      ElMessage.success('标记已读成功')
    }
  } catch (error) {
    console.error('标记已读失败', error)
    ElMessage.error('标记已读失败')
  } finally {
    batchLoading.value = false
  }
}

async function deleteMessages() {
  if (selectedMessages.value.length === 0) return
  
  try {
    batchLoading.value = true
    const res = await http.delete('/api/messages', {
      params: {
        ids: selectedMessages.value.join(',')
      }
    })
    if (res.data.code === 200) {
      messages.value = messages.value.filter(msg => !selectedMessages.value.includes(msg.id))
      total.value -= selectedMessages.value.length
      selectedMessages.value = []
      ElMessage.success('删除成功')
    }
  } catch (error) {
    console.error('删除失败', error)
    ElMessage.error('删除失败')
  } finally {
    batchLoading.value = false
  }
}

function viewMessage(message) {
  currentMessage.value = message
  dialogVisible.value = true
  
  if (!message.read) {
    markSingleAsRead(message.id)
  }
}

async function markSingleAsRead(id) {
  try {
    await http.post('/api/messages/read', {
      ids: [id]
    })
    messages.value = messages.value.map(msg => 
      msg.id === id ? { ...msg, read: true } : msg
    )
  } catch (error) {
    console.error('标记已读失败', error)
  }
}

function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
  loadMessages()
}

function handleCurrentChange(page) {
  currentPage.value = page
  loadMessages()
}

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function simulateNewMessage() {
  const messageContents = [
    '您有一条新的预约提醒，请及时查看。',
    '系统将于今晚23:00进行维护，届时可能无法访问。',
    '您的账号已成功绑定手机号。',
    '新功能上线：现在可以查看历史预约记录。',
    '您的个人信息已更新成功。',
    '学员陈明华已预约您明天上午8:00-10:00的课程。',
    '您有3名学员需要更新学时记录。',
    '学员王建国已取消明天下午的预约。',
    '您的排班表已更新。',
    '学员张丽娜已完成10小时学时。',
    '您明天有4名学员预约练车。',
    '新功能上线：现在可以查看学员详细信息。',
    '学员吴鹏飞已预约您后天的课程。'
  ]
  
  const randomContent = messageContents[Math.floor(Math.random() * messageContents.length)]
  
  const newMessage = {
    id: Date.now(),
    title: '系统通知',
    content: randomContent,
    type: 'system',
    read: false,
    createdAt: new Date().toISOString()
  }
  
  messages.value.unshift(newMessage)
  total.value++
  
  ElNotification({
    title: '新消息',
    message: newMessage.content,
    type: 'info',
    duration: 3000
  })
}

watch([filterType, filterStatus], () => {
  currentPage.value = 1
  loadMessages()
})

onMounted(() => {
  loadMessages()
  
  setInterval(() => {
    if (Math.random() > 0.95) {
      simulateNewMessage()
    }
  }, 30000)
})
</script>

<style scoped>
.messages-page {
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

.header-right {
  display: flex;
  align-items: center;
}

.card {
  padding: 24px;
  border-radius: 12px;
}

.filter-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.messages-list {
  margin-bottom: 20px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: rgba(255, 255, 255, 0.5);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.messages-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 8px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  cursor: pointer;
  transition: all 0.3s ease;
}

.message-item:hover {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(64, 158, 255, 0.2);
  transform: translateY(-1px);
}

.message-item.unread {
  background: rgba(64, 158, 255, 0.05);
  border-color: rgba(64, 158, 255, 0.15);
}

.message-checkbox {
  margin-right: 12px;
  margin-top: 4px;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  gap: 12px;
}

.message-title {
  font-size: 15px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.9);
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.message-time {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  white-space: nowrap;
}

.message-tag {
  white-space: nowrap;
}

.message-body {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.unread-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #409eff;
  margin-left: 12px;
  margin-top: 8px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.message-detail {
  padding: 10px 0;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.detail-time {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.detail-content {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
}

.animate-in {
  animation: fadeInUp 0.5s ease forwards;
  opacity: 0;
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
    align-items: flex-start;
    gap: 12px;
  }
  
  .header-right {
    width: 100%;
    justify-content: flex-end;
  }
  
  .filter-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .filter-section .el-select {
    width: 100% !important;
  }
}
</style>