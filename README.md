# 记忆花园 (Memory Garden)

基于艾宾浩斯遗忘曲线的游戏化间隔重复学习系统。每学习一条新知识，就在虚拟花园中种下一颗种子；通过持续复习，植物从种子逐步生长到结果，直观反映知识掌握程度。

---

## 目录

- [项目简介](#项目简介)
- [核心特性](#核心特性)
- [技术架构](#技术架构)
- [项目结构](#项目结构)
- [安装指南](#安装指南)
- [使用方法](#使用方法)
- [构建方法](#构建方法)
- [API 接口说明](#api-接口说明)
- [数据库设计](#数据库设计)
- [核心算法](#核心算法)
- [常见问题](#常见问题)

---

## 项目简介

**记忆花园** 是一个将间隔重复学习法与虚拟花园养成相结合的 Web 应用。用户创建知识卡片后，系统自动生成对应的虚拟植物；按照艾宾浩斯遗忘曲线规划复习时间，每次复习推动植物生长——从种子、发芽、成长、开花到结果。遗忘则导致植物枯萎，复习可使其复活。

系统采用前后端分离架构：
- **后端**：Spring Boot 2.7 + MyBatis + MySQL 8.0 + JWT 认证
- **前端**：Vue 3 + TypeScript + Vite + Element Plus + ECharts

---

## 核心特性

### 知识管理
- **知识卡片**：手动创建正面（问题）/ 背面（答案）格式的知识卡片
- **预设知识库**：系统内置知识包（Java 基础、数据结构、英语词汇等），一键导入
- **自定义分类**：创建分类组织知识，支持图标和排序
- **重复导入拦截**：同一知识包不可重复导入

### 花园系统
- **5 阶段生长**：种子 → 发芽 → 成长 → 开花 → 结果
- **枯萎机制**：超过推荐复习时间 3 倍未复习，植物枯萎；复习即可复活
- **筛选排序**：按分类、阶段、枯萎状态筛选；按创建时间、复习时间、生长阶段排序
- **可视化**：每个阶段对应独立 SVG 图标，枯萎植物有专属样式

### 复习系统
- **艾宾浩斯间隔**：1→2→4→7→15→30 天自动规划复习
- **三档自评**：记住了 / 模糊 / 忘记了，分别对应推进、维持、回退
- **智能重算**：错过复习后按实际逾期天数重新计算等效轮次
- **复习总结**：完成当日复习后展示统计总结

### 成就系统
- **8 枚徽章**：涵盖种植、打卡、开花、结果、复活、分类等维度
- **三级稀有度**：普通 / 稀有 / 史诗
- **自动解锁**：满足条件后系统自动授予

### 统计面板
- **今日数据**：新增卡片数、复习次数、退化次数
- **连续打卡**：当前连续天数、历史最长连续天数
- **趋势图表**：近 N 天复习趋势折线图
- **阶段分布**：各生长阶段植物数量饼图

---

## 技术架构

```
┌─────────────────────────────────────────────────────┐
│                    前端 (Vue 3)                       │
│  Vue 3 + TypeScript + Vite + Pinia + Element Plus    │
│  ECharts · Vue Router · Axios · SCSS                 │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP / Vite Proxy (/api)
┌──────────────────────▼──────────────────────────────┐
│                  后端 (Spring Boot)                   │
│  Spring Boot 2.7 · MyBatis · Spring Security Crypto  │
│  JWT (java-jwt) · Knife4j · AOP · Validation        │
└──────────────────────┬──────────────────────────────┘
                       │ JDBC
┌──────────────────────▼──────────────────────────────┐
│                   MySQL 8.0                          │
│              memory_garden 数据库                     │
└─────────────────────────────────────────────────────┘
```

| 层级 | 技术选型 | 版本 |
|------|---------|------|
| 前端框架 | Vue 3 | ^3.3 |
| 类型系统 | TypeScript | ^5.3 |
| 构建工具 | Vite | ^4.5 |
| 状态管理 | Pinia | ^2.1 |
| UI 组件库 | Element Plus | ^2.4 |
| 图表库 | ECharts + vue-echarts | ^5.4 / ^6.6 |
| HTTP 客户端 | Axios | ^1.6 |
| 后端框架 | Spring Boot | 2.7.18 |
| ORM | MyBatis | 2.3.2 (starter) |
| 数据库 | MySQL | 8.0 |
| 认证 | JWT (auth0 java-jwt) | 3.19.4 |
| 密码加密 | Spring Security Crypto (BCrypt) | — |
| API 文档 | Knife4j (Swagger) | 3.0.3 |
| Java 版本 | JDK | 1.8 |

---

## 项目结构

```
Memory Garden/
├── memory-garden-server/                # 后端服务
│   ├── src/main/java/com/memorygarden/
│   │   ├── algorithm/                   # 核心算法
│   │   │   ├── EbbinghausCalculator     # 艾宾浩斯间隔计算
│   │   │   ├── GrowthStageCalculator    # 生长阶段计算
│   │   │   ├── WitherCalculator         # 枯萎判定
│   │   │   └── BadgeEvaluator           # 徽章条件评估
│   │   ├── common/                      # 通用组件
│   │   │   ├── constant/                # 常量定义
│   │   │   ├── exception/               # 异常处理
│   │   │   ├── result/                  # 统一返回结构
│   │   │   └── util/                    # JWT 工具类
│   │   ├── config/                      # 配置类
│   │   ├── controller/                  # 控制器（8 个模块）
│   │   ├── interceptor/                 # 认证拦截器
│   │   ├── mapper/                      # MyBatis Mapper
│   │   ├── model/
│   │   │   ├── dto/                     # 请求 DTO
│   │   │   ├── entity/                  # 数据实体
│   │   │   ├── enums/                   # 枚举类型
│   │   │   └── vo/                      # 视图对象
│   │   ├── service/                     # 业务接口 + 实现
│   │   └── task/                        # 定时任务（枯萎检查）
│   ├── src/main/resources/
│   │   ├── db/memory_garden.sql         # 数据库初始化脚本
│   │   ├── mapper/*.xml                 # MyBatis XML 映射
│   │   ├── application.yml              # 主配置
│   │   ├── application-dev.yml          # 开发环境配置
│   │   └── application-prod.yml         # 生产环境配置
│   └── pom.xml
│
├── memory-garden-web/                   # 前端应用
│   ├── src/
│   │   ├── api/                         # API 请求模块
│   │   ├── assets/
│   │   │   ├── images/plants/           # 植物 SVG 图标（6 个阶段）
│   │   │   └── styles/global.scss       # 全局样式 + 设计系统
│   │   ├── components/                  # 公共组件
│   │   ├── router/index.ts              # 路由配置 + 守卫
│   │   ├── stores/                      # Pinia 状态管理
│   │   ├── views/                       # 页面视图（13 个）
│   │   ├── App.vue
│   │   └── main.ts
│   ├── vite.config.ts                   # Vite 配置（含代理）
│   └── package.json
│
├── deploy/                             # 部署包（完整部署资源）
│   ├── backend/                        # 后端可执行 JAR
│   ├── frontend/                       # 前端静态资源
│   ├── sql/                            # 数据库初始化脚本
│   ├── config/                         # 生产环境配置 + Nginx 模板
│   ├── docker/                         # Docker 部署文件
│   │   ├── Dockerfile                  # 多阶段构建镜像
│   │   ├── docker-compose.yml          # 容器编排（含 MySQL）
│   │   └── .dockerignore
│   ├── scripts/                        # 构建脚本
│   │   ├── build.sh                    # Linux/macOS
│   │   └── build.ps1                   # Windows
│   ├── start.sh                        # 启动脚本 (Linux/macOS)
│   ├── start.bat                       # 启动脚本 (Windows)
│   └── .env.example                    # 环境变量模板
└── AGENTS.md                           # 代码规范
```

---

## 安装指南

### 环境要求

| 依赖 | 最低版本 | 说明 |
|------|---------|------|
| JDK | 1.8 | 后端运行环境 |
| Maven | 3.6+ | 后端构建工具 |
| Node.js | 16+ | 前端运行环境 |
| npm | 8+ | 前端包管理 |
| MySQL | 8.0+ | 数据库 |
| Docker | 20.10+ | 容器化部署（可选） |
| Docker Compose | 2.0+ | 容器编排（可选） |

### 方式一：Docker Compose 部署（推荐）

```bash
# 1. 克隆项目
git clone <repository-url>
cd Memory\ Garden

# 2. 创建环境变量文件
cp deploy/.env.example deploy/.env
# 编辑 deploy/.env，填入真实的 JWT_SECRET 和 DB_PASSWORD

# 3. 一键启动
cd deploy/docker
docker compose --env-file ../.env up -d

# 4. 查看日志
docker compose logs -f app
```

启动后访问：
- 前端页面：`http://localhost:8080`
- API 文档：`http://localhost:8080/doc.html`（需设置 `SWAGGER_ENABLE=true`）

### 方式二：手动部署

#### 1. 数据库初始化

```bash
mysql -u root -p

CREATE DATABASE memory_garden DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE memory_garden;
SOURCE memory-garden-server/src/main/resources/db/memory_garden.sql;
```

#### 2. 后端启动

```bash
cd memory-garden-server

# 设置环境变量（必须）
export JWT_SECRET="your-strong-random-secret-at-least-32-chars"
export DB_PASSWORD="your_db_password"
export DB_URL="jdbc:mysql://localhost:3306/memory_garden?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true"
export DB_USERNAME="root"

# 构建并启动
mvn clean package -DskipTests
java -Dspring.profiles.active=prod -jar target/memory-garden-server-1.0.0.jar
```

后端默认运行在 `http://localhost:8080`。

#### 3. 前端部署

```bash
cd memory-garden-web
npm ci
npx vite build
# 产物输出到 dist/ 目录，部署到 Nginx 等 Web 服务器
```

Nginx 配置示例：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # API 反向代理到后端
    location /api/ {
        proxy_pass http://127.0.0.1:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

#### 4. 开发模式

```bash
# 后端
cd memory-garden-server
mvn spring-boot:run

# 前端（自动代理 /api 到后端 8080）
cd memory-garden-web
npm install
npm run dev
```

前端开发服务器运行在 `http://localhost:5173`。

---

## 使用方法

### 命令行参数与脚本

#### 构建脚本

```bash
# Linux/macOS
deploy/scripts/build.sh          # 完整构建（清理 + 前端 + 后端 + 打包到 deploy/）

# Windows PowerShell
deploy\scripts\build.ps1         # 完整构建
```

构建产物输出到 `deploy/` 目录：

```
deploy/
├── backend/
│   └── memory-garden-server-1.0.0.jar   # 后端可执行 JAR
├── frontend/                             # 前端静态资源
├── sql/memory_garden.sql                 # 数据库初始化脚本
├── config/                               # 生产配置 + Nginx 模板
├── .env.example                          # 环境变量模板
├── start.sh / start.bat                  # 启动脚本
└── docker/                               # Docker 部署文件
```

#### 环境变量

| 变量名 | 必需 | 默认值 | 说明 |
|--------|------|--------|------|
| `JWT_SECRET` | 是 | 无 | JWT 签名密钥，至少 32 字符强随机字符串 |
| `DB_PASSWORD` | 是 | 无 | MySQL 数据库密码 |
| `DB_URL` | 否 | `jdbc:mysql://localhost:3306/memory_garden?...` | 数据库连接 URL |
| `DB_USERNAME` | 否 | `root` | 数据库用户名 |
| `SERVER_PORT` | 否 | `8080` | 后端服务端口 |
| `SWAGGER_ENABLE` | 否 | `false` | 是否启用 Swagger API 文档 |

#### JAR 运行参数

```bash
# 基本启动
java -jar memory-garden-server-1.0.0.jar

# 指定生产环境配置
java -Dspring.profiles.active=prod -jar memory-garden-server-1.0.0.jar

# 通过 JVM 参数注入环境变量
java -DJWT_SECRET="your-secret" -DDB_PASSWORD="your-password" \
     -Dspring.profiles.active=prod \
     -jar memory-garden-server-1.0.0.jar

# 指定外部配置文件
java -Dspring.config.additional-location=file:/app/config/ \
     -Dspring.profiles.active=prod \
     -jar memory-garden-server-1.0.0.jar

# 自定义端口
java -Dserver.port=9090 -jar memory-garden-server-1.0.0.jar

# 调整 JVM 内存
java -Xms256m -Xmx512m -jar memory-garden-server-1.0.0.jar
```

#### Docker 命令

```bash
# 构建镜像（从项目根目录执行）
docker build -t memory-garden:latest -f deploy/docker/Dockerfile .

# 运行容器（连接外部 MySQL）
docker run -d \
    --name memory-garden \
    -p 8080:8080 \
    -e JWT_SECRET="your-strong-random-secret" \
    -e DB_PASSWORD="your_db_password" \
    -e DB_URL="jdbc:mysql://host.docker.internal:3306/memory_garden?..." \
    memory-garden:latest

# Docker Compose 启动（含 MySQL）
cd deploy/docker
docker compose --env-file ../.env up -d

# 查看日志
docker compose logs -f app

# 停止服务
docker compose down

# 停止并清除数据卷
docker compose down -v
```

### 用户操作指南

#### 注册与登录

1. 访问系统，进入注册页面
2. 填写用户名、密码、昵称完成注册
3. 使用用户名和密码登录，获取 Token
4. Token 自动存储在 localStorage，后续请求自动携带

#### 创建知识卡片

1. 点击导航栏「新建知识」
2. 填写正面内容（问题）和背面内容（答案）
3. 选择分类（可选）
4. 提交后花园中自动生成一颗种子

#### 导入预设知识包

1. 进入「知识库」页面
2. 浏览可用知识包列表
3. 点击「导入」一键批量生成卡片和种子
4. 同一知识包不可重复导入

#### 复习流程

1. 登录后首页展示花园，导航栏显示待复习数量
2. 点击「开始复习」进入复习页面
3. 查看正面内容 → 点击查看答案 → 选择自评
   - **记住了**：推进到下一生长阶段
   - **模糊**：维持当前阶段，缩短复习间隔
   - **忘记了**：回退一个生长阶段
4. 完成所有待复习后展示总结页

#### 花园管理

- 首页默认展示花园全景
- 按分类、阶段、枯萎状态筛选植物
- 按创建时间、复习时间、生长阶段排序
- 枯萎植物有专属列表和标识

#### 查看成就与统计

- 「徽章」页面查看所有徽章及获得状态
- 「统计」页面查看今日数据、趋势图、阶段分布、连续打卡

---

## 构建方法

### 从源码构建

#### 前置条件

- JDK 1.8+
- Maven 3.6+
- Node.js 16+
- npm 8+

#### 一键构建

```bash
# Linux/macOS
deploy/scripts/build.sh

# Windows
deploy\scripts\build.ps1
```

产物输出到 `deploy/` 目录。

#### 分步构建

```bash
# 1. 前端构建
cd memory-garden-web
npm ci
npx vite build    # 产物: memory-garden-web/dist/

# 2. 后端构建
cd memory-garden-server
mvn clean package -DskipTests    # 产物: target/memory-garden-server-1.0.0.jar
```

### Docker 镜像构建

```bash
# 构建镜像（从项目根目录执行，指定 Dockerfile 路径）
docker build -t memory-garden:latest -f deploy/docker/Dockerfile .

# 查看镜像大小
docker images memory-garden

# 推送到镜像仓库
docker tag memory-garden:latest your-registry/memory-garden:latest
docker push your-registry/memory-garden:latest
```

Docker 镜像特性：
- **多阶段构建**：前端 Node.js 构建 → 后端 Maven 构建 → 仅 JRE 运行时
- **非 root 用户**：应用以 `appuser` 用户运行
- **无源码泄露**：最终镜像不含任何源代码或构建工具
- **依赖缓存**：前端/后端依赖层分离，仅依赖变化时重新安装
- **健康检查**：内置 Docker HEALTHCHECK
- **配置外置**：支持通过环境变量和外部配置文件覆盖

---

## API 接口说明

### 通用规范

**Base URL**: `/api`

**认证方式**: 请求头 `Authorization: {token}`（登录接口返回的 JWT Token）

**统一返回结构**:

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

**错误码**:

| 错误码 | 含义 |
|--------|------|
| 0 | 成功 |
| 40000 | 请求参数错误 |
| 40100 | 未登录 |
| 40101 | 无权限 |
| 40300 | 禁止访问 |
| 40400 | 数据不存在 |
| 50000 | 系统内部异常 |
| 50001 | 操作失败 |

---

### 用户模块 `/api/user`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/register` | 用户注册 | 否 |
| POST | `/login` | 用户登录 | 否 |
| GET | `/current` | 获取当前用户信息 | 是 |
| PUT | `/profile` | 修改个人信息 | 是 |

**注册请求体**:
```json
{
  "username": "string",
  "password": "string",
  "nickname": "string"
}
```

**登录请求体**:
```json
{
  "username": "string",
  "password": "string"
}
```

**登录返回**: `data` 为 JWT Token 字符串

**修改个人信息请求体**:
```json
{
  "nickname": "string",
  "avatarUrl": "string"
}
```

---

### 分类模块 `/api/category`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/` | 创建分类 | 是 |
| GET | `/list` | 获取分类列表 | 是 |
| PUT | `/{id}` | 修改分类 | 是 |
| DELETE | `/{id}` | 删除分类（软删除） | 是 |

---

### 知识卡片模块 `/api/card`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/` | 创建知识卡片 | 是 |
| GET | `/{id}` | 获取卡片详情 | 是 |
| GET | `/list` | 获取卡片列表 | 是 |
| PUT | `/{id}` | 修改知识卡片 | 是 |
| DELETE | `/{id}` | 删除知识卡片（软删除） | 是 |

**列表查询参数**: `categoryId`（可选，按分类筛选）

---

### 花园模块 `/api/garden`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/` | 获取花园视图数据 | 是 |
| GET | `/filter` | 按条件筛选植物 | 是 |
| GET | `/sort` | 按条件排序植物 | 是 |
| GET | `/withered` | 获取枯萎植物列表 | 是 |

**筛选参数**: `categoryId`（可选）、`stage`（可选，1-5）、`withered`（可选，布尔）

**排序参数**: `sortBy`（createTime / nextReviewDate / growthStage）、`order`（asc / desc）

---

### 复习模块 `/api/review`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/pending` | 获取待复习列表 | 是 |
| GET | `/next` | 获取下一个待复习 | 是 |
| POST | `/submit` | 提交复习自评结果 | 是 |
| GET | `/summary` | 获取今日复习总结 | 是 |
| GET | `/pending-count` | 获取待复习数量 | 是 |

**复习提交请求体**:
```json
{
  "cardId": 0,
  "evaluation": "REMEMBERED"
}
```

`evaluation` 取值: `REMEMBERED` / `VAGUE` / `FORGOTTEN`

---

### 徽章模块 `/api/badge`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/list` | 获取所有徽章（含获得状态） | 是 |
| GET | `/my` | 获取已获得徽章 | 是 |

---

### 统计模块 `/api/stats`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/today` | 获取今日学习数据 | 是 |
| GET | `/trend` | 获取趋势数据 | 是 |
| GET | `/streak` | 获取连续打卡天数 | 是 |
| GET | `/stage-distribution` | 获取各阶段植物分布 | 是 |

**趋势参数**: `days`（默认 7）

---

### 预设知识包模块 `/api/study-pack`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/list` | 获取知识包列表 | 否 |
| GET | `/{id}` | 获取知识包详情 | 否 |
| GET | `/{id}/items` | 获取知识包条目列表 | 否 |
| POST | `/{id}/import` | 导入知识包到个人花园 | 是 |

---

## 数据库设计

### 表结构总览

| 表名 | 说明 | 核心字段 |
|------|------|---------|
| `t_user` | 用户表 | id, username, password(BCrypt), nickname, avatar_url, current_streak, max_streak |
| `t_category` | 分类表 | id, user_id, name, icon, sort_order |
| `t_knowledge_card` | 知识卡片表 | id, user_id, category_id, front_content, back_content, note, source_type, source_pack_id |
| `t_plant` | 植物表 | id, card_id, user_id, growth_stage(1-5), review_round, total_review_count, is_withered, next_review_date, last_review_date, stage_success_count |
| `t_review_record` | 复习记录表 | id, user_id, card_id, plant_id, prev_stage, after_stage, evaluation, review_date |
| `t_badge` | 徽章定义表 | id, name, description, icon, rarity(0-2), condition_type, condition_value |
| `t_user_badge` | 用户徽章关联表 | id, user_id, badge_id |
| `t_study_pack` | 知识包表 | id, name, description, card_count |
| `t_study_pack_item` | 知识包条目表 | id, pack_id, front_content, back_content |
| `t_study_pack_import` | 知识包导入记录表 | id, user_id, pack_id |

### 软删除策略

所有业务表均使用 `is_deleted` 字段（0=未删除，1=已删除）实现软删除，禁止物理删除数据。

### 用户数据隔离

所有业务查询均以 `user_id` 为过滤条件，确保不同用户间数据不可见。

---

## 核心算法

### 艾宾浩斯复习间隔

```
轮次 1 → 间隔 1 天
轮次 2 → 间隔 2 天
轮次 3 → 间隔 4 天
轮次 4 → 间隔 7 天
轮次 5 → 间隔 15 天
轮次 6+ → 间隔 30 天
```

### 生长阶段规则

| 自评 | 效果 |
|------|------|
| 记住了 (REMEMBERED) | stage_success_count++；达到阈值则推进到下一阶段 |
| 模糊 (VAGUE) | 维持当前阶段，stage_success_count 重置 |
| 忘记了 (FORGOTTEN) | 回退一个阶段（最低为1），stage_success_count 重置 |

### 枯萎判定

- 条件：当前日期 > next_review_date + 3 × 当前轮次间隔天数
- 定时任务：`WitherCheckTask` 定期扫描并标记枯萎
- 复活：进行一次复习即可恢复到前一生长阶段

### 智能重算

当用户逾期后才复习时，`EbbinghausCalculator.calcEffectiveRound()` 根据逾期天数从当前轮次逐级回退，每级扣除对应间隔天数，计算等效轮次。

### 徽章评估

`BadgeEvaluator` 在关键操作后（创建卡片、提交复习、创建分类等）触发，检查所有未获得徽章的达成条件并自动授予。

---

## 常见问题

### Q: 登录返回"用户名或密码错误"

确认用户名和密码正确。系统对用户名不存在和密码错误返回相同提示，防止用户名枚举攻击。

### Q: 接口返回 40100 未登录

Token 过期或未携带。检查 localStorage 中的 token 是否存在，请求头 `Authorization` 是否正确设置。

### Q: 知识包导入返回"该知识包已导入"

同一知识包对同一用户只能导入一次。如需重新导入，需在数据库 `t_study_pack_import` 表中删除对应记录。

### Q: Docker 容器启动失败

1. 检查 `deploy/.env` 文件是否已创建并填入必需的环境变量（`JWT_SECRET`、`DB_PASSWORD`）
2. 检查 MySQL 容器是否健康：`docker compose ps`
3. 查看应用日志：`docker compose logs app`

### Q: 前端代理 404

确认后端已启动在 8080 端口，Vite 代理配置中 `target` 指向正确地址。生产环境需在 Nginx 中配置 `/api` 反向代理。

### Q: 数据库连接失败

1. 确认 MySQL 服务已启动
2. 确认 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 环境变量正确
3. Docker 环境中数据库主机名使用 `mysql`（容器名），而非 `localhost`
