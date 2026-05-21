# 记忆花园 (Memory Garden)

基于艾宾浩斯遗忘曲线的游戏化学习复习系统。每学习一条新知识，就在虚拟花园中种下一颗种子；通过持续复习，植物从种子逐步生长到结果，直观反映知识掌握程度。

---

## 目录

- [功能特性](#功能特性)
- [技术架构](#技术架构)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [安装部署](#安装部署)
- [使用指南](#使用指南)
- [API 接口说明](#api-接口说明)
- [数据库设计](#数据库设计)
- [核心算法](#核心算法)
- [常见问题](#常见问题)
- [维护注意事项](#维护注意事项)

---

## 功能特性

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
│   │   │   └── result/                  # 统一返回结构
│   │   ├── config/                      # 配置类
│   │   │   ├── CorsConfig               # 跨域配置
│   │   │   ├── SecurityConfig           # 安全配置
│   │   │   ├── SwaggerConfig            # API 文档配置
│   │   │   └── WebMvcConfig             # 拦截器注册
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
│   │   └── application-dev.yml          # 开发环境配置
│   └── pom.xml
│
├── memory-garden-web/                   # 前端应用
│   ├── src/
│   │   ├── api/                         # API 请求模块
│   │   │   ├── request.ts               # Axios 封装（拦截器）
│   │   │   ├── user.ts                  # 用户接口
│   │   │   ├── card.ts                  # 卡片接口
│   │   │   ├── category.ts              # 分类接口
│   │   │   ├── garden.ts                # 花园接口
│   │   │   ├── review.ts                # 复习接口
│   │   │   ├── badge.ts                 # 徽章接口
│   │   │   ├── stats.ts                 # 统计接口
│   │   │   └── studyPack.ts             # 知识包接口
│   │   ├── assets/
│   │   │   ├── images/plants/           # 植物 SVG 图标（6 个阶段）
│   │   │   └── styles/global.scss       # 全局样式
│   │   ├── components/                  # 公共组件
│   │   │   ├── MainLayout.vue           # 主布局
│   │   │   ├── Navbar.vue               # 导航栏
│   │   │   ├── PlantCard.vue            # 植物卡片
│   │   │   ├── PlantStageIcon.vue       # 阶段图标
│   │   │   ├── ReviewCard.vue           # 复习卡片
│   │   │   ├── BadgeItem.vue            # 徽章项
│   │   │   ├── CategoryTag.vue          # 分类标签
│   │   │   ├── StatsChart.vue           # 统计图表
│   │   │   └── StreakCalendar.vue       # 打卡日历
│   │   ├── router/index.ts              # 路由配置 + 守卫
│   │   ├── stores/                      # Pinia 状态管理
│   │   │   ├── user.ts                  # 用户状态
│   │   │   ├── garden.ts                # 花园状态
│   │   │   └── review.ts                # 复习状态
│   │   ├── views/                       # 页面视图（13 个）
│   │   ├── App.vue
│   │   └── main.ts
│   ├── vite.config.ts                   # Vite 配置（含代理）
│   └── package.json
│
├── specs/spec.md                        # 产品需求规格说明书
└── AGENTS.md                            # 代码规范
```

---

## 环境要求

| 依赖 | 最低版本 | 说明 |
|------|---------|------|
| JDK | 1.8 | 后端运行环境 |
| Maven | 3.6+ | 后端构建工具 |
| Node.js | 16+ | 前端运行环境 |
| npm | 8+ | 前端包管理 |
| MySQL | 8.0+ | 数据库 |

---

## 安装部署

### 1. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE memory_garden DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本（含表结构和基础数据：徽章、知识包）
mysql -u root -p memory_garden < memory-garden-server/src/main/resources/db/memory_garden.sql
```

### 2. 后端启动

```bash
cd memory-garden-server

# 修改数据库连接配置（如需）
# 编辑 src/main/resources/application-dev.yml
# spring.datasource.url / username / password

# 构建并启动
mvn clean package -DskipTests
java -jar target/memory-garden-server-1.0.0.jar

# 或开发模式启动
mvn spring-boot:run
```

后端默认运行在 `http://localhost:8080`。

### 3. 前端启动

```bash
cd memory-garden-web

# 安装依赖
npm install

# 开发模式启动（自动代理 /api 到后端 8080）
npm run dev
```

前端默认运行在 `http://localhost:5173`，Vite 开发服务器自动将 `/api` 请求代理到后端。

### 4. 生产构建

```bash
# 前端构建
cd memory-garden-web
npm run build
# 产物输出到 dist/ 目录，可部署到 Nginx 等 Web 服务器

# 后端构建
cd memory-garden-server
mvn clean package -DskipTests
# 产物为 target/memory-garden-server-1.0.0.jar
```

### 5. API 文档

启动后端后访问 Knife4j 文档：`http://localhost:8080/doc.html`

---

## 使用指南

### 注册与登录

1. 访问系统，进入注册页面
2. 填写用户名、密码、昵称完成注册
3. 使用用户名和密码登录，获取 Token
4. Token 自动存储在 localStorage，后续请求自动携带

### 创建知识卡片

1. 点击导航栏「新建知识」
2. 填写正面内容（问题）和背面内容（答案）
3. 选择分类（可选）
4. 提交后花园中自动生成一颗种子

### 导入预设知识包

1. 进入「知识库」页面
2. 浏览可用知识包列表
3. 点击「导入」一键批量生成卡片和种子
4. 同一知识包不可重复导入

### 复习流程

1. 登录后首页展示花园，导航栏显示待复习数量
2. 点击「开始复习」进入复习页面
3. 查看正面内容 → 点击查看答案 → 选择自评
   - **记住了**：推进到下一生长阶段
   - **模糊**：维持当前阶段，缩短复习间隔
   - **忘记了**：回退一个生长阶段
4. 完成所有待复习后展示总结页

### 花园管理

- 首页默认展示花园全景
- 按分类、阶段、枯萎状态筛选植物
- 按创建时间、复习时间、生长阶段排序
- 枯萎植物有专属列表和标识

### 查看成就与统计

- 「徽章」页面查看所有徽章及获得状态
- 「统计」页面查看今日数据、趋势图、阶段分布、连续打卡

---

## API 接口说明

### 通用规范

**Base URL**: `/api`

**认证方式**: 请求头 `Authorization: {token}`（登录接口返回的 Token）

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

**登录返回**: `data` 为 Token 字符串，格式 `{userId}:{uuid}`

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

**分类创建/修改请求体**:
```json
{
  "name": "string",
  "icon": "string",
  "sortOrder": 0
}
```

---

### 知识卡片模块 `/api/card`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/` | 创建知识卡片 | 是 |
| GET | `/{id}` | 获取卡片详情 | 是 |
| GET | `/list` | 获取卡片列表 | 是 |
| PUT | `/{id}` | 修改知识卡片 | 是 |
| DELETE | `/{id}` | 删除知识卡片（软删除，同步删除关联植物） | 是 |

**卡片创建请求体**:
```json
{
  "frontContent": "string",
  "backContent": "string",
  "categoryId": 0,
  "note": "string"
}
```

**卡片修改请求体**:
```json
{
  "frontContent": "string",
  "backContent": "string",
  "categoryId": 0,
  "note": "string"
}
```

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

**花园视图返回数据**:
```json
{
  "totalCount": 25,
  "witheredCount": 2,
  "stageCount": {"1": 4, "2": 8, "3": 7, "4": 4, "5": 2},
  "plants": [...]
}
```

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

`evaluation` 取值: `REMEMBERED`（记住了）/ `VAGUE`（模糊）/ `FORGOTTEN`（忘记了）

**复习总结返回数据**:
```json
{
  "totalCount": 16,
  "rememberedCount": 12,
  "vagueCount": 2,
  "forgottenCount": 2
}
```

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

**今日数据返回**:
```json
{
  "todayNewCards": 0,
  "todayReviewCount": 16,
  "todayDegradedCount": 2,
  "currentStreak": 15,
  "maxStreak": 15,
  "totalCardCount": 25,
  "totalPlantCount": 25,
  "stageDistribution": {"1": 4, "2": 8, "3": 7, "4": 4, "5": 2},
  "trendData": {"2026-05-14": 5, "2026-05-15": 8, ...}
}
```

---

### 预设知识包模块 `/api/study-pack`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/list` | 获取知识包列表 | 否 |
| GET | `/{id}` | 获取知识包详情 | 否 |
| GET | `/{id}/items` | 获取知识包条目列表 | 否 |
| POST | `/{id}/import` | 导入知识包到个人花园 | 是 |

**导入返回**: `data` 为导入的卡片数量（整数）

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

### Q: 植物状态与预期不符

复习自评会实时更新植物状态。可通过统计接口 `/api/stats/today` 查看今日退化次数，通过花园接口 `/api/garden` 查看当前阶段分布。

### Q: 前端代理 404

确认后端已启动在 8080 端口，Vite 代理配置中 `target` 指向正确地址。生产环境需在 Nginx 中配置 `/api` 反向代理。

### Q: 数据库连接失败

检查 `application-dev.yml` 中的数据库地址、用户名、密码是否正确，MySQL 服务是否启动，数据库 `memory_garden` 是否已创建。

---

## 维护注意事项

### 数据安全

- **禁止物理删除**：所有删除操作均为软删除（`is_deleted=1`），不可执行 `DELETE FROM` 语句
- **数据隔离**：运维操作必须带 `user_id` 条件，避免影响其他用户
- **变更前备份**：修改生产数据前务必使用 `mysqldump` 备份相关表

### 定时任务

- `WitherCheckTask` 定期扫描枯萎植物，确保 Spring `@Scheduled` 正常运行
- 如需暂停枯萎检查，可在配置中禁用或注释 `@Scheduled` 注解

### 日志

- 开发环境日志级别为 `debug`（`application-dev.yml` 中配置）
- 生产环境建议调整为 `info` 或 `warn`
- MyBatis SQL 日志默认输出到控制台，生产环境应关闭（移除 `log-impl` 配置）

### 性能

- 所有查询均基于 `user_id` 索引，单用户数据量在千级别内无性能问题
- 如数据量增长，关注 `t_review_record` 表的清理策略（软删除历史记录）
- 花园视图接口会查询用户所有植物，超大数据量时考虑分页

### 扩展方向

- 复习算法模块化设计，可替换为更复杂的间隔重复算法（如 SM-2）
- 植物外观系统预留多皮肤接口，当前为统一 SVG 图标
- 知识卡片字段设计预留扩展空间（note 字段等）
