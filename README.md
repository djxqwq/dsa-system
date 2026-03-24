# DSA System - 驾校练车预约数字化管理系统

## 项目简介

DSA System（Driving School Appointment System）是一个全栈开发的驾校练车预约数字化管理系统，旨在为驾校提供高效、便捷的学员练车预约、教练排班管理、车辆管理等一体化解决方案。系统采用前后端分离架构，支持学员、教练、管理员三种角色，实现了驾校日常运营管理的数字化和智能化。

## 功能特性

### 学员端功能
- **个人信息**：查看个人基本信息（姓名、手机号、学号、性别、报考车型、已练学时）
- **设置中心**：修改个人信息（姓名、手机号、性别、报考车型）和密码
- **练车预约**：查看教练可用时段，在线预约练车时间
- **学时查询**：查看学时记录（包含日期、时间段、教练、车辆、学时）
- **预约记录**：查看历史预约记录，包括预约状态、时间、教练等信息
- **消息中心**：查看系统通知、预约提醒和学时提醒，支持消息分类筛选和批量操作

### 教练端功能
- **个人信息**：查看个人基本信息（姓名、手机号、工号、在岗状态、已完成学时、关联车辆数）
- **设置中心**：修改个人信息（姓名、手机号、在岗状态）和密码
- **排班管理**：设置可预约的时间段，管理个人日程
- **预约管理**：管理所有预约记录，支持确认/拒绝预约、标记完成/爽约、撤销操作
- **学时管理**：记录学员的练车时长和训练内容
- **车辆管理**：查看和管理分配的教练车信息，记录保养维修
- **学员管理**：管理分配给自己的学员，设置要求学时，查看学员性别
- **消息中心**：查看系统通知、预约提醒和学时提醒，支持消息分类筛选和批量操作

### 管理员端功能
- **学员管理**：管理学员账户，包括新增、编辑、禁用等操作，支持设置性别
- **教练管理**：管理教练信息，分配车辆，设置启用状态和在岗状态
- **车辆管理**：管理教练车信息，包括车辆档案、维修保养记录
- **数据统计分析**：可视化仪表盘，展示预约统计、学时统计、教练排行等运营数据
- **系统配置**：系统参数设置，包括预约规则、时间限制、通知设置、系统公告等

## 技术架构

### 后端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.15 | 核心框架 |
| Java | 17 | 开发语言 |
| MyBatis-Plus | 3.5.5 | ORM框架，简化数据库操作 |
| Spring Security | - | 安全框架，实现认证授权 |
| JWT (jjwt) | 0.9.1 | JSON Web Token，实现无状态认证 |
| MySQL | 8.0 | 关系型数据库 |
| Lombok | 1.18.28 | 简化Java代码 |

### 前端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.5.12 | 渐进式JavaScript框架 |
| Vite | 5.4.10 | 下一代前端构建工具 |
| Vue Router | 4.4.5 | 官方路由管理器 |
| Pinia | 2.2.6 | Vue 3 状态管理库 |
| Element Plus | 2.8.8 | Vue 3 UI组件库 |
| Axios | 1.7.9 | HTTP请求库 |
| ECharts | 5.x | 数据可视化图表库 |
| @iconify/vue | 4.1.2 | 图标库 |

## 项目结构

```
dsa-system/
├── frontend/                    # 前端项目
│   ├── src/
│   │   ├── api/                 # API接口封装
│   │   ├── layouts/             # 布局组件
│   │   ├── router/              # 路由配置
│   │   ├── stores/              # Pinia状态管理
│   │   ├── views/               # 页面组件
│   │   │   ├── admin/           # 管理员页面
│   │   │   │   ├── UsersView.vue        # 学员管理
│   │   │   │   ├── CoachesView.vue      # 教练管理
│   │   │   │   ├── VehiclesView.vue     # 车辆管理
│   │   │   │   ├── StatsView.vue        # 数据统计分析
│   │   │   │   └── SettingsView.vue     # 系统配置
│   │   │   ├── coach/           # 教练页面
│   │   │   │   ├── ProfileView.vue      # 个人信息
│   │   │   │   ├── ScheduleView.vue     # 排班管理
│   │   │   │   ├── AppointmentsView.vue # 预约管理
│   │   │   │   ├── HoursRecordView.vue  # 学时管理
│   │   │   │   ├── VehiclesView.vue     # 车辆管理
│   │   │   │   ├── StudentsView.vue     # 学员管理
│   │   │   │   ├── MessagesView.vue     # 消息中心
│   │   │   │   └── SettingsView.vue     # 设置中心
│   │   │   └── student/         # 学员页面
│   │   │       ├── ProfileView.vue      # 个人信息
│   │   │       ├── SettingsView.vue     # 设置中心
│   │   │       ├── BookingView.vue      # 练车预约
│   │   │       ├── HoursView.vue        # 学时查询
│   │   │       ├── HistoryView.vue      # 预约记录
│   │   │       └── MessagesView.vue     # 消息中心
│   │   ├── App.vue              # 根组件
│   │   └── main.js              # 入口文件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js           # Vite配置
├── src/main/java/               # 后端源码
│   └── com/djxlzk/dsasystem/
│       ├── config/              # 配置类
│       ├── controller/          # 控制器层
│       ├── dto/                 # 数据传输对象
│       ├── entity/              # 实体类
│       ├── mapper/              # MyBatis Mapper
│       ├── service/             # 服务层
│       └── util/                # 工具类
├── src/main/resources/
│   ├── application.properties   # 应用配置
│   └── application-dev.properties
├── sql/                         # SQL脚本
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 数据库设计

### 核心数据表
| 表名 | 说明 |
|------|------|
| admin | 管理员信息表 |
| coach | 教练信息表（含work_status在岗状态字段） |
| student | 学员信息表（含gender性别字段） |
| vehicle | 车辆信息表 |
| schedule | 教练排班表 |
| appointment | 预约记录表 |
| maintenance_record | 保养记录表 |
| repair_record | 维修记录表 |
| system_config | 系统配置表 |
| message | 消息表（含student_id学员ID、coach_id教练ID字段） |

## 快速开始

### 环境要求
- JDK 17 或更高版本
- Node.js 16+ 
- MySQL 8.0
- Maven 3.6+

### 后端启动

1. 克隆项目到本地
```bash
git clone <repository-url>
cd dsa-system
```

2. 配置数据库连接（修改 `src/main/resources/application.properties`）
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/driving_school_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. 初始化数据库
```bash
# 执行 sql 目录下的SQL脚本创建数据库和表
# 按顺序执行：
# 1. student.sql - 创建学员表
# 2. vehicle.sql - 创建车辆表
# 3. schedule_appointment.sql - 创建排班和预约表
# 4. maintenance_repair_record.sql - 创建保养维修记录表
# 5. system_config.sql - 创建系统配置表
# 6. create_message_table.sql - 创建消息表
# 7. init_admin.sql - 初始化管理员账户
```

4. 启动后端服务
```bash
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 前端启动

```bash
cd frontend
npm install    # 首次运行需要安装依赖
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

## 构建部署

### 后端构建
```bash
mvn clean package
```
生成的JAR包位于 `target/` 目录下

### 前端构建
```bash
cd frontend
npm run build
```
构建产物位于 `frontend/dist/` 目录下

## API接口说明

### 认证接口
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 学员注册
- `GET /api/captcha` - 获取验证码

### 学员接口
- `GET /api/user/student/profile` - 获取个人信息
- `PUT /api/user/student/profile` - 更新个人信息
- `GET /api/schedule/available` - 获取可预约时段
- `POST /api/appointment` - 创建预约
- `GET /api/appointment/student` - 获取预约历史
- `GET /api/appointment/active` - 获取进行中的预约
- `DELETE /api/appointment/{id}` - 取消预约
- `GET /api/appointment/hours/stats` - 获取学时统计
- `GET /api/appointment/hours/records` - 获取学时记录

### 教练接口
- `GET /api/coach/profile` - 获取个人信息
- `PUT /api/coach/profile` - 更新个人信息
- `GET /api/schedule/coach` - 获取排班列表
- `POST /api/schedule` - 创建排班
- `PUT /api/schedule` - 更新排班
- `DELETE /api/schedule/{id}` - 删除排班
- `PUT /api/schedule/toggle/{id}` - 切换排班状态
- `GET /api/appointment/coach` - 获取教练预约列表
- `PUT /api/appointment/confirm/{id}` - 确认预约
- `PUT /api/appointment/reject/{id}` - 拒绝预约
- `PUT /api/appointment/complete/{id}` - 完成预约
- `PUT /api/appointment/noshow/{id}` - 标记爽约
- `PUT /api/appointment/undo/{id}` - 撤销预约状态
- `GET /api/appointment/hours/coach/records` - 获取教练学时记录
- `GET /api/vehicle/coach` - 获取教练车辆
- `GET /api/maintenance/coach` - 获取保养记录
- `POST /api/maintenance` - 添加保养记录
- `GET /api/repair/coach` - 获取维修记录
- `POST /api/repair` - 添加维修记录
- `PUT /api/coach/student/{studentId}/requiredHours` - 设置学员要求学时

### 消息接口
- `GET /api/messages` - 获取消息列表（支持分页）
- `POST /api/messages/read` - 标记消息为已读
- `DELETE /api/messages` - 删除消息
- `GET /api/messages/{id}` - 获取消息详情

### 管理员接口
- `GET /api/student/list` - 获取学员列表
- `PUT /api/student/{id}` - 更新学员信息
- `DELETE /api/student/{id}` - 删除学员
- `GET /api/coach/all` - 获取所有教练
- `GET /api/coach/list` - 获取教练列表（分页）
- `POST /api/coach` - 添加教练
- `PUT /api/coach/{id}` - 更新教练信息
- `DELETE /api/coach/{id}` - 删除教练
- `GET /api/vehicle/list` - 获取车辆列表（分页）
- `GET /api/vehicle/all` - 获取所有车辆
- `POST /api/vehicle` - 添加车辆
- `PUT /api/vehicle/{id}` - 更新车辆信息
- `DELETE /api/vehicle/{id}` - 删除车辆
- `GET /api/maintenance/all` - 获取所有保养记录
- `GET /api/repair/all` - 获取所有维修记录
- `GET /api/stats/dashboard` - 获取仪表盘统计数据
- `GET /api/config` - 获取系统配置
- `PUT /api/config` - 更新系统配置
- `PUT /api/config/{key}` - 更新单个配置项

## 系统配置项

系统支持以下配置项（通过系统配置页面管理）：

| 配置键 | 默认值 | 说明 |
|--------|--------|------|
| open_days | 7 | 开放预约天数 |
| daily_limit | 1 | 单人每日预约上限 |
| cancel_deadline | 2 | 取消截止时间（小时） |
| confirm_timeout | 30 | 确认超时时间（分钟） |
| system_notice | - | 系统公告 |
| min_appointment_hours | 1 | 最小预约时长 |
| max_appointment_hours | 4 | 最大预约时长 |
| slot_duration | 30 | 时间槽时长（分钟） |
| enable_sms_notify | false | 启用短信通知 |
| enable_email_notify | false | 启用邮件通知 |
| working_start_time | 08:00 | 工作时间开始 |
| working_end_time | 18:00 | 工作时间结束 |
| default_required_hours | 12 | 默认要求学时 |

## 预约状态流转

预约记录有以下状态：

| 状态值 | 状态名 | 说明 |
|--------|--------|------|
| 0 | 待确认 | 学员提交预约，等待教练确认 |
| 1 | 已确认 | 教练已确认预约 |
| 2 | 已完成 | 练车已完成 |
| 3 | 已取消 | 预约已取消 |
| 4 | 爽约 | 学员未按时到场 |
| 5 | 已拒绝 | 教练拒绝预约 |

状态流转图：
```
学员预约 → 待确认(0) ──确认──→ 已确认(1) ──完成──→ 已完成(2)
              │                  │
              └──拒绝──→ 已拒绝(5)   └──爽约──→ 爽约(4)
              
学员取消 → 已取消(3)

已完成(2)、已取消(3)、爽约(4)、已拒绝(5) ──撤销──→ 已确认(1)
```

## 安全特性

- 基于 Spring Security + JWT 的认证授权机制
- 密码加密存储
- 接口权限控制，不同角色访问不同接口
- CORS跨域配置
- 验证码验证防止暴力破解

## 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok简化POJO代码
- 统一使用RESTful API设计风格
- 前端使用Vue 3 Composition API

### 提交规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 重构
- test: 测试相关

## 贡献指南

欢迎提交 Pull Request 参与项目开发。提交前请确保：
1. 代码符合项目规范
2. 通过所有测试用例
3. 提交信息清晰明确

## 许可证

本项目仅供学习和研究使用。

## 联系方式

如有问题或建议，欢迎提交 Issue。
