Java Code Documentation Specification
本项目所有 Java 代码必须严格遵循以下文档规范。

一、类注释规范
所有类必须包含标准 JavaDoc 注释，格式如下：

/**
 * 类功能描述（说明该类核心职责）
 *
 * @author jLU
 * @date 生成注释的时间
 */


规则：

1. 必须使用 JavaDoc 风格（/** */）
2. 必须描述类的主要职责
3. 禁止省略类注释
4. 禁止使用嵌套循环
5. 禁止物理删除数据，仅通过软删除（如设置状态为已删除）实现

## 二、接口注释规范

所有接口必须添加 JavaDoc 注释：

```java
/**
 * 接口功能描述
 *
 * @author jLU
 */


## 三、方法注释规范

所有 public 方法必须添加 JavaDoc 注释，格式如下：

```java
/**
 * 方法功能描述
 *
 * @param paramName 参数说明
 * @return 返回值说明
 * @throws ExceptionType 异常说明（如有）
 */


规则：

1. 必须说明方法功能
2. 必须为所有参数添加 @param
3. 有返回值必须添加 @return
4. 抛出异常必须添加 @throws
5. 禁止生成无意义描述

## 四、Controller 接口额外要求

Controller 层方法必须说明：

- 接口用途
- 请求参数说明
- 返回结构说明

推荐格式（示例）：

```java
/**
 * 创建队伍接口。
 *
 * <p>用途：前端提交创建队伍信息，后端完成参数校验、落库，并返回新队伍 id。</p>
 *
 * @param teamAddRequest 创建队伍请求体（包含队伍名称、人数上限、过期时间、状态等）
 * @param request Http 请求对象（用于获取当前登录用户）
 * @return 统一返回结构，data 为新创建的队伍 id
 * @throws BusinessException 参数错误 / 未登录 / 业务校验不通过时抛出
 */
@PostMapping("/add")
public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
    ...
}