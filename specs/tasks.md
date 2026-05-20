# 记忆花园系统 - 原子化任务列表

> 追踪文档：[specs/spec.md](./spec.md) | [specs/plan.md](./plan.md)
> 编码规范：[AGENTS.md](../AGENTS.md)
> 日期：2026-05-20
> 标记说明：`[P]` = 可并行执行 | `TEST` = 测试任务 | `IMPL` = 实现任务

---

## Phase 1: Foundation & Skeleton

> 解决方案骨架、项目结构、基础配置、依赖注入、日志、环境配置、前端基础工程初始化
> 不实现具体业务功能

### 1.1 后端骨架

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| F-01 | IMPL | 创建 Spring Boot 项目，配置 pom.xml 依赖（Spring Boot 2.7.x、MyBatis、MySQL Driver、Swagger/Knife4j、BCrypt、Lombok） | `pom.xml` | 无 | |
| F-02 | IMPL | 创建启动类 MemoryGardenApplication | `MemoryGardenApplication.java` | F-01 | |
| F-03 | IMPL | 创建 application.yml 主配置（MySQL 数据源、MyBatis 映射路径、服务端口） | `application.yml` | F-01 | |
| F-04 | IMPL | 创建 application-dev.yml 开发环境配置 | `application-dev.yml` | F-03 | |
| F-05 | IMPL | 创建统一返回结构 BaseResponse | `common/result/BaseResponse.java` | F-01 | |
| F-06 | IMPL | 创建返回码枚举 ResultCode | `common/result/ResultCode.java` | F-01 | |
| F-07 | IMPL | 创建业务异常类 BusinessException | `common/exception/BusinessException.java` | F-01 | |
| F-08 | IMPL | 创建全局异常处理器 GlobalExceptionHandler | `common/exception/GlobalExceptionHandler.java` | F-05, F-06, F-07 | |
| F-09 | IMPL | 创建常量定义类 Constant | `common/constant/Constant.java` | F-01 | |
| F-10 | IMPL | 创建跨域配置 CorsConfig | `config/CorsConfig.java` | F-01 | |
| F-11 | IMPL | 创建 Swagger 配置 SwaggerConfig | `config/SwaggerConfig.java` | F-01 | |
| F-12 | IMPL | 创建 MVC 配置 WebMvcConfig（预留拦截器注册点） | `config/WebMvcConfig.java` | F-01 | |

### 1.2 前端骨架

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| F-13 | IMPL | 创建 Vue 3 + TS + Vite 项目，配置 package.json 依赖（Vue3、Element Plus、Pinia、Vue Router、Axios、ECharts） | `package.json` | 无 | [P] |
| F-14 | IMPL | 创建 vite.config.ts 构建配置（代理、别名） | `vite.config.ts` | F-13 | |
| F-15 | IMPL | 创建 tsconfig.json TypeScript 配置 | `tsconfig.json` | F-13 | [P] |
| F-16 | IMPL | 创建 main.ts 入口文件（挂载 App、注册 ElementPlus、Pinia、Router） | `main.ts` | F-13 | |
| F-17 | IMPL | 创建 App.vue 根组件（router-view + 基础布局） | `App.vue` | F-13 | |
| F-18 | IMPL | 创建路由骨架 router/index.ts（空路由表，仅占位） | `router/index.ts` | F-13 | |
| F-19 | IMPL | 创建 axios 封装 api/request.ts（实例创建、请求/响应拦截器、Token 注入、统一错误处理） | `api/request.ts` | F-13 | |
| F-20 | IMPL | 创建全局样式 assets/styles/global.scss | `global.scss` | F-13 | [P] |
| F-21 | IMPL | 创建 Navbar 导航栏组件骨架 | `components/Navbar.vue` | F-17 | |

---

## Phase 2: Domain Model & Domain Tests (TDD)

> 领域实体、值对象、聚合、领域服务、仓储抽象、领域规则测试
> **必须先生成测试任务，再生成实现任务**

### 2.1 枚举与值对象

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| D-01 | TEST | 编写 GrowthStage 枚举测试：验证 5 个阶段值、名称映射、边界值 | `GrowthStageTest.java` | F-02 | |
| D-02 | IMPL | 实现 GrowthStage 枚举（SEED=1, SPROUT=2, GROWING=3, BLOOMING=4, FRUITING=5） | `model/enums/GrowthStage.java` | D-01 | |
| D-03 | TEST | 编写 SelfEvaluation 枚举测试：验证 3 个自评值 | `SelfEvaluationTest.java` | F-02 | [P] |
| D-04 | IMPL | 实现 SelfEvaluation 枚举（REMEMBERED=1, VAGUE=2, FORGOTTEN=3） | `model/enums/SelfEvaluation.java` | D-03 | |
| D-05 | TEST | 编写 BadgeRarity 枚举测试 | `BadgeRarityTest.java` | F-02 | [P] |
| D-06 | IMPL | 实现 BadgeRarity 枚举（COMMON=0, RARE=1, EPIC=2） | `model/enums/BadgeRarity.java` | D-05 | |
| D-07 | TEST | 编写 BadgeConditionType 枚举测试 | `BadgeConditionTypeTest.java` | F-02 | [P] |
| D-08 | IMPL | 实现 BadgeConditionType 枚举（PLANT_FIRST, STREAK_DAYS, BLOOMING_COUNT, FRUIT_FIRST, TOTAL_PLANTS, REVIVE_COUNT, CATEGORY_COUNT） | `model/enums/BadgeConditionType.java` | D-07 | |

### 2.2 领域实体

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| D-09 | TEST | 编写 User 实体测试：验证字段赋值、软删除标记、打卡天数更新逻辑 | `UserTest.java` | F-02 | |
| D-10 | IMPL | 实现 User 实体类（字段见 plan.md §3.2 t_user） | `model/entity/User.java` | D-09 | |
| D-11 | TEST | 编写 Category 实体测试：验证字段赋值、软删除 | `CategoryTest.java` | F-02 | [P] |
| D-12 | IMPL | 实现 Category 实体类 | `model/entity/Category.java` | D-11 | |
| D-13 | TEST | 编写 KnowledgeCard 实体测试：验证字段赋值、来源类型、软删除 | `KnowledgeCardTest.java` | F-02 | [P] |
| D-14 | IMPL | 实现 KnowledgeCard 实体类 | `model/entity/KnowledgeCard.java` | D-13 | |
| D-15 | TEST | 编写 Plant 实体测试：验证字段赋值、生长阶段默认值、枯萎状态切换 | `PlantTest.java` | D-02 | |
| D-16 | IMPL | 实现 Plant 实体类（含 growthStage 默认值=1、isWithered 默认值=false） | `model/entity/Plant.java` | D-15 | |
| D-17 | TEST | 编写 ReviewRecord 实体测试：验证字段赋值、前后阶段/轮次记录 | `ReviewRecordTest.java` | D-04 | [P] |
| D-18 | IMPL | 实现 ReviewRecord 实体类 | `model/entity/ReviewRecord.java` | D-17 | |
| D-19 | TEST | 编写 Badge 实体测试：验证字段赋值、条件类型 | `BadgeTest.java` | D-06, D-08 | |
| D-20 | IMPL | 实现 Badge 实体类 | `model/entity/Badge.java` | D-19 | |
| D-21 | TEST | 编写 UserBadge 实体测试：验证关联关系 | `UserBadgeTest.java` | F-02 | [P] |
| D-22 | IMPL | 实现 UserBadge 实体类 | `model/entity/UserBadge.java` | D-21 | |
| D-23 | TEST | 编写 StudyPack 实体测试 | `StudyPackTest.java` | F-02 | [P] |
| D-24 | IMPL | 实现 StudyPack 实体类 | `model/entity/StudyPack.java` | D-23 | |
| D-25 | TEST | 编写 StudyPackItem 实体测试 | `StudyPackItemTest.java` | F-02 | [P] |
| D-26 | IMPL | 实现 StudyPackItem 实体类 | `model/entity/StudyPackItem.java` | D-25 | |

### 2.3 领域算法（核心业务规则）

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| D-27 | TEST | 编写 EbbinghausCalculator 测试：验证各轮次间隔（1,2,4,7,15,30）、轮次>5 固定 30、轮次=0 返回 1 | `EbbinghausCalculatorTest.java` | F-02 | |
| D-28 | IMPL | 实现 EbbinghausCalculator（间隔表 [1,2,4,7,15,30]，轮次≥6 返回 30） | `algorithm/EbbinghausCalculator.java` | D-27 | |
| D-29 | TEST | 编写 EbbinghausCalculator 智能重算测试：验证错过 1 天/3 天/10 天/60 天的等效轮次计算 | `EbbinghausCalculatorTest.java`（追加） | D-28 | |
| D-30 | IMPL | 实现 EbbinghausCalculator.calcEffectiveRound 方法（智能重算） | `algorithm/EbbinghausCalculator.java` | D-29 | |
| D-31 | TEST | 编写 GrowthStageCalculator 测试：记住了→升级、模糊→维持+间隔缩短、忘记了→回退、枯萎复活、Stage5 不再升级、Stage1 不再回退 | `GrowthStageCalculatorTest.java` | D-02, D-04, D-28 | |
| D-32 | IMPL | 实现 GrowthStageCalculator（含枯萎复活分支、自评三分支、升级/回退逻辑） | `algorithm/GrowthStageCalculator.java` | D-31 | |
| D-33 | TEST | 编写 WitherCalculator 测试：逾期 3 倍阈值→枯萎、未逾期→不枯萎、边界值 | `WitherCalculatorTest.java` | D-28 | |
| D-34 | IMPL | 实现 WitherCalculator（逾期天数 ≥ 当前间隔 × 3 → 枯萎） | `algorithm/WitherCalculator.java` | D-33 | |
| D-35 | TEST | 编写 BadgeEvaluator 测试：各 condition_type 达成/未达成判定 | `BadgeEvaluatorTest.java` | D-08 | |
| D-36 | IMPL | 实现 BadgeEvaluator（7 种条件类型的判定逻辑） | `algorithm/BadgeEvaluator.java` | D-35 | |

---

## Phase 3: Application Use Cases & Application Tests (TDD)

> 应用服务/UseCase、输入输出模型、业务编排、事务边界抽象、应用层测试
> **必须先生成测试任务，再生成实现任务**

### 3.1 DTO / VO 模型

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-01 | IMPL | 创建 UserRegisterRequest DTO | `model/dto/UserRegisterRequest.java` | D-10 | |
| A-02 | IMPL | 创建 UserLoginRequest DTO | `model/dto/UserLoginRequest.java` | D-10 | [P] |
| A-03 | IMPL | 创建 CardCreateRequest DTO | `model/dto/CardCreateRequest.java` | D-14 | |
| A-04 | IMPL | 创建 CardUpdateRequest DTO | `model/dto/CardUpdateRequest.java` | D-14 | [P] |
| A-05 | IMPL | 创建 CategoryCreateRequest DTO | `model/dto/CategoryCreateRequest.java` | D-12 | [P] |
| A-06 | IMPL | 创建 ReviewSubmitRequest DTO | `model/dto/ReviewSubmitRequest.java` | D-18 | |
| A-07 | IMPL | 创建 StudyPackImportRequest DTO | `model/dto/StudyPackImportRequest.java` | D-24 | [P] |
| A-08 | IMPL | 创建 UserVO | `model/vo/UserVO.java` | D-10 | [P] |
| A-09 | IMPL | 创建 CardVO | `model/vo/CardVO.java` | D-14 | [P] |
| A-10 | IMPL | 创建 PlantVO | `model/vo/PlantVO.java` | D-16 | [P] |
| A-11 | IMPL | 创建 GardenVO（含分组统计） | `model/vo/GardenVO.java` | D-16 | |
| A-12 | IMPL | 创建 ReviewVO | `model/vo/ReviewVO.java` | D-18 | [P] |
| A-13 | IMPL | 创建 BadgeVO | `model/vo/BadgeVO.java` | D-20 | [P] |
| A-14 | IMPL | 创建 StatsVO | `model/vo/StatsVO.java` | D-16 | [P] |
| A-15 | IMPL | 创建 ReviewSummaryVO | `model/vo/ReviewSummaryVO.java` | D-18 | [P] |

### 3.2 Service 接口定义

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-16 | IMPL | 定义 UserService 接口（register、login、getCurrentUser、updateProfile） | `service/UserService.java` | D-10, A-01, A-02, A-08 | |
| A-17 | IMPL | 定义 CategoryService 接口（create、list、update、delete） | `service/CategoryService.java` | D-12, A-05 | [P] |
| A-18 | IMPL | 定义 KnowledgeCardService 接口（create、getById、list、update、delete） | `service/KnowledgeCardService.java` | D-14, A-03, A-04, A-09 | [P] |
| A-19 | IMPL | 定义 PlantService 接口（getGardenView、filter、sort、getWithered） | `service/PlantService.java` | D-16, A-11 | |
| A-20 | IMPL | 定义 ReviewService 接口（getPending、getNext、submit、getSummary、getPendingCount） | `service/ReviewService.java` | D-18, A-06, A-12, A-15 | |
| A-21 | IMPL | 定义 BadgeService 接口（getAllBadges、getMyBadges） | `service/BadgeService.java` | D-20, A-13 | [P] |
| A-22 | IMPL | 定义 StatsService 接口（getToday、getTrend、getStreak、getStageDistribution） | `service/StatsService.java` | D-16, A-14 | [P] |
| A-23 | IMPL | 定义 StudyPackService 接口（list、getDetail、importPack） | `service/StudyPackService.java` | D-24, A-07 | [P] |

### 3.3 Service 实现与测试（TDD）

#### UserService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-24 | TEST | 编写 UserServiceImpl.register 测试：注册成功、用户名重复、参数校验 | `UserServiceImplTest.java` | A-16 | |
| A-25 | IMPL | 实现 UserServiceImpl.register（BCrypt 加密、用户名唯一校验、落库） | `service/impl/UserServiceImpl.java` | A-24 | |
| A-26 | TEST | 编写 UserServiceImpl.login 测试：登录成功、密码错误、用户不存在 | `UserServiceImplTest.java`（追加） | A-25 | |
| A-27 | IMPL | 实现 UserServiceImpl.login（密码校验、生成 Token） | `service/impl/UserServiceImpl.java` | A-26 | |
| A-28 | TEST | 编写 UserServiceImpl.getCurrentUser / updateProfile 测试 | `UserServiceImplTest.java`（追加） | A-25 | |
| A-29 | IMPL | 实现 UserServiceImpl.getCurrentUser + updateProfile | `service/impl/UserServiceImpl.java` | A-28 | |

#### CategoryService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-30 | TEST | 编写 CategoryServiceImpl CRUD 测试：创建、列表、修改、软删除 | `CategoryServiceImplTest.java` | A-17 | |
| A-31 | IMPL | 实现 CategoryServiceImpl 全部方法 | `service/impl/CategoryServiceImpl.java` | A-30 | |

#### KnowledgeCardService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-32 | TEST | 编写 KnowledgeCardServiceImpl.create 测试：创建成功、自动创建 Plant 种子、分类校验 | `KnowledgeCardServiceImplTest.java` | A-18 | |
| A-33 | IMPL | 实现 KnowledgeCardServiceImpl.create（含自动创建 Plant） | `service/impl/KnowledgeCardServiceImpl.java` | A-32 | |
| A-34 | TEST | 编写 KnowledgeCardServiceImpl.delete 测试：软删除卡片 + 软删除关联 Plant | `KnowledgeCardServiceImplTest.java`（追加） | A-33 | |
| A-35 | IMPL | 实现 KnowledgeCardServiceImpl.delete（级联软删除 Plant） | `service/impl/KnowledgeCardServiceImpl.java` | A-34 | |
| A-36 | TEST | 编写 KnowledgeCardServiceImpl.getById / list / update 测试 | `KnowledgeCardServiceImplTest.java`（追加） | A-33 | |
| A-37 | IMPL | 实现 KnowledgeCardServiceImpl.getById + list + update | `service/impl/KnowledgeCardServiceImpl.java` | A-36 | |

#### PlantService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-38 | TEST | 编写 PlantServiceImpl.getGardenView 测试：按阶段分组、统计各阶段数量 | `PlantServiceImplTest.java` | A-19 | |
| A-39 | IMPL | 实现 PlantServiceImpl.getGardenView | `service/impl/PlantServiceImpl.java` | A-38 | |
| A-40 | TEST | 编写 PlantServiceImpl.filter / sort / getWithered 测试 | `PlantServiceImplTest.java`（追加） | A-39 | |
| A-41 | IMPL | 实现 PlantServiceImpl.filter + sort + getWithered | `service/impl/PlantServiceImpl.java` | A-40 | |

#### ReviewService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-42 | TEST | 编写 ReviewServiceImpl.getPending / getNext 测试：仅返回今日到期、按日期排序 | `ReviewServiceImplTest.java` | A-20 | |
| A-43 | IMPL | 实现 ReviewServiceImpl.getPending + getNext | `service/impl/ReviewServiceImpl.java` | A-42 | |
| A-44 | TEST | 编写 ReviewServiceImpl.submit 测试：记住了→升级+推进轮次、模糊→维持+缩短间隔、忘记了→回退、枯萎复活、打卡天数更新 | `ReviewServiceImplTest.java`（追加） | A-43 | |
| A-45 | IMPL | 实现 ReviewServiceImpl.submit（核心：调用 GrowthStageCalculator + EbbinghausCalculator + 更新 Plant + 记录 ReviewRecord + 更新打卡） | `service/impl/ReviewServiceImpl.java` | A-44 | |
| A-46 | TEST | 编写 ReviewServiceImpl.getSummary / getPendingCount 测试 | `ReviewServiceImplTest.java`（追加） | A-45 | |
| A-47 | IMPL | 实现 ReviewServiceImpl.getSummary + getPendingCount | `service/impl/ReviewServiceImpl.java` | A-46 | |

#### BadgeService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-48 | TEST | 编写 BadgeServiceImpl.getAllBadges / getMyBadges 测试 | `BadgeServiceImplTest.java` | A-21 | [P] |
| A-49 | IMPL | 实现 BadgeServiceImpl | `service/impl/BadgeServiceImpl.java` | A-48 | [P] |
| A-50 | TEST | 编写 BadgeServiceImpl.evaluateAndAward 测试：达成条件→授予徽章、已获得→不重复授予 | `BadgeServiceImplTest.java`（追加） | A-49 | |
| A-51 | IMPL | 实现 BadgeServiceImpl.evaluateAndAward（调用 BadgeEvaluator + 写入 UserBadge） | `service/impl/BadgeServiceImpl.java` | A-50 | |

#### StatsService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-52 | TEST | 编写 StatsServiceImpl.getToday 测试：今日新增/复习/生长回退数 | `StatsServiceImplTest.java` | A-22 | [P] |
| A-53 | IMPL | 实现 StatsServiceImpl.getToday | `service/impl/StatsServiceImpl.java` | A-52 | [P] |
| A-54 | TEST | 编写 StatsServiceImpl.getTrend / getStreak / getStageDistribution 测试 | `StatsServiceImplTest.java`（追加） | A-53 | |
| A-55 | IMPL | 实现 StatsServiceImpl.getTrend + getStreak + getStageDistribution | `service/impl/StatsServiceImpl.java` | A-54 | |

#### StudyPackService

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| A-56 | TEST | 编写 StudyPackServiceImpl.list / getDetail 测试 | `StudyPackServiceImplTest.java` | A-23 | [P] |
| A-57 | IMPL | 实现 StudyPackServiceImpl.list + getDetail | `service/impl/StudyPackServiceImpl.java` | A-56 | [P] |
| A-58 | TEST | 编写 StudyPackServiceImpl.importPack 测试：批量创建卡片+植物、导入记录 | `StudyPackServiceImplTest.java`（追加） | A-57 | |
| A-59 | IMPL | 实现 StudyPackServiceImpl.importPack（批量创建 KnowledgeCard + Plant + 记录导入） | `service/impl/StudyPackServiceImpl.java` | A-58 | |

---

## Phase 4: API Contracts & Web API (TDD)

> API DTO、错误响应模型、Controller、请求校验、DTO 映射、Result 到 HTTP 响应映射、接口测试
> **必须先生成接口测试任务，再生成实现任务**

### 4.1 鉴权拦截器

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-01 | IMPL | 实现 AuthInterceptor 登录鉴权拦截器（Token 校验、从 Token 解析 userId、存入 request attribute） | `interceptor/AuthInterceptor.java` | A-27 | |
| C-02 | IMPL | 在 WebMvcConfig 中注册 AuthInterceptor（排除登录/注册路径） | `config/WebMvcConfig.java` | C-01 | |

### 4.2 UserController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-03 | TEST | 编写 UserController.register 接口测试：POST /api/user/register 成功 200、参数缺失 400、用户名重复 | `UserControllerTest.java` | C-02 | |
| C-04 | IMPL | 实现 UserController.register | `controller/UserController.java` | C-03 | |
| C-05 | TEST | 编写 UserController.login 接口测试：POST /api/user/login 成功返回 Token、密码错误 | `UserControllerTest.java`（追加） | C-04 | |
| C-06 | IMPL | 实现 UserController.login | `controller/UserController.java` | C-05 | |
| C-07 | TEST | 编写 UserController.getCurrentUser / updateProfile 接口测试 | `UserControllerTest.java`（追加） | C-06 | |
| C-08 | IMPL | 实现 UserController.getCurrentUser + updateProfile | `controller/UserController.java` | C-07 | |

### 4.3 CategoryController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-09 | TEST | 编写 CategoryController CRUD 接口测试：POST/GET/PUT/DELETE /api/category/* | `CategoryControllerTest.java` | C-02 | |
| C-10 | IMPL | 实现 CategoryController 全部接口 | `controller/CategoryController.java` | C-09 | |

### 4.4 KnowledgeCardController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-11 | TEST | 编写 KnowledgeCardController.create 接口测试：POST /api/card 成功、参数校验 | `KnowledgeCardControllerTest.java` | C-02 | |
| C-12 | IMPL | 实现 KnowledgeCardController.create | `controller/KnowledgeCardController.java` | C-11 | |
| C-13 | TEST | 编写 KnowledgeCardController.getById / list / update / delete 接口测试 | `KnowledgeCardControllerTest.java`（追加） | C-12 | |
| C-14 | IMPL | 实现 KnowledgeCardController.getById + list + update + delete | `controller/KnowledgeCardController.java` | C-13 | |

### 4.5 GardenController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-15 | TEST | 编写 GardenController.getGardenView 接口测试：GET /api/garden 返回分组数据 | `GardenControllerTest.java` | C-02 | |
| C-16 | IMPL | 实现 GardenController.getGardenView | `controller/GardenController.java` | C-15 | |
| C-17 | TEST | 编写 GardenController.filter / sort / withered 接口测试 | `GardenControllerTest.java`（追加） | C-16 | |
| C-18 | IMPL | 实现 GardenController.filter + sort + withered | `controller/GardenController.java` | C-17 | |

### 4.6 ReviewController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-19 | TEST | 编写 ReviewController.getPending / getNext 接口测试 | `ReviewControllerTest.java` | C-02 | |
| C-20 | IMPL | 实现 ReviewController.getPending + getNext | `controller/ReviewController.java` | C-19 | |
| C-21 | TEST | 编写 ReviewController.submit 接口测试：POST /api/review/submit 成功、自评值校验 | `ReviewControllerTest.java`（追加） | C-20 | |
| C-22 | IMPL | 实现 ReviewController.submit（含徽章检测触发） | `controller/ReviewController.java` | C-21 | |
| C-23 | TEST | 编写 ReviewController.summary / pendingCount 接口测试 | `ReviewControllerTest.java`（追加） | C-22 | |
| C-24 | IMPL | 实现 ReviewController.summary + pendingCount | `controller/ReviewController.java` | C-23 | |

### 4.7 BadgeController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-25 | TEST | 编写 BadgeController.getAllBadges / getMyBadges 接口测试 | `BadgeControllerTest.java` | C-02 | [P] |
| C-26 | IMPL | 实现 BadgeController | `controller/BadgeController.java` | C-25 | [P] |

### 4.8 StatsController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-27 | TEST | 编写 StatsController.today / trend / streak / stageDistribution 接口测试 | `StatsControllerTest.java` | C-02 | [P] |
| C-28 | IMPL | 实现 StatsController 全部接口 | `controller/StatsController.java` | C-27 | [P] |

### 4.9 StudyPackController

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| C-29 | TEST | 编写 StudyPackController.list / getDetail / importPack 接口测试 | `StudyPackControllerTest.java` | C-02 | [P] |
| C-30 | IMPL | 实现 StudyPackController 全部接口 | `controller/StudyPackController.java` | C-29 | [P] |

---

## Phase 5: Infrastructure & Integration

> 仓储实现、数据库映射、外部服务适配、认证落地、配置实现、集成测试支撑

### 5.1 数据库建表

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| I-01 | IMPL | 编写 MySQL schema.sql 建表脚本（t_user、t_category、t_knowledge_card、t_plant、t_review_record、t_badge、t_user_badge、t_study_pack、t_study_pack_item、t_study_pack_import） | `db/schema.sql` | F-03 | |
| I-02 | IMPL | 编写徽章初始数据 SQL（8 枚徽章 INSERT） | `db/init-badges.sql` | I-01 | [P] |
| I-03 | IMPL | 编写预设知识包初始数据 SQL | `db/init-study-packs.sql` | I-01 | [P] |

### 5.2 MyBatis Mapper 实现

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| I-04 | IMPL | 实现 UserMapper 接口 + XML（selectById、selectByUsername、insert、update、逻辑删除） | `UserMapper.java` + `UserMapper.xml` | I-01 | |
| I-05 | IMPL | 实现 CategoryMapper 接口 + XML（selectByUserId、insert、update、逻辑删除） | `CategoryMapper.java` + `CategoryMapper.xml` | I-01 | [P] |
| I-06 | IMPL | 实现 KnowledgeCardMapper 接口 + XML（selectById、selectByUserId、selectByCategoryId、insert、update、逻辑删除） | `KnowledgeCardMapper.java` + `KnowledgeCardMapper.xml` | I-01 | [P] |
| I-07 | IMPL | 实现 PlantMapper 接口 + XML（selectByUserId、selectByCardId、selectWithered、countByStage、insert、update、逻辑删除） | `PlantMapper.java` + `PlantMapper.xml` | I-01 | [P] |
| I-08 | IMPL | 实现 ReviewRecordMapper 接口 + XML（selectByCardId、selectByUserIdAndDate、countByUserIdAndDate、insert） | `ReviewRecordMapper.java` + `ReviewRecordMapper.xml` | I-01 | [P] |
| I-09 | IMPL | 实现 BadgeMapper 接口 + XML（selectAll、selectByConditionType） | `BadgeMapper.java` + `BadgeMapper.xml` | I-01 | [P] |
| I-10 | IMPL | 实现 UserBadgeMapper 接口 + XML（selectByUserId、existsByUserIdAndBadgeId、insert） | `UserBadgeMapper.java` + `UserBadgeMapper.xml` | I-01 | [P] |
| I-11 | IMPL | 实现 StudyPackMapper 接口 + XML（selectAll、selectById） | `StudyPackMapper.java` + `StudyPackMapper.xml` | I-01 | [P] |
| I-12 | IMPL | 实现 StudyPackItemMapper 接口 + XML（selectByPackId） | `StudyPackItemMapper.java` + `StudyPackItemMapper.xml` | I-01 | [P] |

### 5.3 枯萎检测定时任务

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| I-13 | IMPL | 实现枯萎检测定时任务 WitherCheckTask（每日凌晨扫描、调用 WitherCalculator、批量更新 is_withered） | `task/WitherCheckTask.java` | I-07, D-34 | |

### 5.4 集成测试

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| I-14 | TEST | 编写 UserService 集成测试（真实数据库：注册→登录→获取用户） | `UserServiceIntegrationTest.java` | I-04 | |
| I-15 | TEST | 编写 KnowledgeCard + Plant 集成测试（创建卡片→自动创建植物→删除卡片→级联软删除） | `CardPlantIntegrationTest.java` | I-06, I-07 | |
| I-16 | TEST | 编写 Review 全流程集成测试（创建卡片→复习提交→植物生长→间隔计算→枯萎→复活） | `ReviewIntegrationTest.java` | I-07, I-08 | |
| I-17 | TEST | 编写 Badge 集成测试（复习触发→徽章判定→授予） | `BadgeIntegrationTest.java` | I-09, I-10 | |
| I-18 | TEST | 编写 StudyPack 导入集成测试（浏览→导入→批量创建卡片+植物） | `StudyPackIntegrationTest.java` | I-11, I-12 | |

---

## Phase 6: Frontend UI & Interaction

> 路由、页面、组件、布局、APIService、表单处理、页面状态、鉴权态、联调任务

### 6.1 路由与布局

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-01 | IMPL | 完善路由表 router/index.ts（13 个路由 + 路由守卫：未登录重定向 /login） | `router/index.ts` | F-18 | |
| UI-02 | IMPL | 创建主布局组件 MainLayout.vue（Navbar + 侧边栏 + content 区域） | `components/MainLayout.vue` | UI-01 | |

### 6.2 状态管理

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-03 | IMPL | 实现 user store（login、register、getCurrentUser、logout、Token 持久化） | `stores/user.ts` | F-19 | |
| UI-04 | IMPL | 实现 garden store（gardenView 数据、筛选条件、排序方式） | `stores/garden.ts` | F-19 | [P] |
| UI-05 | IMPL | 实现 review store（pendingList、currentIndex、submitResult） | `stores/review.ts` | F-19 | [P] |

### 6.3 API Service 层

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-06 | IMPL | 封装 user API（register、login、getCurrentUser、updateProfile） | `api/user.ts` | F-19 | |
| UI-07 | IMPL | 封装 card API（create、getById、list、update、delete） | `api/card.ts` | F-19 | [P] |
| UI-08 | IMPL | 封装 category API（create、list、update、delete） | `api/category.ts` | F-19 | [P] |
| UI-09 | IMPL | 封装 garden API（getGardenView、filter、sort、withered） | `api/garden.ts` | F-19 | [P] |
| UI-10 | IMPL | 封装 review API（getPending、getNext、submit、getSummary、getPendingCount） | `api/review.ts` | F-19 | [P] |
| UI-11 | IMPL | 封装 badge API（getAllBadges、getMyBadges） | `api/badge.ts` | F-19 | [P] |
| UI-12 | IMPL | 封装 stats API（getToday、getTrend、getStreak、getStageDistribution） | `api/stats.ts` | F-19 | [P] |
| UI-13 | IMPL | 封装 studyPack API（list、getDetail、importPack） | `api/studyPack.ts` | F-19 | [P] |

### 6.4 基础组件

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-14 | IMPL | 实现 PlantStageIcon 组件（根据阶段显示对应图标） | `components/PlantStageIcon.vue` | 无 | |
| UI-15 | IMPL | 实现 CategoryTag 组件（分类标签展示+颜色） | `components/CategoryTag.vue` | 无 | [P] |
| UI-16 | IMPL | 实现 PlantCard 组件（植物卡片：图标+名称+阶段+枯萎标识+待复习高亮） | `components/PlantCard.vue` | UI-14 | |
| UI-17 | IMPL | 实现 ReviewCard 组件（卡片翻转交互：正面→点击→背面） | `components/ReviewCard.vue` | UI-14 | |
| UI-18 | IMPL | 实现 BadgeItem 组件（徽章展示：图标+名称+条件、未获得灰色） | `components/BadgeItem.vue` | 无 | [P] |
| UI-19 | IMPL | 实现 StatsChart 组件（ECharts 折线图+饼图） | `components/StatsChart.vue` | 无 | [P] |
| UI-20 | IMPL | 实现 StreakCalendar 组件（打卡日历热力图） | `components/StreakCalendar.vue` | 无 | [P] |

### 6.5 页面实现

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-21 | IMPL | 实现 LoginView 登录页（表单+校验+调用 API+跳转） | `views/LoginView.vue` | UI-03, UI-06 | |
| UI-22 | IMPL | 实现 RegisterView 注册页（表单+校验+调用 API+跳转登录） | `views/RegisterView.vue` | UI-03, UI-06 | [P] |
| UI-23 | IMPL | 实现 GardenView 花园主页（植物网格+分组+筛选+排序+待复习高亮+枯萎标识） | `views/GardenView.vue` | UI-04, UI-09, UI-16 | |
| UI-24 | IMPL | 实现 CardCreateView 创建知识卡片页（表单+分类选择+调用 API） | `views/CardCreateView.vue` | UI-07, UI-08 | |
| UI-25 | IMPL | 实现 CardEditView 编辑知识卡片页（加载已有数据+表单+调用 API） | `views/CardEditView.vue` | UI-07, UI-08 | |
| UI-26 | IMPL | 实现 CardListView 知识卡片列表页（表格+分类筛选+分页+编辑/删除） | `views/CardListView.vue` | UI-07, UI-08 | |
| UI-27 | IMPL | 实现 CategoryManageView 分类管理页（列表+新增+编辑+删除） | `views/CategoryManageView.vue` | UI-08 | |
| UI-28 | IMPL | 实现 ReviewView 复习页（进度条+ReviewCard 翻转+三按钮+自动跳转下一棵） | `views/ReviewView.vue` | UI-05, UI-10, UI-17 | |
| UI-29 | IMPL | 实现 ReviewSummaryView 复习总结页（统计+生长变化+新徽章+返回花园） | `views/ReviewSummaryView.vue` | UI-05, UI-10 | |
| UI-30 | IMPL | 实现 BadgeView 徽章页（全部徽章网格+获得状态+悬停条件） | `views/BadgeView.vue` | UI-11, UI-18 | |
| UI-31 | IMPL | 实现 StatsView 统计页（今日数据+趋势图+阶段分布+打卡日历） | `views/StatsView.vue` | UI-12, UI-19, UI-20 | |
| UI-32 | IMPL | 实现 StudyPackView 预设知识库页（知识包列表+详情预览+一键导入） | `views/StudyPackView.vue` | UI-13 | |
| UI-33 | IMPL | 实现 ProfileView 个人中心页（用户信息+修改昵称/头像+徽章概览+打卡天数） | `views/ProfileView.vue` | UI-03, UI-06 | |

### 6.6 Navbar 完善

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-34 | IMPL | 完善 Navbar 组件（待复习数量徽标+导航链接+用户头像+退出登录） | `components/Navbar.vue` | UI-03, UI-10 | |

### 6.7 植物图片素材

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-35 | IMPL | 准备植物各阶段图片素材（seed.png、sprout.png、growing.png、blooming.png、fruiting.png、withered.png） | `assets/images/plants/` | 无 | |

### 6.8 联调

| ID | 类型 | 任务描述 | 涉及文件 | 依赖 | 标记 |
|----|------|----------|----------|------|------|
| UI-36 | TEST | 前后端联调：注册→登录→创建分类→创建卡片→花园查看→复习→总结→查看统计→查看徽章 | 全流程 | UI-21~UI-34, I-01~I-12 | |
| UI-37 | TEST | 前后端联调：预设知识库浏览→导入→花园查看新植物 | 全流程 | UI-32, I-11, I-12 | |
| UI-38 | TEST | 前后端联调：枯萎场景（手动修改 next_review_date→刷新花园→查看枯萎标识→复习复活） | 全流程 | UI-23, I-13 | |

---

## 任务统计

| Phase | TEST 任务数 | IMPL 任务数 | 总计 |
|-------|------------|------------|------|
| Phase 1: Foundation | 0 | 21 | 21 |
| Phase 2: Domain Model | 14 | 14 | 28 |
| Phase 3: Application | 18 | 23 | 41 |
| Phase 4: API Contracts | 13 | 14 | 27 |
| Phase 5: Infrastructure | 5 | 13 | 18 |
| Phase 6: Frontend | 3 | 35 | 38 |
| **合计** | **53** | **120** | **173** |

---

## 依赖关系总图

```
Phase 1 (F-01 ~ F-21)
    │
    ▼
Phase 2 (D-01 ~ D-36)
    │
    ▼
Phase 3 (A-01 ~ A-59)
    │
    ├──────────────────┐
    ▼                  ▼
Phase 4 (C-01~C-30)  Phase 5 (I-01~I-18)  ← 可并行
    │                  │
    └──────┬───────────┘
           ▼
Phase 6 (UI-01 ~ UI-38)
```

> Phase 4 和 Phase 5 可并行推进：Phase 4 使用 Mock 完成接口测试，Phase 5 实现数据库层。两者完成后进入 Phase 6 前端联调。
