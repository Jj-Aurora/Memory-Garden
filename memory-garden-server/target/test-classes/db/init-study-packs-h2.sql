-- ============================================================
-- 记忆花园系统 - 预设知识包初始数据 (H2 兼容)
-- @author jLU
-- @date 2026-05-20
-- ============================================================

-- 知识包1：Java 基础
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(1, 'Java 基础', 'Java 编程语言核心概念，适合初学者快速入门', 'Java', 6);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(1, 'Java 中 == 和 equals() 的区别？', '== 比较引用地址（基本类型比较值）；equals() 比较对象内容，需重写 hashCode 和 equals', 1),
(1, 'Java 中 HashMap 的底层实现？', 'JDK1.8 采用数组+链表+红黑树，链表长度≥8且数组长度≥64时转红黑树', 2),
(1, '什么是 Java 的自动装箱和拆箱？', '自动装箱：基本类型→包装类（Integer.valueOf）；拆箱：包装类→基本类型（intValue）', 3),
(1, 'Java 中 final 关键字的作用？', '修饰类：不可继承；修饰方法：不可重写；修饰变量：常量，不可修改', 4),
(1, 'Java 中接口和抽象类的区别？', '接口：多实现、默认方法(JDK8)、无状态；抽象类：单继承、可有构造器和成员变量', 5),
(1, '什么是 Java 的垃圾回收机制？', 'JVM 自动回收不再被引用的对象，主要算法：标记-清除、复制、标记-整理', 6);

-- 知识包2：数据结构
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(2, '数据结构基础', '常见数据结构的核心概念与时间复杂度', '数据结构', 5);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(2, '数组和链表的区别？', '数组：连续内存、O(1)随机访问、O(n)插入删除；链表：离散内存、O(n)访问、O(1)插入删除', 1),
(2, '栈和队列的区别？', '栈：后进先出(LIFO)，只能从栈顶操作；队列：先进先出(FIFO)，队尾入队首出', 2),
(2, '二叉搜索树的特点？', '左子树<根<右子树，中序遍历有序，平均查找O(log n)，最坏退化为O(n)', 3),
(2, '哈希表如何解决冲突？', '主要方法：链地址法（拉链法）、开放地址法（线性探测、二次探测、双重哈希）', 4),
(2, '红黑树的5个性质？', '1.节点红或黑 2.根黑 3.叶(nil)黑 4.红节点子必黑 5.任一节点到叶的黑色节点数相同', 5);

-- 知识包3：Spring Boot
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(3, 'Spring Boot 核心', 'Spring Boot 框架核心知识点', 'Spring Boot', 5);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(3, 'Spring Boot 自动配置原理？', '@EnableAutoConfiguration → SpringFactoriesLoader 加载 META-INF/spring.factories → 条件注解(@Conditional)筛选配置类', 1),
(3, 'Spring Bean 的生命周期？', '实例化→属性赋值→Aware回调→BeanPostProcessor前置→初始化→BeanPostProcessor后置→使用→销毁', 2),
(3, 'Spring AOP 的实现方式？', 'JDK动态代理（接口）和CGLIB代理（类），默认有接口用JDK，无接口用CGLIB', 3),
(3, '@Transactional 失效的场景？', '1.方法非public 2.自调用 3.异常被catch 4.默认只回滚RuntimeException 5.数据库引擎不支持事务', 4),
(3, 'Spring Boot Starter 的原理？', 'Starter包含自动配置模块，通过spring.factories注册配置类，条件注解控制Bean创建', 5);

-- 知识包4：计算机网络
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(4, '计算机网络', '计算机网络核心协议与分层模型，面试高频考点', '计算机网络', 6);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(4, 'TCP 三次握手的过程？', '1.客户端发送SYN 2.服务端回复SYN+ACK 3.客户端发送ACK，连接建立', 1),
(4, 'TCP 四次挥手的过程？', '1.主动方发送FIN 2.被动方回复ACK 3.被动方发送FIN 4.主动方回复ACK，等待2MSL后关闭', 2),
(4, 'HTTP 和 HTTPS 的区别？', 'HTTPS = HTTP + SSL/TLS加密，默认端口443（HTTP为80），HTTPS需要CA证书，数据传输加密', 3),
(4, 'GET 和 POST 的区别？', 'GET：参数在URL中、有长度限制、可缓存、幂等；POST：参数在请求体、无长度限制、不可缓存、非幂等', 4),
(4, '什么是 DNS？解析过程是怎样的？', '域名系统，将域名解析为IP地址。过程：浏览器缓存→系统缓存→路由器缓存→ISP DNS→根域名服务器→顶级域名服务器→权威域名服务器', 5),
(4, 'OSI 七层模型和 TCP/IP 四层模型的对应关系？', 'OSI七层：物理/数据链路/网络/传输/会话/表示/应用；TCP/IP四层：网络接口/网际/传输/应用', 6);

-- 知识包5：操作系统
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(5, '操作系统', '操作系统核心概念：进程管理、内存管理、文件系统', '操作系统', 5);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(5, '进程和线程的区别？', '进程是资源分配的基本单位，线程是CPU调度的基本单位；进程有独立地址空间，线程共享进程资源', 1),
(5, '什么是死锁？产生条件？', '死锁：多个进程互相等待对方释放资源。四个必要条件：互斥、占有并等待、不可抢占、循环等待', 2),
(5, '虚拟内存的原理？', '将程序使用的地址空间与物理内存分离，通过页表映射，使用磁盘作为扩展内存，按需调页', 3),
(5, '常见的页面置换算法？', 'FIFO（先进先出）、LRU（最近最少使用）、LFU（最不经常使用）、Clock（时钟算法）', 4),
(5, '进程间通信（IPC）的方式？', '管道、命名管道、消息队列、共享内存、信号量、信号、Socket', 5);

-- 知识包6：数据库
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(6, '数据库基础', '关系型数据库核心知识：SQL、索引、事务、优化', '数据库', 6);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(6, '数据库事务的 ACID 特性？', '原子性(Atomicity)：全部成功或全部回滚；一致性(Consistency)：数据从一个一致状态到另一个；隔离性(Isolation)：事务间互不干扰；持久性(Durability)：提交后永久保存', 1),
(6, 'MySQL 的索引类型有哪些？', '主键索引、唯一索引、普通索引、组合索引、全文索引；按结构分：B+Tree索引、Hash索引、R-Tree索引', 2),
(6, '什么是索引覆盖？', '查询所需的所有列都在索引中，无需回表查询数据行，提高查询效率', 3),
(6, 'MySQL 的隔离级别？', 'READ UNCOMMITTED（脏读）、READ COMMITTED（不可重复读）、REPEATABLE READ（幻读，MySQL默认）、SERIALIZABLE（串行化）', 4),
(6, '什么是 MVCC？', '多版本并发控制，通过隐藏列（事务ID、回滚指针）和Undo Log实现快照读，解决读写冲突', 5),
(6, 'SQL 优化的常见手段？', '1.合理建索引 2.避免SELECT * 3.使用EXPLAIN分析 4.避免索引失效（函数、隐式转换、OR） 5.分页优化 6.避免大事务', 6);

-- 知识包7：设计模式
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(7, '设计模式', '常用设计模式的核心思想与应用场景', '设计模式', 5);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(7, '单例模式的实现方式？', '饿汉式（类加载时创建）、懒汉式（延迟创建+双重检查锁）、静态内部类、枚举（最佳实践）', 1),
(7, '工厂方法模式 vs 抽象工厂模式？', '工厂方法：一个工厂接口对应一个产品等级；抽象工厂：一个工厂接口对应多个产品等级（产品族）', 2),
(7, '观察者模式的原理？', '定义对象间一对多的依赖关系，当一个对象状态改变时，所有依赖它的对象都会收到通知并自动更新', 3),
(7, '策略模式的应用场景？', '需要在不同算法/策略间灵活切换时，如支付方式选择、排序算法选择、折扣计算策略', 4),
(7, '装饰器模式 vs 继承？', '装饰器：动态扩展功能，比继承更灵活，避免类爆炸；继承：静态扩展，编译时确定，类层次膨胀', 5);

-- 知识包8：英语四级高频词汇
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(8, '英语四级高频词汇', 'CET-4 考试高频核心词汇，含释义与例句', '英语', 6);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(8, 'inevitable', 'adj. 不可避免的，必然发生的。例：Change is inevitable in a growing company.', 1),
(8, 'substantial', 'adj. 大量的，实质的。例：We need substantial evidence to support this claim.', 2),
(8, 'comprehensive', 'adj. 全面的，综合的。例：The report provides a comprehensive analysis of the market.', 3),
(8, 'demonstrate', 'v. 证明，演示。例：The experiment demonstrates the effectiveness of the new drug.', 4),
(8, 'perspective', 'n. 视角，观点。例：Try to see the problem from a different perspective.', 5),
(8, 'constitute', 'v. 构成，组成。例：Women constitute over 50% of the population.', 6);

-- 知识包9：Git 版本控制
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(9, 'Git 版本控制', 'Git 核心概念与常用命令，开发者必备技能', 'Git', 5);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(9, 'git merge 和 git rebase 的区别？', 'merge：保留完整分支历史，创建合并提交；rebase：将提交重新应用到目标分支，历史线性更清晰，但改写提交历史', 1),
(9, 'git reset 的三种模式？', '--soft：只移动HEAD，保留暂存区和工作区；--mixed（默认）：移动HEAD，重置暂存区，保留工作区；--hard：全部重置', 2),
(9, '什么是 git stash？', '临时保存未提交的修改，将工作区恢复到干净状态。git stash pop 恢复最近一次保存', 3),
(9, 'git cherry-pick 的作用？', '将指定提交应用到当前分支，用于从其他分支挑选特定修改，而不合并整个分支', 4),
(9, '如何解决合并冲突？', '1.git status查看冲突文件 2.手动编辑冲突区域（保留需要的代码） 3.git add标记已解决 4.git commit完成合并', 5);

-- 知识包10：算法与复杂度
INSERT INTO t_study_pack (id, name, description, category_name, card_count) VALUES
(10, '算法与复杂度', '常见排序算法与时间空间复杂度分析', '算法', 5);

INSERT INTO t_study_pack_item (pack_id, front_content, back_content, sort_order) VALUES
(10, '快速排序的原理和复杂度？', '选取基准元素，将数组分为小于和大于基准的两部分，递归排序。平均O(n log n)，最坏O(n²)，空间O(log n)', 1),
(10, '归并排序的原理和复杂度？', '分治法：将数组对半拆分，分别排序后合并。时间始终O(n log n)，空间O(n)，稳定排序', 2),
(10, '动态规划的解题步骤？', '1.定义状态（dp数组含义） 2.推导状态转移方程 3.确定初始条件 4.确定遍历顺序 5.举例验证', 3),
(10, 'BFS 和 DFS 的区别？', 'BFS：广度优先，用队列，适合最短路径；DFS：深度优先，用栈/递归，适合全排列、组合问题', 4),
(10, '什么是贪心算法？', '每步选择局部最优解，期望得到全局最优。适用条件：贪心选择性质+最优子结构。如：活动选择、哈夫曼编码', 5);
