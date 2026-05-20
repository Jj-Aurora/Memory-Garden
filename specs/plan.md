# 记忆花园系统 - 技术方案与实施计划

> 版本：v1.0（MVP）
> 追踪文档：[specs/spec.md](./spec.md)
> 编码规范：[AGENTS.md](../AGENTS.md)
> 日期：2026-05-20

---

## 一、技术栈确认

| 层级 | 技术选型 | 版本 |
|------|----------|------|
| 后端框架 | Spring Boot | 2.7.x（兼容 Java 8） |
| 语言 | Java | 8 |
| ORM | MyBatis | 3.5.x |
| 数据库 | MySQL | 8.0.x |
| 前端框架 | Vue.js | 3.x |
| 前端语言 | TypeScript | 4.x+ |
| 构建工具 | Vite | 4.x+ |
| UI 组件库 | Element Plus | 2.x |
| API 文档 | Swagger / Knife4j | - |

---

## 二、项目结构

### 2.1 后端目录结构

```
memory-garden-server/
├── src/main/java/com/memorygarden/
│   ├── MemoryGardenApplication.java          # 启动类
│   ├── config/                               # 配置类
│   │   ├── CorsConfig.java                   # 跨域配置
│   │   ├── SwaggerConfig.java                # Swagger 配置
│   │   └── WebMvcConfig.java                 # MVC 配置（拦截器注册等）
│   ├── common/                               # 通用模块
│   │   ├── result/BaseResponse.java          # 统一返回结构
│   │   ├── result/ResultCode.java            # 返回码枚举
│   │   ├── exception/BusinessException.java  # 业务异常
│   │   ├── exception/GlobalExceptionHandler.java  # 全局异常处理
│   │   └── constant/Constant.java            # 常量定义
│   ├── model/                                # 数据模型
│   │   ├── entity/                           # 数据库实体
│   │   │   ├── User.java
│   │   │   ├── KnowledgeCard.java
│   │   │   ├── Category.java
│   │   │   ├── Plant.java
│   │   │   ├── ReviewRecord.java
│   │   │   ├── Badge.java
│   │   │   ├── UserBadge.java
│   │   │   ├── StudyPack.java
│   │   │   └── StudyPackItem.java
│   │   ├── dto/                              # 数据传输对象（请求）
│   │   │   ├── UserRegisterRequest.java
│   │   │   ├── UserLoginRequest.java
│   │   │   ├── CardCreateRequest.java
│   │   │   ├── CardUpdateRequest.java
│   │   │   ├── CategoryCreateRequest.java
│   │   │   ├── ReviewSubmitRequest.java
│   │   │   └── StudyPackImportRequest.java
│   │   └── vo/                               # 视图对象（响应）
│   │       ├── UserVO.java
│   │       ├── CardVO.java
│   │       ├── PlantVO.java
│   │       ├── GardenVO.java
│   │       ├── ReviewVO.java
│   │       ├── BadgeVO.java
│   │       ├── StatsVO.java
│   │       └── ReviewSummaryVO.java
│   ├── mapper/                               # MyBatis Mapper 接口
│   │   ├── UserMapper.java
│   │   ├── KnowledgeCardMapper.java
│   │   ├── CategoryMapper.java
│   │   ├── PlantMapper.java
│   │   ├── ReviewRecordMapper.java
│   │   ├── BadgeMapper.java
│   │   ├── UserBadgeMapper.java
│   │   ├── StudyPackMapper.java
│   │   └── StudyPackItemMapper.java
│   ├── service/                              # 业务逻辑接口
│   │   ├── UserService.java
│   │   ├── KnowledgeCardService.java
│   │   ├── CategoryService.java
│   │   ├── PlantService.java
│   │   ├── ReviewService.java
│   │   ├── BadgeService.java
│   │   ├── StatsService.java
│   │   └── StudyPackService.java
│   ├── service/impl/                         # 业务逻辑实现
│   │   ├── UserServiceImpl.java
│   │   ├── KnowledgeCardServiceImpl.java
│   │   ├── CategoryServiceImpl.java
│   │   ├── PlantServiceImpl.java
│   │   ├── ReviewServiceImpl.java
│   │   ├── BadgeServiceImpl.java
│   │   ├── StatsServiceImpl.java
│   │   └── StudyPackServiceImpl.java
│   ├── algorithm/                            # 核心算法模块
│   │   ├── EbbinghausCalculator.java         # 艾宾浩斯间隔计算
│   │   ├── GrowthStageCalculator.java        # 生长阶段推进计算
│   │   ├── WitherCalculator.java             # 枯萎判定计算
│   │   └── BadgeEvaluator.java               # 徽章达成判定
│   ├── controller/                           # 控制器
│   │   ├── UserController.java
│   │   ├── KnowledgeCardController.java
│   │   ├── CategoryController.java
│   │   ├── GardenController.java
│   │   ├── ReviewController.java
│   │   ├── BadgeController.java
│   │   ├── StatsController.java
│   │   └── StudyPackController.java
│   └── interceptor/                          # 拦截器
│       └── AuthInterceptor.java              # 登录鉴权拦截器
├── src/main/resources/
│   ├── application.yml                       # 主配置
│   ├── mapper/                               # MyBatis XML
│   │   ├── UserMapper.xml
│   │   ├── KnowledgeCardMapper.xml
│   │   ├── CategoryMapper.xml
│   │   ├── PlantMapper.xml
│   │   ├── ReviewRecordMapper.xml
│   │   ├── BadgeMapper.xml
│   │   ├── UserBadgeMapper.xml
│   │   ├── StudyPackMapper.xml
│   │   └── StudyPackItemMapper.xml
│   └── db/
│       └── schema.sql                        # MySQL 建表脚本
└── pom.xml
```

### 2.2 前端目录结构

```
memory-garden-web/
├── public/
│   └── favicon.ico
├── src/
│   ├── main.ts                               # 入口
│   ├── App.vue                               # 根组件
│   ├── router/
│   │   └── index.ts                          # 路由定义
│   ├── stores/                               # Pinia 状态管理
│   │   ├── user.ts
│   │   ├── garden.ts
│   │   └── review.ts
│   ├── api/                                  # API 请求封装
│   │   ├── request.ts                        # axios 实例 + 拦截器
│   │   ├── user.ts
│   │   ├── card.ts
│   │   ├── category.ts
│   │   ├── garden.ts
│   │   ├── review.ts
│   │   ├── badge.ts
│   │   ├── stats.ts
│   │   └── studyPack.ts
│   ├── views/                                # 页面组件
│   │   ├── LoginView.vue
│   │   ├── RegisterView.vue
│   │   ├── GardenView.vue                    # 花园主页
│   │   ├── ReviewView.vue                    # 复习页面
│   │   ├── ReviewSummaryView.vue             # 复习完成总结
│   │   ├── CardCreateView.vue                # 创建知识卡片
│   │   ├── CardEditView.vue                  # 编辑知识卡片
│   │   ├── CardListView.vue                  # 知识卡片列表
│   │   ├── CategoryManageView.vue            # 分类管理
│   │   ├── StudyPackView.vue                 # 预设知识库
│   │   ├── BadgeView.vue                     # 成就徽章
│   │   ├── StatsView.vue                     # 学习统计
│   │   └── ProfileView.vue                   # 个人中心
│   ├── components/                           # 可复用组件
│   │   ├── PlantCard.vue                     # 单个植物展示卡片
│   │   ├── PlantStageIcon.vue                # 植物阶段图标
│   │   ├── ReviewCard.vue                    # 复习卡片（正面/背面翻转）
│   │   ├── BadgeItem.vue                     # 徽章展示项
│   │   ├── StatsChart.vue                    # 统计图表
│   │   ├── StreakCalendar.vue                # 打卡日历热力图
│   │   ├── CategoryTag.vue                   # 分类标签
│   │   └── Navbar.vue                        # 导航栏
│   ├── assets/                               # 静态资源
│   │   ├── images/
│   │   │   └── plants/                       # 植物各阶段图片
│   │   │       ├── seed.png
│   │   │       ├── sprout.png
│   │   │       ├── growing.png
│   │   │       ├── blooming.png
│   │   │       ├── fruiting.png
│   │   │       └── withered.png
│   │   └── styles/
│   │       └── global.scss
│   └── utils/
│       ├── date.ts                           # 日期工具
│       └── format.ts                         # 格式化工具
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

---

## 三、数据库设计（MySQL）

### 3.1 ER 关系概览

```
User 1──N Category        （用户拥有多个分类）
User 1──N KnowledgeCard   （用户拥有多张知识卡片）
User 1──N Plant           （用户拥有多棵植物）
User 1──N ReviewRecord    （用户拥有多条复习记录）
User 1──N UserBadge       （用户拥有多个徽章）
User 1──N StudyPackImport （用户导入多个知识包）

Category 1──N KnowledgeCard  （分类下有多张卡片）

KnowledgeCard 1──1 Plant        （一张卡片对应一棵植物）
KnowledgeCard 1──N ReviewRecord （一张卡片有多条复习记录）

Badge 1──N UserBadge  （一个徽章可被多用户获得）

StudyPack 1──N StudyPackItem （知识包包含多个条目）
```

### 3.2 表结构定义

#### t_user（用户表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码（BCrypt 加密） |
| nickname | VARCHAR(50) | | 昵称 |
| avatar_url | VARCHAR(500) | | 头像 URL |
| current_streak | INT | DEFAULT 0 | 当前连续打卡天数 |
| max_streak | INT | DEFAULT 0 | 历史最长连续天数 |
| last_check_in | DATE | | 最后打卡日期 |
| status | TINYINT | DEFAULT 1 | 状态：0-禁用，1-正常 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除：0-未删除，1-已删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### t_category（分类表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| user_id | BIGINT | NOT NULL, FK | 所属用户 |
| name | VARCHAR(100) | NOT NULL | 分类名称 |
| icon | VARCHAR(50) | | 分类图标标识 |
| sort_order | INT | DEFAULT 0 | 排序序号 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### t_knowledge_card（知识卡片表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| user_id | BIGINT | NOT NULL, FK | 所属用户 |
| category_id | BIGINT | FK | 所属分类（可为空=未分类） |
| front_content | TEXT | NOT NULL | 正面内容（问题） |
| back_content | TEXT | NOT NULL | 背面内容（答案） |
| note | TEXT | | 备注 |
| source_type | TINYINT | DEFAULT 0 | 来源：0-手动创建，1-预设库导入 |
| source_pack_id | BIGINT | FK | 来源知识包 ID（导入时记录） |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### t_plant（植物表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| user_id | BIGINT | NOT NULL, FK | 所属用户 |
| card_id | BIGINT | NOT NULL, FK, UNIQUE | 关联知识卡片（1:1） |
| growth_stage | TINYINT | DEFAULT 1 | 生长阶段：1-种子，2-发芽，3-成长，4-开花，5-结果 |
| is_withered | TINYINT | DEFAULT 0 | 是否枯萎：0-否，1-是 |
| stage_success_count | INT | DEFAULT 0 | 当前阶段成功复习次数 |
| total_review_count | INT | DEFAULT 0 | 总复习次数 |
| review_round | INT | DEFAULT 0 | 当前艾宾浩斯复习轮次（0=未复习过） |
| next_review_date | DATE | NOT NULL | 下次应复习日期 |
| last_review_date | DATE | | 最后复习日期 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### t_review_record（复习记录表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| user_id | BIGINT | NOT NULL, FK | 所属用户 |
| card_id | BIGINT | NOT NULL, FK | 关联知识卡片 |
| plant_id | BIGINT | NOT NULL, FK | 关联植物 |
| self_evaluation | TINYINT | NOT NULL | 自评：1-记住了，2-模糊，3-忘记了 |
| prev_stage | TINYINT | | 复习前生长阶段 |
| after_stage | TINYINT | | 复习后生长阶段 |
| prev_round | INT | | 复习前复习轮次 |
| after_round | INT | | 复习后复习轮次 |
| was_withered | TINYINT | DEFAULT 0 | 复习时是否处于枯萎状态 |
| scheduled_date | DATE | | 原计划复习日期 |
| actual_date | DATE | NOT NULL | 实际复习日期 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |

#### t_badge（徽章定义表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| name | VARCHAR(50) | NOT NULL, UNIQUE | 徽章名称 |
| description | VARCHAR(200) | NOT NULL | 解锁条件描述 |
| icon | VARCHAR(100) | | 徽章图标标识 |
| rarity | TINYINT | DEFAULT 0 | 稀有度：0-普通，1-稀有，2-史诗 |
| condition_type | VARCHAR(50) | NOT NULL | 达成条件类型（见 6.4 节） |
| condition_value | INT | NOT NULL | 达成条件阈值 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |

#### t_user_badge（用户徽章关联表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| user_id | BIGINT | NOT NULL, FK | 用户 |
| badge_id | BIGINT | NOT NULL, FK | 徽章 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 获得时间 |

**唯一约束**：(user_id, badge_id)

#### t_study_pack（预设知识包表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| name | VARCHAR(100) | NOT NULL | 知识包名称 |
| description | TEXT | | 知识包描述 |
| category_name | VARCHAR(100) | | 建议分类名称 |
| card_count | INT | DEFAULT 0 | 包含卡片数 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |
| update_time | DATETIME | NOT NULL | 更新时间 |

#### t_study_pack_item（知识包条目表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| pack_id | BIGINT | NOT NULL, FK | 所属知识包 |
| front_content | TEXT | NOT NULL | 正面内容 |
| back_content | TEXT | NOT NULL | 背面内容 |
| note | TEXT | | 备注 |
| sort_order | INT | DEFAULT 0 | 排序序号 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 创建时间 |

#### t_study_pack_import（用户导入记录表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO | 主键 |
| user_id | BIGINT | NOT NULL, FK | 用户 |
| pack_id | BIGINT | NOT NULL, FK | 知识包 |
| is_deleted | TINYINT | DEFAULT 0 | 软删除 |
| create_time | DATETIME | NOT NULL | 导入时间 |

---

## 四、API 接口设计

### 4.1 统一返回结构

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

- `code=0`：成功
- `code!=0`：失败，message 描述原因

### 4.2 接口清单

> 以下所有接口前缀：`/api`

#### 用户模块 `/api/user`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| U-01 | POST | /user/register | 用户注册 | spec §2.1 用户系统 |
| U-02 | POST | /user/login | 用户登录 | spec §2.1 用户系统 |
| U-03 | GET | /user/current | 获取当前登录用户信息 | - |
| U-04 | PUT | /user/profile | 修改个人信息（昵称/头像） | - |

#### 分类模块 `/api/category`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| C-01 | POST | /category | 创建分类 | spec §2.1 自定义分类 |
| C-02 | GET | /category/list | 获取当前用户分类列表 | spec §2.1 自定义分类 |
| C-03 | PUT | /category/{id} | 修改分类 | spec §2.1 自定义分类 |
| C-04 | DELETE | /category/{id} | 删除分类（软删除） | spec §2.1 自定义分类 |

#### 知识卡片模块 `/api/card`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| K-01 | POST | /card | 创建知识卡片 | spec §3.1 卡片创建流程 |
| K-02 | GET | /card/{id} | 获取卡片详情 | - |
| K-03 | GET | /card/list | 获取卡片列表（支持分类筛选、分页） | spec §3.4 花园视图筛选 |
| K-04 | PUT | /card/{id} | 修改知识卡片 | - |
| K-05 | DELETE | /card/{id} | 删除知识卡片（软删除，同步软删除植物） | - |

#### 花园模块 `/api/garden`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| G-01 | GET | /garden | 获取花园视图数据（所有植物+分组统计） | spec §3.4 花园视图 |
| G-02 | GET | /garden/filter | 按分类/阶段/状态筛选植物 | spec §3.4 筛选功能 |
| G-03 | GET | /garden/sort | 按时间/复习日期/阶段排序 | spec §3.4 排序选项 |
| G-04 | GET | /garden/withered | 获取枯萎植物列表 | spec §3.2 枯萎机制 |

#### 复习模块 `/api/review`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| R-01 | GET | /review/pending | 获取今日待复习列表 | spec §3.3 复习流程-1 |
| R-02 | GET | /review/next | 获取下一棵待复习植物详情 | spec §3.3 复习流程-2~5 |
| R-03 | POST | /review/submit | 提交复习自评结果 | spec §3.3 复习流程-6~7 |
| R-04 | GET | /review/summary | 获取今日复习完成总结 | spec §3.3 复习流程-9 |
| R-05 | GET | /review/pending-count | 获取待复习数量（用于导航栏徽标） | spec §2.1 站内通知 |

#### 徽章模块 `/api/badge`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| B-01 | GET | /badge/list | 获取所有徽章列表（含获得状态） | spec §3.5 徽章展示 |
| B-02 | GET | /badge/my | 获取已获得的徽章列表 | spec §3.5 徽章展示 |

#### 统计模块 `/api/stats`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| S-01 | GET | /stats/today | 获取今日学习数据 | spec §3.6 今日数据 |
| S-02 | GET | /stats/trend | 获取周/月趋势数据 | spec §3.6 趋势图 |
| S-03 | GET | /stats/streak | 获取连续打卡数据 | spec §3.6 连续打卡 |
| S-04 | GET | /stats/stage-distribution | 获取各阶段植物数量分布 | spec §3.6 分布饼图 |

#### 预设知识库模块 `/api/study-pack`

| # | 方法 | 路径 | 说明 | 对应需求 |
|---|------|------|------|----------|
| P-01 | GET | /study-pack/list | 获取可用知识包列表 | spec §3.1 预设知识库 |
| P-02 | GET | /study-pack/{id} | 获取知识包详情（含条目预览） | spec §3.1 预设知识库 |
| P-03 | POST | /study-pack/{id}/import | 导入知识包到个人花园 | spec §3.1 预设知识库 |

---

## 五、前端页面与路由

### 5.1 路由表

| 路径 | 页面组件 | 需登录 | 说明 | 对应需求 |
|------|----------|--------|------|----------|
| /login | LoginView | 否 | 登录页 | spec §2.1 用户系统 |
| /register | RegisterView | 否 | 注册页 | spec §2.1 用户系统 |
| / | GardenView | 是 | 花园主页（默认页） | spec §3.4 花园视图 |
| /review | ReviewView | 是 | 复习页面 | spec §3.3 复习流程 |
| /review/summary | ReviewSummaryView | 是 | 复习完成总结 | spec §3.3 复习流程-9 |
| /card/create | CardCreateView | 是 | 创建知识卡片 | spec §3.1 卡片创建流程 |
| /card/:id/edit | CardEditView | 是 | 编辑知识卡片 | - |
| /cards | CardListView | 是 | 知识卡片列表 | - |
| /categories | CategoryManageView | 是 | 分类管理 | spec §2.1 自定义分类 |
| /study-packs | StudyPackView | 是 | 预设知识库 | spec §3.1 预设知识库 |
| /badges | BadgeView | 是 | 成就徽章 | spec §3.5 成就系统 |
| /stats | StatsView | 是 | 学习统计 | spec §3.6 统计面板 |
| /profile | ProfileView | 是 | 个人中心 | - |

### 5.2 页面交互流程

```
注册/登录 → 花园主页（默认）
              ├── 点击「新建知识」→ 创建卡片页 → 提交后返回花园
              ├── 点击「预设库」→ 知识库页 → 导入后返回花园
              ├── 点击「待复习」→ 复习页 → 逐张复习 → 总结页 → 返回花园
              ├── 点击植物 → 查看卡片详情
              ├── 导航栏 → 分类管理 / 徽章 / 统计 / 个人中心
              └── 花园筛选/排序 → 刷新花园视图
```

### 5.3 关键交互细节

**花园主页（GardenView）**：
- 顶部：待复习数量徽标 + 快捷入口
- 主体：植物网格展示，按生长阶段分组（种子区/发芽区/成长区/开花区/结果区）
- 枯萎植物灰色显示，带枯叶标识
- 待复习植物带闪烁/高亮边框
- 底部/侧边：筛选栏（分类）+ 排序切换

**复习页面（ReviewView）**：
- 卡片翻转交互：点击翻转查看答案
- 底部三个按钮：「记住了」（绿色）/「模糊」（黄色）/「忘记了」（红色）
- 顶部进度条：当前第 N 棵 / 共 M 棵

**复习总结页（ReviewSummaryView）**：
- 本次复习统计：记住了 X 棵、模糊 Y 棵、忘记了 Z 棵
- 生长变化：X 棵升级、Y 棵回退、Z 棵复活
- 新获得的徽章（如有）
- 按钮：「返回花园」

---

## 六、核心算法设计

### 6.1 艾宾浩斯间隔计算（EbbinghausCalculator）

```
输入：当前复习轮次 reviewRound
输出：下次复习间隔天数

间隔表：[1, 2, 4, 7, 15, 30]

算法：
  if reviewRound < 6:
    return 间隔表[reviewRound]      // 索引从 0 开始
  else:
    return 30                        // 后续固定 30 天
```

### 6.2 错过复习的智能重算

```
输入：scheduledDate（原计划日期）, actualDate（实际复习日期）, currentRound
输出：等效轮次 effectiveRound

算法：
  1. 计算实际经过天数 elapsedDays = actualDate - lastReviewDate
  2. 从间隔表累加，找到 elapsedDays 落在哪个区间
     cumulative = 0
     for i in 0..5:
       cumulative += 间隔表[i]
       if elapsedDays <= cumulative:
         effectiveRound = i
         break
     else:
       effectiveRound = currentRound  // 超出范围，保持当前轮次
  3. 返回 effectiveRound
```

### 6.3 生长阶段推进计算（GrowthStageCalculator）

```
输入：currentStage, selfEvaluation, stageSuccessCount, wasWithered
输出：newStage, newStageSuccessCount, newRound, nextReviewInterval

算法：
  // 1. 枯萎复活
  if wasWithered:
    newStage = max(1, currentStage - 1)  // 恢复到前一阶段
    newStageSuccessCount = 0
    newRound = currentRound              // 保持当前轮次
    nextReviewInterval = EbbinghausCalculator.calc(newRound)
    return

  // 2. 正常复习
  switch selfEvaluation:
    case REMEMBERED:                     // 记住了
      newStageSuccessCount = stageSuccessCount + 1
      if newStageSuccessCount >= 1 && currentStage < 5:
        newStage = currentStage + 1      // 升级
        newStageSuccessCount = 0
      else:
        newStage = currentStage          // 维持
      newRound = currentRound + 1

    case VAGUE:                          // 模糊
      newStage = currentStage            // 维持
      newStageSuccessCount = stageSuccessCount  // 不增加
      newRound = currentRound            // 不推进轮次，间隔缩短
      // 间隔缩短策略：取当前轮次间隔的一半
      nextReviewInterval = max(1, EbbinghausCalculator.calc(currentRound) / 2)
      return

    case FORGOTTEN:                      // 忘记了
      newStage = max(1, currentStage - 1)  // 回退一个阶段
      newStageSuccessCount = 0
      newRound = max(0, currentRound - 1)  // 回退一个轮次

  nextReviewInterval = EbbinghausCalculator.calc(newRound)
```

**生长阶段升级条件细化**：

| 当前阶段 | 升级到下一阶段所需成功次数 | 说明 |
|----------|--------------------------|------|
| 种子(1) → 发芽(2) | 1 次「记住了」 | spec: 首次复习且自评非"忘记" |
| 发芽(2) → 成长(3) | 1 次「记住了」 | spec: 第 2 次成功复习 |
| 成长(3) → 开花(4) | 1 次「记住了」 | spec: 第 3 次成功复习 |
| 开花(4) → 结果(5) | 1 次「记住了」 | spec: 连续多次成功复习 |

> MVP 统一为每个阶段 1 次成功即升级，后续可调整阈值。

### 6.4 枯萎判定计算（WitherCalculator）

```
输入：plant 的 nextReviewDate, 当前日期
输出：是否枯萎

算法：
  overdueDays = currentDate - nextReviewDate
  currentInterval = EbbinghausCalculator.calc(plant.reviewRound)
  witherThreshold = currentInterval * 3     // spec: 3 倍时间
  return overdueDays >= witherThreshold
```

**定时任务**：每日凌晨执行一次枯萎检测，扫描所有未枯萎植物，更新 `is_withered` 状态。

### 6.5 徽章达成判定（BadgeEvaluator）

```
徽章条件类型与判定逻辑：

| condition_type       | 判定逻辑                                    | condition_value |
|----------------------|---------------------------------------------|-----------------|
| PLANT_FIRST          | 用户种植的第 1 棵植物                        | 1               |
| STREAK_DAYS          | 连续学习天数 >= value                        | 7 / 30          |
| BLOOMING_COUNT       | 当前开花阶段植物数 >= value                  | 10              |
| FRUIT_FIRST          | 首次有植物到达结果阶段                       | 1               |
| TOTAL_PLANTS         | 总种植植物数 >= value                        | 100             |
| REVIVE_COUNT         | 成功复活枯萎植物数 >= value                  | 5               |
| CATEGORY_COUNT       | 创建分类数 >= value                          | 5               |
```

**触发时机**：在以下操作后异步执行徽章检测
- 创建知识卡片 → 检测 PLANT_FIRST, TOTAL_PLANTS
- 提交复习结果 → 检测 STREAK_DAYS, BLOOMING_COUNT, FRUIT_FIRST, REVIVE_COUNT
- 创建分类 → 检测 CATEGORY_COUNT

---

## 七、实现阶段划分

### Phase 0：项目初始化（基础骨架）

| 任务 | 说明 | 依赖 |
|------|------|------|
| P0-01 | 创建 Spring Boot 后端项目，配置 SQLite + MyBatis | 无 |
| P0-02 | 创建 Vue 3 + TS + Vite + Element Plus 前端项目 | 无 |
| P0-03 | 编写 SQLite schema.sql 建表脚本 | P0-01 |
| P0-04 | 实现统一返回结构 BaseResponse + 异常处理 | P0-01 |
| P0-05 | 配置跨域、Swagger、全局拦截器 | P0-01 |
| P0-06 | 前端 axios 封装 + 路由骨架 + 布局组件 | P0-02 |

### Phase 1：用户系统（核心前置）

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P1-01 | 后端：User 实体 + Mapper + Service + Controller | P0 | spec §2.1 用户系统 |
| P1-02 | 后端：注册接口（参数校验 + BCrypt 加密） | P1-01 | spec §2.1 用户系统 |
| P1-03 | 后端：登录接口（校验 + 生成 Token） | P1-01 | spec §2.1 用户系统 |
| P1-04 | 后端：AuthInterceptor 登录鉴权拦截器 | P1-03 | spec §4.2 权限 |
| P1-05 | 前端：登录/注册页面 | P0-06 | spec §2.1 用户系统 |
| P1-06 | 前端：登录状态管理（Pinia + Token 持久化） | P1-05 | - |

### Phase 2：知识管理（核心数据）

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P2-01 | 后端：Category 实体 + Mapper + CRUD | P1 | spec §2.1 自定义分类 |
| P2-02 | 后端：KnowledgeCard 实体 + Mapper + CRUD | P1 | spec §2.1 创建知识卡片 |
| P2-03 | 后端：创建卡片时自动创建 Plant（种子状态） | P2-02 | spec §3.1 卡片创建流程-5 |
| P2-04 | 后端：删除卡片时软删除关联 Plant | P2-02 | AGENTS.md 软删除规则 |
| P2-05 | 前端：分类管理页面 | P2-01 | spec §2.1 自定义分类 |
| P2-06 | 前端：创建/编辑知识卡片页面 | P2-02 | spec §3.1 卡片创建流程 |
| P2-07 | 前端：知识卡片列表页面（分类筛选） | P2-02 | spec §3.4 筛选功能 |

### Phase 3：花园系统（核心可视化）

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P3-01 | 后端：Plant 实体 + Mapper | P2-03 | spec §2.1 花园系统 |
| P3-02 | 后端：花园视图接口（分组+统计） | P3-01 | spec §3.4 花园视图 |
| P3-03 | 后端：花园筛选/排序接口 | P3-01 | spec §3.4 筛选/排序 |
| P3-04 | 后端：枯萎检测定时任务 | P3-01 | spec §3.2 枯萎机制 |
| P3-05 | 前端：植物各阶段图片素材准备 | 无 | spec §3.2 统一外观 |
| P3-06 | 前端：PlantCard 组件（展示+状态标识） | P3-05 | spec §3.4 花园视图 |
| P3-07 | 前端：花园主页（网格布局+分组+筛选+排序） | P3-06 | spec §3.4 花园视图 |

### Phase 4：复习系统（核心闭环）

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P4-01 | 后端：EbbinghausCalculator 算法实现 | P0 | spec §3.2 艾宾浩斯 |
| P4-02 | 后端：GrowthStageCalculator 算法实现 | P4-01 | spec §3.2 生长规则 |
| P4-03 | 后端：WitherCalculator 算法实现 | P4-01 | spec §3.2 枯萎机制 |
| P4-04 | 后端：ReviewRecord 实体 + Mapper | P2 | spec §3.3 复习流程 |
| P4-05 | 后端：获取待复习列表接口 | P4-04 | spec §3.3 复习流程-1 |
| P4-06 | 后端：提交复习结果接口（核心：更新植物状态+计算下次复习） | P4-01~04 | spec §3.3 复习流程-6~7 |
| P4-07 | 后端：复习完成总结接口 | P4-06 | spec §3.3 复习流程-9 |
| P4-08 | 后端：打卡天数更新逻辑 | P4-06 | spec §2.1 连续打卡 |
| P4-09 | 前端：ReviewCard 翻转组件 | P3-05 | spec §3.3 复习流程-3~5 |
| P4-10 | 前端：复习页面（进度条+翻转+三按钮） | P4-09 | spec §3.3 复习流程 |
| P4-11 | 前端：复习总结页面 | P4-10 | spec §3.3 复习流程-9 |
| P4-12 | 前端：导航栏待复习数量徽标 | P4-05 | spec §2.1 站内通知 |

### Phase 5：成就与统计（激励闭环）

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P5-01 | 后端：Badge + UserBadge 实体 + Mapper | P1 | spec §3.5 成就系统 |
| P5-02 | 后端：BadgeEvaluator 徽章判定逻辑 | P5-01 | spec §3.5 成就系统 |
| P5-03 | 后端：复习提交后触发徽章检测 | P4-06, P5-02 | spec §3.5 成就系统 |
| P5-04 | 后端：徽章列表/已获得接口 | P5-01 | spec §3.5 徽章展示 |
| P5-05 | 后端：StatsService 统计接口 | P4-04 | spec §3.6 统计面板 |
| P5-06 | 前端：BadgeItem 组件 + 徽章页面 | P5-04 | spec §3.5 徽章展示 |
| P5-07 | 前端：StatsChart 图表组件 + 统计页面 | P5-05 | spec §3.6 统计面板 |
| P5-08 | 前端：StreakCalendar 打卡日历组件 | P5-05 | spec §3.6 连续打卡 |
| P5-09 | 前端：获得徽章庆祝弹窗 | P5-06 | spec §3.5 徽章展示 |

### Phase 6：预设知识库（体验增强）

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P6-01 | 后端：StudyPack + StudyPackItem 实体 + Mapper | P2 | spec §3.1 预设知识库 |
| P6-02 | 后端：知识包列表/详情接口 | P6-01 | spec §3.1 预设知识库 |
| P6-03 | 后端：知识包导入接口（批量创建卡片+植物） | P6-01 | spec §3.1 预设知识库 |
| P6-04 | 数据：准备 MVP 预设知识包数据（SQL 脚本） | P6-01 | spec §7.2 T-01 |
| P6-05 | 前端：预设知识库浏览+导入页面 | P6-02 | spec §3.1 预设知识库 |

### Phase 7：联调与优化

| 任务 | 说明 | 依赖 | 对应需求 |
|------|------|------|----------|
| P7-01 | 前后端全流程联调 | P1~P6 | - |
| P7-02 | 枯萎阈值调优（验证 3 倍是否合理） | P7-01 | spec §7.2 T-02 |
| P7-03 | 植物图片素材最终确认 | P7-01 | - |
| P7-04 | 响应时间优化（核心接口 < 500ms） | P7-01 | spec §5.1 |
| P7-05 | 浏览器兼容性测试 | P7-01 | spec §5.1 |

### 阶段依赖关系

```
Phase 0（骨架）
    ↓
Phase 1（用户系统）
    ↓
Phase 2（知识管理）──→ Phase 6（预设知识库）
    ↓
Phase 3（花园系统）
    ↓
Phase 4（复习系统）
    ↓
Phase 5（成就与统计）
    ↓
Phase 7（联调优化）
```

> Phase 6 可与 Phase 3~4 并行开发，不阻塞主线。

---

## 八、编码规范约束（来自 AGENTS.md）

以下规则在所有实现阶段必须严格遵守：

| # | 规则 | 说明 |
|---|------|------|
| R-01 | 所有类必须包含 JavaDoc 注释 | 格式：`@author jLU` + `@date` |
| R-02 | 所有 public 方法必须包含 JavaDoc | 含 `@param`、`@return`、`@throws` |
| R-03 | Controller 方法必须说明接口用途、请求参数、返回结构 | 见 AGENTS.md §四 |
| R-04 | 禁止物理删除数据 | 仅通过软删除（`is_deleted` 字段） |
| R-05 | 禁止嵌套循环 | 需要嵌套时使用 Stream API 或提取方法 |

---

## 九、需求追踪矩阵

| spec 需求编号 | 需求名称 | 优先级 | 实现阶段 | 关键接口/页面 |
|--------------|----------|--------|----------|--------------|
| §2.1 | 注册/登录 | P0 | Phase 1 | U-01, U-02 / LoginView, RegisterView |
| §2.1 | 创建知识卡片 | P0 | Phase 2 | K-01 / CardCreateView |
| §2.1 | 预设知识库 | P1 | Phase 6 | P-01~P-03 / StudyPackView |
| §2.1 | 自定义分类/标签 | P1 | Phase 2 | C-01~C-04 / CategoryManageView |
| §2.1 | 种植（5 阶段） | P0 | Phase 3 | G-01 / GardenView |
| §2.1 | 枯萎与复活 | P1 | Phase 3~4 | G-04, R-03 / GardenView |
| §2.1 | 自动排列布局 | P1 | Phase 3 | G-01 / GardenView |
| §2.1 | 艾宾浩斯曲线 | P0 | Phase 4 | R-01~R-03 / ReviewView |
| §2.1 | 用户自评 | P0 | Phase 4 | R-03 / ReviewView |
| §2.1 | 错过智能重算 | P1 | Phase 4 | R-03（算法内部） |
| §2.1 | 站内通知 | P0 | Phase 4 | R-05 / Navbar 徽标 |
| §2.1 | 徽章解锁 | P1 | Phase 5 | B-01, B-02 / BadgeView |
| §2.1 | 学习统计面板 | P1 | Phase 5 | S-01~S-04 / StatsView |
| §2.1 | 连续打卡天数 | P1 | Phase 5 | S-03 / StreakCalendar |
