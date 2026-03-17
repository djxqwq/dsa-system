# DSA System - 驾校练车预约数字化管理系统

## 项目简介

DSA System（Driving School Appointment System）是一个全栈开发的驾校练车预约数字化管理系统，旨在为驾校提供高效、便捷的学员练车预约、教练排班管理、车辆管理等一体化解决方案。系统采用前后端分离架构，支持学员、教练、管理员三种角色，实现了驾校日常运营管理的数字化和智能化。

## 功能特性

### 学员端功能
- **个人信息管理**：学员可查看和修改个人资料，包括联系方式、车型选择等
- **练车预约**：查看教练可用时段，在线预约练车时间
- **学时查询**：实时查看已完成的练车学时统计
- **预约历史**：查看历史预约记录，包括预约状态、时间、教练等信息

### 教练端功能
- **个人信息管理**：教练可维护个人资料和教学信息
- **排班管理**：设置可预约的时间段，管理个人日程
- **预约确认**：审核学员的预约请求，确认或拒绝预约
- **学时记录**：记录学员的练车时长和训练内容
- **车辆管理**：查看和管理分配的教练车信息

### 管理员端功能
- **用户管理**：管理学员账户，包括新增、编辑、禁用等操作
- **教练管理**：管理教练信息，分配车辆，设置权限
- **车辆管理**：管理教练车信息，包括车辆档案、维修保养记录
- **数据统计分析**：查看预约统计、学时统计等运营数据
- **系统配置**：系统参数设置，如预约规则、时段设置等

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
│   │   │   ├── coach/           # 教练页面
│   │   │   └── student/         # 学员页面
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
- **admin**：管理员信息表
- **coach**：教练信息表
- **student**：学员信息表
- **vehicle**：车辆信息表
- **schedule**：教练排班表
- **appointment**：预约记录表
- **maintenance_record**：保养记录表
- **repair_record**：维修记录表

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
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/captcha` - 获取验证码

### 学员接口
- `GET /api/student/profile` - 获取个人信息
- `PUT /api/student/profile` - 更新个人信息
- `GET /api/appointments/available` - 获取可预约时段
- `POST /api/appointments` - 创建预约
- `GET /api/appointments/history` - 获取预约历史

### 教练接口
- `GET /api/coach/profile` - 获取个人信息
- `PUT /api/coach/profile` - 更新个人信息
- `GET /api/schedule` - 获取排班列表
- `POST /api/schedule` - 创建排班
- `GET /api/appointments/pending` - 获取待确认预约
- `PUT /api/appointments/{id}/confirm` - 确认预约

### 管理员接口
- `GET /api/admin/users` - 获取用户列表
- `GET /api/admin/coaches` - 获取教练列表
- `GET /api/admin/vehicles` - 获取车辆列表
- `GET /api/admin/stats` - 获取统计数据

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
