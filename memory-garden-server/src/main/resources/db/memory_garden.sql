/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80019 (8.0.19)
 Source Host           : localhost:3306
 Source Schema         : memory_garden

 Target Server Type    : MySQL
 Target Server Version : 80019 (8.0.19)
 File Encoding         : 65001

 Date: 20/05/2026 23:58:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_badge
-- ----------------------------
DROP TABLE IF EXISTS `t_badge`;
CREATE TABLE `t_badge`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '徽章名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '解锁条件描述',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '徽章图标标识',
  `rarity` tinyint NOT NULL DEFAULT 0 COMMENT '稀有度：0-普通，1-稀有，2-史诗',
  `condition_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '达成条件类型',
  `condition_value` int NOT NULL COMMENT '达成条件阈值',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '徽章定义表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_badge
-- ----------------------------
INSERT INTO `t_badge` VALUES (1, '初入花园', '种下第一棵知识植物', 'seed', 0, 'PLANT_FIRST', 1, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (2, '坚持不懈', '连续打卡7天', 'streak-7', 0, 'STREAK_DAYS', 7, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (3, '花开满园', '拥有5棵开花植物', 'bloom-5', 1, 'BLOOMING_COUNT', 5, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (4, '硕果累累', '第一棵植物结果', 'fruit-1', 1, 'FRUIT_FIRST', 1, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (5, '知识森林', '拥有20棵知识植物', 'forest', 2, 'TOTAL_PLANTS', 20, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (6, '枯木逢春', '复活1棵枯萎植物', 'revive', 1, 'REVIVE_COUNT', 1, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (7, '博学多才', '创建5个分类', 'category-5', 0, 'CATEGORY_COUNT', 5, 0, '2026-05-20 21:06:27');
INSERT INTO `t_badge` VALUES (8, '记忆大师', '连续打卡30天', 'streak-30', 2, 'STREAK_DAYS', 30, 0, '2026-05-20 21:06:27');

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类图标标识',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序序号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (1, 1, '英语', 'en', 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (2, 1, '数学', 'math', 2, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (3, 1, '历史', 'history', 3, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (4, 1, '物理', 'physics', 4, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (5, 1, '编程', 'code', 5, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (6, 2, 'Java', 'java', 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (7, 2, '数据结构', 'ds', 2, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (8, 2, 'Spring Boot', 'spring', 3, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (9, 3, '日常', 'daily', 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_category` VALUES (10, 3, '前端开发', 'frontend', 2, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_category` VALUES (11, 3, 'Python', 'python', 3, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_category` VALUES (12, 3, '英语', 'en', 4, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_category` VALUES (13, 3, '算法', 'algo', 5, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');

-- ----------------------------
-- Table structure for t_knowledge_card
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_card`;
CREATE TABLE `t_knowledge_card`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `category_id` bigint NULL DEFAULT NULL COMMENT '所属分类（可为空=未分类）',
  `front_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '正面内容（问题）',
  `back_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '背面内容（答案）',
  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `source_type` tinyint NOT NULL DEFAULT 0 COMMENT '来源：0-手动创建，1-预设库导入',
  `source_pack_id` bigint NULL DEFAULT NULL COMMENT '来源知识包 ID（导入时记录）',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识卡片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_card
-- ----------------------------
INSERT INTO `t_knowledge_card` VALUES (1, 1, 1, 'What is the past tense of \"go\"?', 'The past tense of \"go\" is \"went\".', '不规则动词', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (2, 1, 1, 'What is the difference between \"affect\" and \"effect\"?', '\"Affect\" is usually a verb meaning to influence; \"Effect\" is usually a noun meaning the result.', '易混淆词汇', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (3, 1, 1, 'What does \"ubiquitous\" mean?', 'Present, appearing, or found everywhere.', 'GRE词汇', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (4, 1, 1, 'What is a relative clause?', 'A clause that modifies a noun and usually begins with who, which, that, whom, whose.', '语法知识', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (5, 1, 2, '什么是斐波那契数列？', '每一项等于前两项之和的数列：1, 1, 2, 3, 5, 8, 13, 21, ...', '递推关系 F(n)=F(n-1)+F(n-2)', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (6, 1, 2, '欧拉公式是什么？', 'e^(iπ) + 1 = 0，连接了五个最重要的数学常数', '数学之美', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (7, 1, 2, '什么是矩阵的秩？', '矩阵中线性无关的行（或列）向量的最大个数', '线性代数核心概念', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (8, 1, 3, '工业革命发生在哪个世纪？', '18世纪中叶至19世纪，起源于英国', '约1760-1840年', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (9, 1, 3, '丝绸之路的起点是哪里？', '长安（今西安），是古代东西方贸易和文化交流的重要通道', '汉武帝时期张骞出使西域开辟', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (10, 1, 4, '牛顿第二定律的公式是什么？', 'F = ma，力等于质量乘以加速度', '经典力学基础', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (11, 1, 4, '什么是海森堡不确定性原理？', '不可能同时精确测量粒子的位置和动量，Δx·Δp ≥ ℏ/2', '量子力学基本原理', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (12, 1, 5, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (13, 1, 5, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (14, 1, 5, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (15, 1, 5, 'Java 中 final 关键字的作用？', '修饰类：不可继承；修饰方法：不可重写；修饰变量：常量，不可修改', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (16, 1, 5, 'Java 中接口和抽象类的区别？', '接口：多实现、默认方法(JDK8)、无状态；抽象类：单继承、可有构造器和成员变量', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (17, 1, 5, '什么是 Java 的垃圾回收机制？', 'JVM 自动回收不再被引用的对象，主要算法：标记-清除、复制、标记-整理', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (18, 1, NULL, '什么是艾宾浩斯遗忘曲线？', '遗忘速度先快后慢，复习间隔应逐渐增大：1天、2天、4天、7天、15天、30天', '本系统的核心算法', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (19, 1, NULL, '番茄工作法的核心步骤？', '1.选择任务 2.设定25分钟计时 3.专注工作 4.短暂休息5分钟 5.每4个番茄休息15-30分钟', '时间管理方法', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (20, 1, NULL, '什么是费曼学习法？', '通过向他人简单解释来检验自己是否真正理解，步骤：选择概念→教授→回顾→简化', '高效学习法', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (21, 2, 6, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (22, 2, 6, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (23, 2, 6, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', NULL, 1, 1, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (24, 2, 7, '数组和链表的区别？', '数组：连续内存、O(1)随机访问、O(n)插入删除；链表：离散内存、O(n)访问、O(1)插入删除', NULL, 1, 2, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (25, 2, 7, '栈和队列的区别？', '栈：后进先出(LIFO)，只能从栈顶操作；队列：先进先出(FIFO)，队尾入队首出', NULL, 1, 2, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (26, 2, 8, 'Spring Boot 自动配置原理？', '@EnableAutoConfiguration → SpringFactoriesLoader 加载 META-INF/spring.factories → 条件注解(@Conditional)筛选配置类', NULL, 1, 3, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (27, 2, 8, 'Spring Bean 的生命周期？', '实例化→属性赋值→Aware回调→BeanPostProcessor前置→初始化→BeanPostProcessor后置→使用→销毁', NULL, 1, 3, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (28, 2, NULL, '什么是 RESTful API？', '一种软件架构风格，使用HTTP动词操作资源：GET查询、POST创建、PUT更新、DELETE删除', 'Web开发基础', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (29, 3, 9, '如何有效记忆单词？', '结合语境记忆、使用间隔重复、多感官参与、联想记忆法', '英语学习方法', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (30, 3, 9, '什么是思维导图？', '一种可视化思维工具，以中心主题向外发散分支，帮助整理和关联信息', '学习工具', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (31, 3, NULL, '什么是刻意练习？', '有目的、有反馈的专注练习，核心要素：明确目标、即时反馈、突破舒适区', '学习方法论', 0, NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_knowledge_card` VALUES (32, 3, 11, '数组和链表的区别？', '数组：连续内存、O(1)随机访问、O(n)插入删除；链表：离散内存、O(n)访问、O(1)插入删除', NULL, 1, 2, 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (33, 3, 11, '栈和队列的区别？', '栈：后进先出(LIFO)，只能从栈顶操作；队列：先进先出(FIFO)，队尾入队首出', NULL, 1, 2, 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (34, 3, 11, '二叉搜索树的特点？', '左子树<根<右子树，中序遍历有序，平均查找O(log n)，最坏退化为O(n)', NULL, 1, 2, 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (35, 3, 11, '哈希表如何解决冲突？', '主要方法：链地址法（拉链法）、开放地址法（线性探测、二次探测、双重哈希）', NULL, 1, 2, 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (36, 3, 11, '红黑树的5个性质？', '1.节点红或黑 2.根黑 3.叶(nil)黑 4.红节点子必黑 5.任一节点到叶的黑色节点数相同', NULL, 1, 2, 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (37, 3, 10, 'Vue 3 中 ref 和 reactive 的区别？', 'ref：用于基本类型和对象，通过 .value 访问；reactive：仅用于对象类型，直接访问属性', 'Composition API 核心', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (38, 3, 10, '什么是虚拟 DOM？', '虚拟 DOM 是真实 DOM 的 JavaScript 对象表示，通过 diff 算法最小化 DOM 操作，提升性能', '前端性能优化', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (39, 3, 10, 'CSS Flexbox 的核心属性？', '容器：display:flex, flex-direction, justify-content, align-items；项目：flex-grow, flex-shrink, flex-basis', '布局基础', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (40, 3, 10, '什么是闭包（Closure）？', '函数与其词法环境的组合，内部函数可以访问外部函数的变量，即使外部函数已执行完毕', 'JS核心概念', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (41, 3, 11, 'Python 中列表和元组的区别？', '列表(list)可变，用[]；元组(tuple)不可变，用()；元组可作为字典键，性能更优', 'Python基础', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (42, 3, 11, 'Python 装饰器的原理？', '本质是高阶函数，接受一个函数作为参数并返回新函数，使用 @ 语法糖简化调用', 'Python进阶', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (43, 3, 11, 'Python GIL 是什么？', '全局解释器锁，同一时刻只允许一个线程执行Python字节码，影响多线程CPU密集型任务性能', '并发编程', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (44, 3, 11, 'Python 中深拷贝和浅拷贝的区别？', '浅拷贝：复制对象引用（copy.copy）；深拷贝：递归复制所有嵌套对象（copy.deepcopy）', '重要区别', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (45, 3, 12, 'elaborate', 'v. 详细阐述，精心制作；adj. 精心设计的。例：Could you elaborate on your proposal?', 'GRE词汇', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (46, 3, 12, 'pragmatic', 'adj. 务实的，实用主义的。例：We need a pragmatic approach to solve this problem.', 'GRE词汇', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (47, 3, 12, 'resilient', 'adj. 有弹性的，能迅速恢复的。例：Children are often more resilient than adults think.', '高频词汇', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (48, 3, 13, '快速排序的原理和复杂度？', '选取基准元素，将数组分为小于和大于基准的两部分，递归排序。平均O(n log n)，最坏O(n²)，空间O(log n)', NULL, 1, 10, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (49, 3, 13, '归并排序的原理和复杂度？', '分治法：将数组对半拆分，分别排序后合并。时间始终O(n log n)，空间O(n)，稳定排序', NULL, 1, 10, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (50, 3, 13, '动态规划的解题步骤？', '1.定义状态（dp数组含义） 2.推导状态转移方程 3.确定初始条件 4.确定遍历顺序 5.举例验证', NULL, 1, 10, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (51, 3, 13, 'BFS 和 DFS 的区别？', 'BFS：广度优先，用队列，适合最短路径；DFS：深度优先，用栈/递归，适合全排列、组合问题', NULL, 1, 10, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (52, 3, NULL, '什么是微服务架构？', '将单一应用拆分为多个小型服务，每个服务独立部署、独立运行、独立扩展，通过API通信。优点：独立部署、技术异构、故障隔离；缺点：分布式复杂性、数据一致性、运维成本', '架构设计笔记', 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (53, 3, NULL, 'RESTful API 设计最佳实践？', '1.使用名词复数作为资源路径 2.用HTTP动词表示操作 3.使用合适的状态码 4.版本控制 5.分页和过滤 6.HATEOAS', NULL, 0, NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_knowledge_card` VALUES (67, 1, NULL, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', NULL, 1, 1, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_knowledge_card` VALUES (68, 1, NULL, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', NULL, 1, 1, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_knowledge_card` VALUES (69, 1, NULL, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', NULL, 1, 1, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_knowledge_card` VALUES (70, 1, NULL, 'Java 中 final 关键字的作用？', '修饰类：不可继承；修饰方法：不可重写；修饰变量：常量，不可修改', NULL, 1, 1, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_knowledge_card` VALUES (71, 1, NULL, 'Java 中接口和抽象类的区别？', '接口：多实现、默认方法(JDK8)、无状态；抽象类：单继承、可有构造器和成员变量', NULL, 1, 1, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_knowledge_card` VALUES (72, 1, NULL, '什么是 Java 的垃圾回收机制？', 'JVM 自动回收不再被引用的对象，主要算法：标记-清除、复制、标记-整理', NULL, 1, 1, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_knowledge_card` VALUES (73, 1, NULL, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', NULL, 1, 1, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_knowledge_card` VALUES (74, 1, NULL, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', NULL, 1, 1, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_knowledge_card` VALUES (75, 1, NULL, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', NULL, 1, 1, 1, '2026-05-20 23:17:57', '2026-05-20 23:18:58');
INSERT INTO `t_knowledge_card` VALUES (76, 1, NULL, 'Java 中 final 关键字的作用？', '修饰类：不可继承；修饰方法：不可重写；修饰变量：常量，不可修改', NULL, 1, 1, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_knowledge_card` VALUES (77, 1, NULL, 'Java 中接口和抽象类的区别？', '接口：多实现、默认方法(JDK8)、无状态；抽象类：单继承、可有构造器和成员变量', NULL, 1, 1, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_knowledge_card` VALUES (78, 1, NULL, '什么是 Java 的垃圾回收机制？', 'JVM 自动回收不再被引用的对象，主要算法：标记-清除、复制、标记-整理', NULL, 1, 1, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_knowledge_card` VALUES (85, 3, NULL, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', NULL, 1, 1, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_knowledge_card` VALUES (86, 3, NULL, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', NULL, 1, 1, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_knowledge_card` VALUES (87, 3, NULL, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', NULL, 1, 1, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_knowledge_card` VALUES (88, 3, NULL, 'Java 中 final 关键字的作用？', '修饰类：不可继承；修饰方法：不可重写；修饰变量：常量，不可修改', NULL, 1, 1, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_knowledge_card` VALUES (89, 3, NULL, 'Java 中接口和抽象类的区别？', '接口：多实现、默认方法(JDK8)、无状态；抽象类：单继承、可有构造器和成员变量', NULL, 1, 1, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_knowledge_card` VALUES (90, 3, NULL, '什么是 Java 的垃圾回收机制？', 'JVM 自动回收不再被引用的对象，主要算法：标记-清除、复制、标记-整理', NULL, 1, 1, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');

-- ----------------------------
-- Table structure for t_plant
-- ----------------------------
DROP TABLE IF EXISTS `t_plant`;
CREATE TABLE `t_plant`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `card_id` bigint NOT NULL COMMENT '关联知识卡片（1:1）',
  `growth_stage` tinyint NOT NULL DEFAULT 1 COMMENT '生长阶段：1-种子，2-发芽，3-成长，4-开花，5-结果',
  `is_withered` tinyint NOT NULL DEFAULT 0 COMMENT '是否枯萎：0-否，1-是',
  `stage_success_count` int NOT NULL DEFAULT 0 COMMENT '当前阶段成功复习次数',
  `total_review_count` int NOT NULL DEFAULT 0 COMMENT '总复习次数',
  `review_round` int NOT NULL DEFAULT 0 COMMENT '当前艾宾浩斯复习轮次（0=未复习过）',
  `next_review_date` date NOT NULL COMMENT '下次应复习日期',
  `last_review_date` date NULL DEFAULT NULL COMMENT '最后复习日期',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_card_id`(`card_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_next_review_date`(`next_review_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '植物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_plant
-- ----------------------------
INSERT INTO `t_plant` VALUES (1, 1, 1, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (2, 1, 2, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (3, 1, 3, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (4, 1, 4, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (5, 1, 5, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (6, 1, 6, 3, 0, 0, 3, 3, '2026-05-27', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (7, 1, 7, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-19', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (8, 1, 8, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (9, 1, 9, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-19', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (10, 1, 10, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-18', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (11, 1, 11, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-17', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (12, 1, 12, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-16', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (13, 1, 13, 5, 0, 0, 5, 5, '2026-06-19', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (14, 1, 14, 5, 0, 0, 5, 5, '2026-06-19', '2026-05-19', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (15, 1, 15, 3, 1, 0, 3, 3, '2026-05-18', '2026-05-15', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (16, 1, 16, 2, 1, 0, 1, 1, '2026-05-17', '2026-05-14', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (17, 1, 17, 4, 1, 0, 4, 4, '2026-05-16', '2026-05-13', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (18, 1, 18, 2, 0, 0, 1, 1, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (19, 1, 19, 3, 0, 0, 2, 2, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (20, 1, 20, 1, 0, 0, 0, 0, '2026-05-21', NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (21, 2, 21, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (22, 2, 22, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (23, 2, 23, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (24, 2, 24, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (25, 2, 25, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (26, 2, 26, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-19', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (27, 2, 27, 2, 1, 0, 1, 1, '2026-05-18', '2026-05-15', 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (28, 2, 28, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:19:15', '2026-05-20 22:19:15');
INSERT INTO `t_plant` VALUES (29, 3, 29, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (30, 3, 30, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:19:15', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (31, 3, 31, 3, 1, 0, 2, 2, '2026-05-17', '2026-05-13', 0, '2026-05-20 22:19:15', '2026-05-20 23:39:57');
INSERT INTO `t_plant` VALUES (32, 3, 32, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:26:32', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (33, 3, 33, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-20', 0, '2026-05-20 22:26:32', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (34, 3, 34, 2, 0, 0, 1, 1, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (35, 3, 35, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-19', 0, '2026-05-20 22:26:32', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (36, 3, 36, 5, 0, 0, 5, 5, '2026-06-19', '2026-05-20', 0, '2026-05-20 22:26:32', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (37, 3, 37, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:42:14', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (38, 3, 38, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-20', 0, '2026-05-20 22:42:14', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (39, 3, 39, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-19', 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (40, 3, 40, 5, 0, 0, 5, 5, '2026-06-19', '2026-05-20', 0, '2026-05-20 22:42:14', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (41, 3, 41, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:42:14', '2026-05-20 23:39:57');
INSERT INTO `t_plant` VALUES (42, 3, 42, 2, 0, 0, 1, 1, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (43, 3, 43, 3, 0, 0, 2, 2, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (44, 3, 44, 4, 0, 0, 4, 4, '2026-06-04', '2026-05-18', 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (45, 3, 45, 2, 0, 0, 1, 1, '2026-05-22', '2026-05-20', 0, '2026-05-20 22:42:14', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (46, 3, 46, 3, 1, 0, 2, 2, '2026-05-17', '2026-05-14', 0, '2026-05-20 22:42:14', '2026-05-20 23:39:57');
INSERT INTO `t_plant` VALUES (47, 3, 47, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (48, 3, 48, 1, 0, 0, 0, 0, '2026-05-20', NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (49, 3, 49, 2, 0, 0, 1, 1, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (50, 3, 50, 3, 0, 0, 2, 2, '2026-05-24', '2026-05-20', 0, '2026-05-20 22:42:14', '2026-05-20 23:41:35');
INSERT INTO `t_plant` VALUES (51, 3, 51, 4, 0, 0, 5, 5, '2026-05-27', '2026-05-12', 0, '2026-05-20 22:42:14', '2026-05-20 22:45:31');
INSERT INTO `t_plant` VALUES (52, 3, 52, 3, 0, 0, 2, 2, '2026-05-20', '2026-05-18', 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (53, 3, 53, 1, 0, 0, 0, 0, '2026-05-21', NULL, 0, '2026-05-20 22:42:14', '2026-05-20 22:42:14');
INSERT INTO `t_plant` VALUES (67, 1, 67, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_plant` VALUES (68, 1, 68, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_plant` VALUES (69, 1, 69, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_plant` VALUES (70, 1, 70, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_plant` VALUES (71, 1, 71, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_plant` VALUES (72, 1, 72, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:54', '2026-05-20 23:17:54');
INSERT INTO `t_plant` VALUES (73, 1, 73, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_plant` VALUES (74, 1, 74, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_plant` VALUES (75, 1, 75, 1, 0, 0, 0, 1, '2026-05-20', NULL, 1, '2026-05-20 23:17:57', '2026-05-20 23:18:58');
INSERT INTO `t_plant` VALUES (76, 1, 76, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_plant` VALUES (77, 1, 77, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_plant` VALUES (78, 1, 78, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:17:57', '2026-05-20 23:17:57');
INSERT INTO `t_plant` VALUES (85, 3, 85, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_plant` VALUES (86, 3, 86, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_plant` VALUES (87, 3, 87, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_plant` VALUES (88, 3, 88, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_plant` VALUES (89, 3, 89, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');
INSERT INTO `t_plant` VALUES (90, 3, 90, 1, 0, 0, 0, 1, '2026-05-20', NULL, 0, '2026-05-20 23:44:50', '2026-05-20 23:44:50');

-- ----------------------------
-- Table structure for t_review_record
-- ----------------------------
DROP TABLE IF EXISTS `t_review_record`;
CREATE TABLE `t_review_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `card_id` bigint NOT NULL COMMENT '关联知识卡片',
  `plant_id` bigint NOT NULL COMMENT '关联植物',
  `self_evaluation` tinyint NOT NULL COMMENT '自评：1-记住了，2-模糊，3-忘记了',
  `prev_stage` tinyint NULL DEFAULT NULL COMMENT '复习前生长阶段',
  `after_stage` tinyint NULL DEFAULT NULL COMMENT '复习后生长阶段',
  `prev_round` int NULL DEFAULT NULL COMMENT '复习前复习轮次',
  `after_round` int NULL DEFAULT NULL COMMENT '复习后复习轮次',
  `was_withered` tinyint NOT NULL DEFAULT 0 COMMENT '复习时是否处于枯萎状态',
  `scheduled_date` date NULL DEFAULT NULL COMMENT '原计划复习日期',
  `actual_date` date NOT NULL COMMENT '实际复习日期',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_card_id`(`card_id` ASC) USING BTREE,
  INDEX `idx_actual_date`(`actual_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 119 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '复习记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_review_record
-- ----------------------------
INSERT INTO `t_review_record` VALUES (1, 1, 3, 3, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (2, 1, 4, 4, 2, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (3, 1, 5, 5, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (4, 1, 5, 5, 1, 2, 3, 1, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (5, 1, 6, 6, 1, 1, 2, 0, 1, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (6, 1, 6, 6, 2, 2, 3, 1, 2, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (7, 1, 6, 6, 1, 3, 3, 2, 3, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (8, 1, 7, 7, 1, 1, 2, 0, 1, 0, '2026-05-17', '2026-05-17', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (9, 1, 7, 7, 3, 2, 1, 1, 0, 0, '2026-05-19', '2026-05-19', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (10, 1, 7, 7, 1, 1, 3, 0, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (11, 1, 8, 8, 1, 1, 2, 0, 1, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (12, 1, 8, 8, 1, 2, 3, 1, 2, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (13, 1, 8, 8, 1, 3, 4, 2, 3, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (14, 1, 8, 8, 1, 4, 4, 3, 4, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (15, 1, 9, 9, 1, 1, 2, 0, 1, 0, '2026-05-09', '2026-05-09', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (16, 1, 9, 9, 1, 2, 3, 1, 2, 0, '2026-05-11', '2026-05-11', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (17, 1, 9, 9, 1, 3, 4, 2, 3, 0, '2026-05-15', '2026-05-15', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (18, 1, 9, 9, 2, 4, 4, 3, 4, 0, '2026-05-19', '2026-05-19', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (19, 1, 10, 10, 1, 1, 2, 0, 1, 0, '2026-05-08', '2026-05-08', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (20, 1, 10, 10, 1, 2, 3, 1, 2, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (21, 1, 10, 10, 1, 3, 4, 2, 3, 0, '2026-05-14', '2026-05-14', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (22, 1, 10, 10, 1, 4, 4, 3, 4, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (23, 1, 11, 11, 1, 1, 2, 0, 1, 0, '2026-05-07', '2026-05-07', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (24, 1, 11, 11, 1, 2, 3, 1, 2, 0, '2026-05-09', '2026-05-09', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (25, 1, 11, 11, 1, 3, 4, 2, 3, 0, '2026-05-13', '2026-05-13', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (26, 1, 11, 11, 1, 4, 4, 3, 4, 0, '2026-05-17', '2026-05-17', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (27, 1, 12, 12, 1, 1, 2, 0, 1, 0, '2026-05-06', '2026-05-06', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (28, 1, 12, 12, 1, 2, 3, 1, 2, 0, '2026-05-08', '2026-05-08', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (29, 1, 12, 12, 1, 3, 4, 2, 3, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (30, 1, 12, 12, 1, 4, 4, 3, 4, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (31, 1, 13, 13, 1, 1, 2, 0, 1, 0, '2026-05-05', '2026-05-05', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (32, 1, 13, 13, 1, 2, 3, 1, 2, 0, '2026-05-07', '2026-05-07', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (33, 1, 13, 13, 1, 3, 4, 2, 3, 0, '2026-05-11', '2026-05-11', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (34, 1, 13, 13, 1, 4, 4, 3, 4, 0, '2026-05-15', '2026-05-15', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (35, 1, 13, 13, 1, 4, 5, 4, 5, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (36, 1, 14, 14, 1, 1, 2, 0, 1, 0, '2026-05-04', '2026-05-04', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (37, 1, 14, 14, 1, 2, 3, 1, 2, 0, '2026-05-06', '2026-05-06', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (38, 1, 14, 14, 1, 3, 4, 2, 3, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (39, 1, 14, 14, 2, 4, 4, 3, 4, 0, '2026-05-14', '2026-05-14', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (40, 1, 14, 14, 1, 4, 5, 4, 5, 0, '2026-05-19', '2026-05-19', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (41, 1, 15, 15, 1, 1, 2, 0, 1, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (42, 1, 15, 15, 1, 2, 3, 1, 2, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (43, 1, 15, 15, 3, 3, 3, 2, 3, 0, '2026-05-15', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (44, 1, 16, 16, 1, 1, 2, 0, 1, 0, '2026-05-13', '2026-05-13', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (45, 1, 16, 16, 3, 2, 2, 1, 1, 0, '2026-05-15', '2026-05-17', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (46, 1, 17, 17, 1, 1, 2, 0, 1, 0, '2026-05-08', '2026-05-08', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (47, 1, 17, 17, 1, 2, 3, 1, 2, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (48, 1, 17, 17, 1, 3, 4, 2, 3, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (49, 1, 17, 17, 3, 4, 4, 3, 4, 0, '2026-05-14', '2026-05-16', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (50, 2, 21, 21, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (51, 2, 22, 22, 2, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (52, 2, 24, 24, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (53, 2, 24, 24, 1, 2, 3, 1, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (54, 2, 25, 25, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (55, 2, 25, 25, 2, 2, 3, 1, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (56, 2, 26, 26, 1, 1, 2, 0, 1, 0, '2026-05-11', '2026-05-11', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (57, 2, 26, 26, 1, 2, 3, 1, 2, 0, '2026-05-13', '2026-05-13', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (58, 2, 26, 26, 1, 3, 4, 2, 3, 0, '2026-05-17', '2026-05-17', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (59, 2, 26, 26, 1, 4, 4, 3, 4, 0, '2026-05-19', '2026-05-19', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (60, 2, 27, 27, 1, 1, 2, 0, 1, 0, '2026-05-13', '2026-05-13', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (61, 2, 27, 27, 3, 2, 2, 1, 1, 0, '2026-05-15', '2026-05-18', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (62, 3, 30, 30, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:19:15');
INSERT INTO `t_review_record` VALUES (63, 3, 31, 31, 2, 1, 1, 0, 0, 1, '2026-05-15', '2026-05-20', 0, '2026-05-20 22:19:51');
INSERT INTO `t_review_record` VALUES (64, 3, 29, 29, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (65, 3, 31, 31, 1, 1, 2, 0, 1, 0, '2026-05-11', '2026-05-11', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (66, 3, 31, 31, 3, 2, 3, 1, 2, 0, '2026-05-13', '2026-05-16', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (67, 3, 32, 32, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (68, 3, 33, 33, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (69, 3, 33, 33, 1, 2, 3, 1, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (70, 3, 34, 34, 2, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (71, 3, 35, 35, 1, 1, 2, 0, 1, 0, '2026-05-11', '2026-05-11', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (72, 3, 35, 35, 1, 2, 3, 1, 2, 0, '2026-05-13', '2026-05-13', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (73, 3, 35, 35, 1, 3, 4, 2, 3, 0, '2026-05-17', '2026-05-17', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (74, 3, 35, 35, 1, 4, 4, 3, 4, 0, '2026-05-19', '2026-05-19', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (75, 3, 36, 36, 1, 1, 2, 0, 1, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (76, 3, 36, 36, 1, 2, 3, 1, 2, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (77, 3, 36, 36, 2, 3, 4, 2, 3, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (78, 3, 36, 36, 1, 4, 4, 3, 4, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (79, 3, 36, 36, 1, 4, 5, 4, 5, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (80, 3, 37, 37, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (81, 3, 38, 38, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (82, 3, 38, 38, 1, 2, 3, 1, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (83, 3, 39, 39, 1, 1, 2, 0, 1, 0, '2026-05-11', '2026-05-11', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (84, 3, 39, 39, 1, 2, 3, 1, 2, 0, '2026-05-13', '2026-05-13', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (85, 3, 39, 39, 2, 3, 4, 2, 3, 0, '2026-05-17', '2026-05-17', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (86, 3, 39, 39, 1, 4, 4, 3, 4, 0, '2026-05-19', '2026-05-19', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (87, 3, 40, 40, 1, 1, 2, 0, 1, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (88, 3, 40, 40, 1, 2, 3, 1, 2, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (89, 3, 40, 40, 1, 3, 4, 2, 3, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (90, 3, 40, 40, 1, 4, 4, 3, 4, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (91, 3, 40, 40, 1, 4, 5, 4, 5, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (92, 3, 42, 42, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (93, 3, 43, 43, 1, 1, 2, 0, 1, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (94, 3, 43, 43, 3, 2, 1, 1, 0, 0, '2026-05-18', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (95, 3, 44, 44, 1, 1, 2, 0, 1, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (96, 3, 44, 44, 1, 2, 3, 1, 2, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (97, 3, 44, 44, 1, 3, 4, 2, 3, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (98, 3, 44, 44, 1, 4, 4, 3, 4, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (99, 3, 45, 45, 1, 1, 2, 0, 1, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (100, 3, 46, 46, 1, 1, 2, 0, 1, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (101, 3, 46, 46, 1, 2, 3, 1, 2, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (102, 3, 46, 46, 3, 3, 3, 2, 2, 0, '2026-05-14', '2026-05-17', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (103, 3, 49, 49, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (104, 3, 50, 50, 1, 1, 2, 0, 1, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (105, 3, 50, 50, 1, 2, 3, 1, 2, 0, '2026-05-20', '2026-05-20', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (106, 3, 51, 51, 1, 1, 2, 0, 1, 0, '2026-05-08', '2026-05-08', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (107, 3, 51, 51, 1, 2, 3, 1, 2, 0, '2026-05-10', '2026-05-10', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (108, 3, 51, 51, 1, 3, 4, 2, 3, 0, '2026-05-12', '2026-05-12', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (109, 3, 51, 51, 3, 4, 4, 3, 4, 0, '2026-05-14', '2026-05-15', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (110, 3, 52, 52, 1, 1, 2, 0, 1, 0, '2026-05-16', '2026-05-16', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (111, 3, 52, 52, 2, 2, 3, 1, 2, 0, '2026-05-18', '2026-05-18', 0, '2026-05-20 22:42:14');
INSERT INTO `t_review_record` VALUES (112, 3, 51, 51, 1, 4, 4, 4, 5, 1, '2026-05-15', '2026-05-20', 0, '2026-05-20 22:45:31');

-- ----------------------------
-- Table structure for t_study_pack
-- ----------------------------
DROP TABLE IF EXISTS `t_study_pack`;
CREATE TABLE `t_study_pack`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '知识包名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '知识包描述',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '建议分类名称',
  `card_count` int NOT NULL DEFAULT 0 COMMENT '包含卡片数',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预设知识包表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_study_pack
-- ----------------------------
INSERT INTO `t_study_pack` VALUES (1, 'Java 基础', 'Java 编程语言核心概念，适合初学者快速入门', 'Java', 6, 0, '2026-05-20 21:06:50', '2026-05-20 21:06:50');
INSERT INTO `t_study_pack` VALUES (2, '数据结构基础', '常见数据结构的核心概念与时间复杂度', '数据结构', 5, 0, '2026-05-20 21:06:50', '2026-05-20 21:06:50');
INSERT INTO `t_study_pack` VALUES (3, 'Spring Boot 核心', 'Spring Boot 框架核心知识点', 'Spring Boot', 5, 0, '2026-05-20 21:06:50', '2026-05-20 21:06:50');
INSERT INTO `t_study_pack` VALUES (4, '计算机网络', '计算机网络核心协议与分层模型，面试高频考点', '计算机网络', 6, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');
INSERT INTO `t_study_pack` VALUES (5, '操作系统', '操作系统核心概念：进程管理、内存管理、文件系统', '操作系统', 5, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');
INSERT INTO `t_study_pack` VALUES (6, '数据库基础', '关系型数据库核心知识：SQL、索引、事务、优化', '数据库', 6, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');
INSERT INTO `t_study_pack` VALUES (7, '设计模式', '常用设计模式的核心思想与应用场景', '设计模式', 5, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');
INSERT INTO `t_study_pack` VALUES (8, '英语四级高频词汇', 'CET-4 考试高频核心词汇，含释义与例句', '英语', 6, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');
INSERT INTO `t_study_pack` VALUES (9, 'Git 版本控制', 'Git 核心概念与常用命令，开发者必备技能', 'Git', 5, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');
INSERT INTO `t_study_pack` VALUES (10, '算法与复杂度', '常见排序算法与时间空间复杂度分析', '算法', 5, 0, '2026-05-20 22:34:22', '2026-05-20 22:34:22');

-- ----------------------------
-- Table structure for t_study_pack_import
-- ----------------------------
DROP TABLE IF EXISTS `t_study_pack_import`;
CREATE TABLE `t_study_pack_import`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户',
  `pack_id` bigint NOT NULL COMMENT '知识包',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '导入时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_pack_id`(`pack_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户导入记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_study_pack_import
-- ----------------------------
INSERT INTO `t_study_pack_import` VALUES (1, 1, 1, 0, '2026-05-20 22:19:15');
INSERT INTO `t_study_pack_import` VALUES (2, 2, 1, 0, '2026-05-20 22:19:15');
INSERT INTO `t_study_pack_import` VALUES (3, 2, 2, 0, '2026-05-20 22:19:15');
INSERT INTO `t_study_pack_import` VALUES (4, 2, 3, 0, '2026-05-20 22:19:15');
INSERT INTO `t_study_pack_import` VALUES (5, 3, 2, 0, '2026-05-20 22:42:14');
INSERT INTO `t_study_pack_import` VALUES (6, 3, 10, 0, '2026-05-20 22:42:14');
INSERT INTO `t_study_pack_import` VALUES (8, 3, 1, 0, '2026-05-20 23:44:50');

-- ----------------------------
-- Table structure for t_study_pack_item
-- ----------------------------
DROP TABLE IF EXISTS `t_study_pack_item`;
CREATE TABLE `t_study_pack_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pack_id` bigint NOT NULL COMMENT '所属知识包',
  `front_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '正面内容',
  `back_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '背面内容',
  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序序号',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pack_id`(`pack_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识包条目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_study_pack_item
-- ----------------------------
INSERT INTO `t_study_pack_item` VALUES (1, 1, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', NULL, 1, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (2, 1, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', NULL, 2, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (3, 1, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', NULL, 3, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (4, 1, 'Java 中 final 关键字的作用？', '修饰类：不可继承；修饰方法：不可重写；修饰变量：常量，不可修改', NULL, 4, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (5, 1, 'Java 中接口和抽象类的区别？', '接口：多实现、默认方法(JDK8)、无状态；抽象类：单继承、可有构造器和成员变量', NULL, 5, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (6, 1, '什么是 Java 的垃圾回收机制？', 'JVM 自动回收不再被引用的对象，主要算法：标记-清除、复制、标记-整理', NULL, 6, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (7, 2, '数组和链表的区别？', '数组：连续内存、O(1)随机访问、O(n)插入删除；链表：离散内存、O(n)访问、O(1)插入删除', NULL, 1, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (8, 2, '栈和队列的区别？', '栈：后进先出(LIFO)，只能从栈顶操作；队列：先进先出(FIFO)，队尾入队首出', NULL, 2, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (9, 2, '二叉搜索树的特点？', '左子树<根<右子树，中序遍历有序，平均查找O(log n)，最坏退化为O(n)', NULL, 3, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (10, 2, '哈希表如何解决冲突？', '主要方法：链地址法（拉链法）、开放地址法（线性探测、二次探测、双重哈希）', NULL, 4, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (11, 2, '红黑树的5个性质？', '1.节点红或黑 2.根黑 3.叶(nil)黑 4.红节点子必黑 5.任一节点到叶的黑色节点数相同', NULL, 5, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (12, 3, 'Spring Boot 自动配置原理？', '@EnableAutoConfiguration → SpringFactoriesLoader 加载 META-INF/spring.factories → 条件注解(@Conditional)筛选配置类', NULL, 1, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (13, 3, 'Spring Bean 的生命周期？', '实例化→属性赋值→Aware回调→BeanPostProcessor前置→初始化→BeanPostProcessor后置→使用→销毁', NULL, 2, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (14, 3, 'Spring AOP 的实现方式？', 'JDK动态代理（接口）和CGLIB代理（类），默认有接口用JDK，无接口用CGLIB', NULL, 3, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (15, 3, '@Transactional 失效的场景？', '1.方法非public 2.自调用 3.异常被catch 4.默认只回滚RuntimeException 5.数据库引擎不支持事务', NULL, 4, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (16, 3, 'Spring Boot Starter 的原理？', 'Starter包含自动配置模块，通过spring.factories注册配置类，条件注解控制Bean创建', NULL, 5, 0, '2026-05-20 21:06:50');
INSERT INTO `t_study_pack_item` VALUES (17, 4, 'TCP 三次握手的过程？', '1.客户端发送SYN 2.服务端回复SYN+ACK 3.客户端发送ACK，连接建立', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (18, 4, 'TCP 四次挥手的过程？', '1.主动方发送FIN 2.被动方回复ACK 3.被动方发送FIN 4.主动方回复ACK，等待2MSL后关闭', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (19, 4, 'HTTP 和 HTTPS 的区别？', 'HTTPS = HTTP + SSL/TLS加密，默认端口443（HTTP为80），HTTPS需要CA证书，数据传输加密', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (20, 4, 'GET 和 POST 的区别？', 'GET：参数在URL中、有长度限制、可缓存、幂等；POST：参数在请求体、无长度限制、不可缓存、非幂等', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (21, 4, '什么是 DNS？解析过程是怎样的？', '域名系统，将域名解析为IP地址。过程：浏览器缓存→系统缓存→路由器缓存→ISP DNS→根域名服务器→顶级域名服务器→权威域名服务器', NULL, 5, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (22, 4, 'OSI 七层模型和 TCP/IP 四层模型的对应关系？', 'OSI七层：物理/数据链路/网络/传输/会话/表示/应用；TCP/IP四层：网络接口/网际/传输/应用', NULL, 6, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (23, 5, '进程和线程的区别？', '进程是资源分配的基本单位，线程是CPU调度的基本单位；进程有独立地址空间，线程共享进程资源', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (24, 5, '什么是死锁？产生条件？', '死锁：多个进程互相等待对方释放资源。四个必要条件：互斥、占有并等待、不可抢占、循环等待', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (25, 5, '虚拟内存的原理？', '将程序使用的地址空间与物理内存分离，通过页表映射，使用磁盘作为扩展内存，按需调页', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (26, 5, '常见的页面置换算法？', 'FIFO（先进先出）、LRU（最近最少使用）、LFU（最不经常使用）、Clock（时钟算法）', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (27, 5, '进程间通信（IPC）的方式？', '管道、命名管道、消息队列、共享内存、信号量、信号、Socket', NULL, 5, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (28, 6, '数据库事务的 ACID 特性？', '原子性(Atomicity)：全部成功或全部回滚；一致性(Consistency)：数据从一个一致状态到另一个；隔离性(Isolation)：事务间互不干扰；持久性(Durability)：提交后永久保存', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (29, 6, 'MySQL 的索引类型有哪些？', '主键索引、唯一索引、普通索引、组合索引、全文索引；按结构分：B+Tree索引、Hash索引、R-Tree索引', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (30, 6, '什么是索引覆盖？', '查询所需的所有列都在索引中，无需回表查询数据行，提高查询效率', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (31, 6, 'MySQL 的隔离级别？', 'READ UNCOMMITTED（脏读）、READ COMMITTED（不可重复读）、REPEATABLE READ（幻读，MySQL默认）、SERIALIZABLE（串行化）', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (32, 6, '什么是 MVCC？', '多版本并发控制，通过隐藏列（事务ID、回滚指针）和Undo Log实现快照读，解决读写冲突', NULL, 5, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (33, 6, 'SQL 优化的常见手段？', '1.合理建索引 2.避免SELECT * 3.使用EXPLAIN分析 4.避免索引失效（函数、隐式转换、OR） 5.分页优化 6.避免大事务', NULL, 6, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (34, 7, '单例模式的实现方式？', '饿汉式（类加载时创建）、懒汉式（延迟创建+双重检查锁）、静态内部类、枚举（最佳实践）', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (35, 7, '工厂方法模式 vs 抽象工厂模式？', '工厂方法：一个工厂接口对应一个产品等级；抽象工厂：一个工厂接口对应多个产品等级（产品族）', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (36, 7, '观察者模式的原理？', '定义对象间一对多的依赖关系，当一个对象状态改变时，所有依赖它的对象都会收到通知并自动更新', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (37, 7, '策略模式的应用场景？', '需要在不同算法/策略间灵活切换时，如支付方式选择、排序算法选择、折扣计算策略', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (38, 7, '装饰器模式 vs 继承？', '装饰器：动态扩展功能，比继承更灵活，避免类爆炸；继承：静态扩展，编译时确定，类层次膨胀', NULL, 5, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (39, 8, 'inevitable', 'adj. 不可避免的，必然发生的。例：Change is inevitable in a growing company.', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (40, 8, 'substantial', 'adj. 大量的，实质的。例：We need substantial evidence to support this claim.', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (41, 8, 'comprehensive', 'adj. 全面的，综合的。例：The report provides a comprehensive analysis of the market.', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (42, 8, 'demonstrate', 'v. 证明，演示。例：The experiment demonstrates the effectiveness of the new drug.', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (43, 8, 'perspective', 'n. 视角，观点。例：Try to see the problem from a different perspective.', NULL, 5, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (44, 8, 'constitute', 'v. 构成，组成。例：Women constitute over 50% of the population.', NULL, 6, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (45, 9, 'git merge 和 git rebase 的区别？', 'merge：保留完整分支历史，创建合并提交；rebase：将提交重新应用到目标分支，历史线性更清晰，但改写提交历史', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (46, 9, 'git reset 的三种模式？', '--soft：只移动HEAD，保留暂存区和工作区；--mixed（默认）：移动HEAD，重置暂存区，保留工作区；--hard：全部重置', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (47, 9, '什么是 git stash？', '临时保存未提交的修改，将工作区恢复到干净状态。git stash pop 恢复最近一次保存', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (48, 9, 'git cherry-pick 的作用？', '将指定提交应用到当前分支，用于从其他分支挑选特定修改，而不合并整个分支', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (49, 9, '如何解决合并冲突？', '1.git status查看冲突文件 2.手动编辑冲突区域（保留需要的代码） 3.git add标记已解决 4.git commit完成合并', NULL, 5, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (50, 10, '快速排序的原理和复杂度？', '选取基准元素，将数组分为小于和大于基准的两部分，递归排序。平均O(n log n)，最坏O(n²)，空间O(log n)', NULL, 1, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (51, 10, '归并排序的原理和复杂度？', '分治法：将数组对半拆分，分别排序后合并。时间始终O(n log n)，空间O(n)，稳定排序', NULL, 2, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (52, 10, '动态规划的解题步骤？', '1.定义状态（dp数组含义） 2.推导状态转移方程 3.确定初始条件 4.确定遍历顺序 5.举例验证', NULL, 3, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (53, 10, 'BFS 和 DFS 的区别？', 'BFS：广度优先，用队列，适合最短路径；DFS：深度优先，用栈/递归，适合全排列、组合问题', NULL, 4, 0, '2026-05-20 22:34:22');
INSERT INTO `t_study_pack_item` VALUES (54, 10, '什么是贪心算法？', '每步选择局部最优解，期望得到全局最优。适用条件：贪心选择性质+最优子结构。如：活动选择、哈夫曼编码', NULL, 5, 0, '2026-05-20 22:34:22');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt 加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像 URL',
  `current_streak` int NOT NULL DEFAULT 0 COMMENT '当前连续打卡天数',
  `max_streak` int NOT NULL DEFAULT 0 COMMENT '历史最长连续天数',
  `last_check_in` date NULL DEFAULT NULL COMMENT '最后打卡日期',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'testuser01', '$2a$10$an60kXs0l.Si0TYmbF.GU.kbbhhVYsfzWlG0uc7bpXqHbKy5kpOem', 'test01', NULL, 10, 10, '2026-05-20', 1, 0, '2026-05-20 21:31:15', '2026-05-20 22:35:59');
INSERT INTO `t_user` VALUES (3, 'lujun', '$2a$10$DKPYTnXd1sU9y9HgrRvVo.387u4wW87./TK3fYzHWtvqU1mgfEvpW', 'lujun', NULL, 15, 15, '2026-05-20', 1, 0, '2026-05-20 21:33:47', '2026-05-20 23:34:55');

-- ----------------------------
-- Table structure for t_user_badge
-- ----------------------------
DROP TABLE IF EXISTS `t_user_badge`;
CREATE TABLE `t_user_badge`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户',
  `badge_id` bigint NOT NULL COMMENT '徽章',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除，1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_badge`(`user_id` ASC, `badge_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户徽章关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_badge
-- ----------------------------
INSERT INTO `t_user_badge` VALUES (1, 1, 1, 0, '2026-05-20 22:19:15');
INSERT INTO `t_user_badge` VALUES (2, 1, 3, 0, '2026-05-20 22:19:15');
INSERT INTO `t_user_badge` VALUES (3, 1, 4, 0, '2026-05-20 22:19:15');
INSERT INTO `t_user_badge` VALUES (4, 1, 7, 0, '2026-05-20 22:19:15');
INSERT INTO `t_user_badge` VALUES (5, 2, 1, 0, '2026-05-20 22:19:15');
INSERT INTO `t_user_badge` VALUES (6, 3, 1, 0, '2026-05-20 22:42:14');
INSERT INTO `t_user_badge` VALUES (7, 3, 4, 0, '2026-05-20 22:42:14');
INSERT INTO `t_user_badge` VALUES (8, 3, 7, 0, '2026-05-20 22:42:14');
INSERT INTO `t_user_badge` VALUES (9, 3, 5, 0, '2026-05-20 22:42:14');

SET FOREIGN_KEY_CHECKS = 1;
