-- Demo content for the local weblog database. Back up before importing.
-- Replaces content tables only; t_user, t_user_role and t_blog_settings are preserved.
-- Run with the mysql client using --default-character-set=utf8mb4, without --force.
SET NAMES utf8mb4;
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION';
SET @seed_day = CURDATE();
START TRANSACTION;

DELETE FROM t_comment;
DELETE FROM t_article_tag_rel;
DELETE FROM t_article_category_rel;
DELETE FROM t_article_content;
DELETE FROM t_article;
DELETE FROM t_tag;
DELETE FROM t_category;
DELETE FROM t_chat_message;
DELETE FROM t_statistics_article_pv;

INSERT INTO t_category (id, name, create_time, update_time, is_deleted, articles_total) VALUES
(1, '前端开发', DATE_SUB(@seed_day, INTERVAL 90 DAY), NOW(), 0, 0),
(2, '后端开发', DATE_SUB(@seed_day, INTERVAL 90 DAY), NOW(), 0, 0),
(3, '项目实战', DATE_SUB(@seed_day, INTERVAL 90 DAY), NOW(), 0, 0),
(4, '效率与随笔', DATE_SUB(@seed_day, INTERVAL 90 DAY), NOW(), 0, 0);

INSERT INTO t_tag (id, name, create_time, update_time, is_deleted, articles_total) VALUES
(1, 'Vue 3', NOW(), NOW(), 0, 0),
(2, 'JavaScript', NOW(), NOW(), 0, 0),
(3, 'CSS', NOW(), NOW(), 0, 0),
(4, 'Spring Boot', NOW(), NOW(), 0, 0),
(5, 'Java', NOW(), NOW(), 0, 0),
(6, 'MySQL', NOW(), NOW(), 0, 0),
(7, 'WebSocket', NOW(), NOW(), 0, 0),
(8, '性能优化', NOW(), NOW(), 0, 0),
(9, '工程实践', NOW(), NOW(), 0, 0),
(10, '测试', NOW(), NOW(), 0, 0),
(11, '工具', NOW(), NOW(), 0, 0),
(12, '生活记录', NOW(), NOW(), 0, 0);

INSERT INTO t_article (id, title, cover, summary, create_time, update_time, is_deleted, read_num) VALUES
(1, 'Vue 3 组件通信：从 Props 到 Pinia 的选择', '/demo/covers/frontend.webp',
 '用一个文章列表的例子，梳理父子组件通信、跨层级共享与全局状态的边界，让数据流更容易维护。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 84 DAY), '09:20:00'), NOW(), 0, 128),
(2, 'Spring Boot 接口设计：让请求和响应保持一致', '/demo/covers/backend.webp',
 '从参数校验、业务异常到分页响应，整理博客接口开发中容易忽略的细节，并给出可检查的接口约定。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 76 DAY), '10:10:00'), NOW(), 0, 96),
(3, '把个人博客从能用做到好用', '/demo/covers/project.webp',
 '一次围绕阅读、搜索与交流的博客整理记录：先打通真实流程，再用一组完整内容检查每个页面。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 62 DAY), '14:30:00'), NOW(), 0, 204),
(4, '周末整理工作台：给专注留一点空间', '/demo/covers/life.webp',
 '整理桌面、记录想法、收拢待办。比起增加工具，我更想找到一个容易开始、也容易坚持的工作节奏。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 56 DAY), '16:00:00'), NOW(), 0, 168),
(5, 'CSS 布局排查：为什么按钮和头像会被挤变形', '/demo/covers/frontend.webp',
 '从 Flex 收缩机制出发，逐步定位导航栏变形、文字换行与头像比例失衡的问题，补齐移动端边界。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 45 DAY), '09:45:00'), NOW(), 0, 87),
(6, 'MySQL 游标分页：给聊天历史一个稳定的顺序', '/demo/covers/backend.webp',
 '聊天消息一边新增、一边向上翻页时，如何避免重复和遗漏？用消息 ID 实现一套清晰的历史查询。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 38 DAY), '11:15:00'), NOW(), 0, 156),
(7, 'WebSocket 聊天室：从连接建立到消息展示', '/demo/covers/project.webp',
 '拆开登录身份、连接状态、消息入库和页面回显四个环节，记录一次公共聊天室的端到端联调。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 30 DAY), '13:40:00'), NOW(), 0, 245),
(8, '我的每周复盘：把遇到的问题变成下一次的清单', '/demo/covers/life.webp',
 '保留有用的经验，也允许计划调整。用一页笔记回顾本周产出、卡点和下周最值得完成的一件事。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 21 DAY), '18:00:00'), NOW(), 0, 192),
(9, '深浅主题切换：一次圆形过渡动画的打磨', '/demo/covers/frontend.webp',
 '以主题按钮为圆心展开深色页面，切回浅色时反向收回，并处理滚动定位、结束帧和动画降级。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 14 DAY), '10:30:00'), NOW(), 0, 318),
(10, '评论系统的数据关系：一级评论、回复与审核', '/demo/covers/backend.webp',
 '用文章下的讨论串解释评论路由、父评论和回复目标之间的关系，再检查审核状态与展示顺序。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 9 DAY), '15:20:00'), NOW(), 0, 221),
(11, '为博客准备一套真正能发现问题的测试数据', '/demo/covers/project.webp',
 '测试内容不只是几行占位文字。文章、标签、评论和聊天记录要能串起来，才能发现真实使用中的问题。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 5 DAY), '09:00:00'), NOW(), 0, 157),
(12, '开发工具箱：我会反复使用的几个小习惯', '/demo/covers/life.webp',
 '把常见排查过程写成可重复的小步骤：先看现象，再收集证据，最后留下可恢复的数据与清晰的记录。',
 TIMESTAMP(DATE_SUB(@seed_day, INTERVAL 2 DAY), '17:10:00'), NOW(), 0, 264);

INSERT INTO t_article_content (id, article_id, content) VALUES
(1, 1, '## 从一个文章列表开始

页面负责获取文章，列表负责排列，卡片负责展示。先把这三件事分开，组件之间需要传递的数据就清楚了。卡片只接收文章对象，不负责发请求，也不直接修改上层的列表。

### 父子组件：Props 与事件

```vue
<script setup>
defineProps({ article: { type: Object, required: true } })
const emit = defineEmits(["open"])
</script>

<template>
  <button @click="emit(''open'', article.id)">{{ article.title }}</button>
</template>
```

父组件传入展示数据，子组件通过事件表达用户意图。修改列表、跳转详情这些动作由父组件处理，排查时只需沿着事件往上找。

### 跨层级的数据

主题、用户信息适合放进共享状态；表单草稿、弹窗开关通常保留在页面内部。使用 Pinia 的判断标准是多处是否需要共同维护同一份数据，而不是组件数量是否很多。

| 场景 | 建议方式 |
| --- | --- |
| 文章卡片接收标题 | Props |
| 卡片通知打开详情 | emit |
| 全站用户登录信息 | Pinia |
| 单页筛选条件 | 页面内的 ref |

## 验证数据流

- 切换文章时，标题和标签是否同时变化。
- 刷新页面后，登录信息是否按预期恢复。
- 退出登录时，共享状态和持久化存储是否一起清理。

把数据归属定下来之后，状态更新就不需要依靠额外的刷新来碰运气。'),
(2, 2, '## 接口约定先于页面联调

一个接口能返回 JSON，还不足以说明它已经便于使用。页面需要知道请求是否成功、业务错误如何展示，以及列表中的数量究竟表示当前页还是总数。

### 请求参数有边界

新增文章时，标题不能为空；分页查询时，页码和每页数量需要合理上限。校验应当放在服务入口，同时保留业务层对关联资源是否存在的检查。

```java
public class ArticleQuery {
    @Min(1)
    private long current = 1;

    @Min(1)
    @Max(50)
    private long size = 10;
}
```

### 成功与失败都保持结构稳定

```json
{
  "success": true,
  "data": [],
  "current": 1,
  "size": 10,
  "total": 0
}
```

空列表应当是一种正常结果。文章不存在、参数不合法和服务暂时异常则需要不同的错误信息，方便页面选择提示或跳转。

## 联调检查清单

1. 分别提交空值、边界值与正常参数。
2. 检查时间字段的格式是否一致。
3. 在文章不存在时确认返回受控错误。
4. 对写入操作确认失败时有没有部分数据残留。

接口约定稳定之后，页面就能把精力放在交互上，日志也更容易关联到具体请求。'),
(3, 3, '## 先走完一条完整路径

我会先从首页打开一篇文章，阅读正文，点击标签，再回到原文发表评论。这样能同时检查列表、详情、路由与评论关联，而不是只看某一个页面能否打开。

### 阅读体验

标题层级、段落宽度和代码块会影响阅读节奏。文章中既要有短段落，也要有表格和较长代码，才能看到真实的换行与溢出情况。

> 内容应当帮助发现问题，而不是把空白区域暂时填满。

### 搜索与归档

分类适合表达文章所属领域，标签适合表达文章涉及的技术。归档则按时间提供另一条入口。准备跨月文章后，可以更直观地检查月份分组与排序。

| 入口 | 主要检查 |
| --- | --- |
| 首页 | 封面、摘要、分页 |
| 分类与标签 | 关联数量、筛选结果 |
| 归档 | 月份分组、时间顺序 |
| 搜索 | 标题命中、跳转详情 |

## 交流功能

文章评论适合围绕一个主题持续讨论，聊天室适合即时交流。两者的数据结构和加载方式不同，需要分别验证退出、刷新和历史加载。

### 这次整理的顺序

1. 保证内容与关联记录完整。
2. 用真实接口验证数据。
3. 在桌面和手机上走一遍相同流程。
4. 记录可重复出现的问题，再逐个修复。

页面变得好用，往往来自这些小流程逐渐连贯起来。'),
(4, 4, '## 从清空一小块桌面开始

周末我没有给自己安排一份很长的整理清单，只是先把键盘前的一块区域空出来。留下常用的笔记本和水杯，其余物品各自放回固定位置。

### 让常用物品容易拿到

高频使用的东西放在手边，低频使用的东西收进抽屉。充电线固定之后，换设备不必重新找接口，也减少了桌面上来回缠绕的线。

### 给待办减一点负担

以前我会把想到的所有事情都放进今天的计划，结果每晚都在把未完成事项挪到明天。现在只写三件值得完成的事，其他想法先放进收集区。

| 时间 | 安排 |
| --- | --- |
| 开始前 5 分钟 | 选定当前最重要的一件事 |
| 工作中 | 把突然出现的想法记下来 |
| 结束前 10 分钟 | 留下下一步与未解决的问题 |

## 留一点空白

工作台并不需要一直保持展示照片里的状态。对我而言，整理的意义是下一次坐下来时，可以很快知道从哪里开始。

> 好的习惯应该降低开始的难度，而不是增加维持它的压力。

下午出门走了一会儿，回来时再打开项目，之前卡住的问题也有了新的切入点。'),
(5, 5, '## 变形通常从空间分配开始

导航栏同时放着站点名称、主题开关、搜索框和头像。屏幕变窄之后，Flex 容器会尝试压缩子元素；如果头像和开关也参与收缩，就可能从圆形变成椭圆。

### 固定尺寸不等于禁止收缩

```css
.avatar-button {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  overflow: hidden;
}

.avatar-button img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
```

按钮负责尺寸，图片负责填充。使用 object-fit 可以避免原图比例把头像撑开，但容器本身仍要有明确的宽高。

### 搜索框需要响应式策略

宽屏显示图标、提示文字和快捷键；窄屏只保留搜索图标。这样可以减少占用，同时保留相同的入口与点击范围。

## 排查顺序

1. 看容器是否真的有足够宽度。
2. 看哪些子元素允许收缩。
3. 检查文字是否被强制保持单行。
4. 分别测试游客、登录状态和较长站点名称。

### 需要覆盖的尺寸

建议至少检查 320、390、768 和 1440 像素宽度，并尝试放大系统字体。布局在这些边界下保持稳定，才说明问题被处理到了。'),
(6, 6, '## 为什么聊天历史适合游标

当用户向上加载旧消息时，新的消息还在不断进入。如果按偏移量查询，前面的数据一增加，下一页的起点就会移动，容易出现重复记录。

### 用已加载的最小 ID 继续查询

```sql
SELECT id, nickname, content, create_time
FROM t_chat_message
WHERE id < ?
ORDER BY id DESC
LIMIT 11;
```

页面每次显示十条，但查询十一条。多出来的一条只用来判断是否还有历史；真正返回前，再把十条记录反转为时间正序。

### ID 与时间的职责不同

消息 ID 提供稳定的分页顺序，时间字段负责展示。两条消息可能具有相同的秒级时间，不能只依赖时间字符串判断先后。

| 字段 | 用途 |
| --- | --- |
| id | 排序、去重与历史游标 |
| create_time | 展示发送时间 |
| qq | 判断持久化消息的发送身份 |

## 页面端的合并

历史记录追加在列表前面，新消息追加在末尾。合并前按 ID 去重，可以处理历史请求与实时推送刚好重叠的情况。

### 验证分页边界

分别准备零条、十条、十一条和超过三页的消息。还要在历史请求未完成时接收新消息，确认滚动位置与排序没有跳变。'),
(7, 7, '## 把发送流程拆成四步

点击发送后，页面需要把内容交给 WebSocket，服务端保存消息，再广播带有消息 ID 的结果，最后由客户端更新列表和输入框。只确认数据库有记录，无法说明整条链路已经完成。

### 身份要跨连接保持稳定

连接 ID 会随着刷新而变化，用户身份却不应随之变化。聊天消息保存用户标识后，历史记录和实时推送才能按同一套规则判断是否为自己发送。

```javascript
function mergeMessages(history, incoming) {
  const byId = new Map()
  for (const message of [...history, ...incoming]) {
    byId.set(message.id, message)
  }
  return [...byId.values()].sort((a, b) => a.id - b.id)
}
```

### 退出不只是关闭弹窗

退出时需要清理聊天身份、关闭连接并更新页面状态。刷新后如果又恢复旧身份，通常说明持久化存储没有一起清理。

## 联调场景

| 操作 | 预期结果 |
| --- | --- |
| 发送正常消息 | 输入框清空，消息出现 |
| 刷新页面 | 历史正常加载 |
| 重复打开同一用户 | 在线身份不重复计数 |
| 退出聊天 | 登录区域恢复，连接关闭 |

### 失败也要有结果

连接断开时保留尚未发送的草稿，并提示当前状态。不要仅在控制台记录失败，否则用户只能通过反复点击判断系统有没有响应。'),
(8, 8, '## 复盘不必写成长篇总结

我把每周复盘控制在一页以内：完成了什么，哪里卡住了，下周准备先做什么。重点是留下可以帮助下一次行动的信息。

### 本周完成

- 整理了博客中几条常用的阅读路径。
- 把两个反复遇到的问题写成了排查笔记。
- 给测试内容增加了评论与聊天记录。

### 一个值得保留的变化

遇到问题时，我开始先记录具体操作和页面现象，再去猜原因。比如记录窗口宽度、登录状态和刷新前后的差别，这些信息比一句“偶尔有问题”更容易继续排查。

| 记录 | 作用 |
| --- | --- |
| 操作步骤 | 帮助重复出现同一现象 |
| 预期行为 | 说明真正需要解决的目标 |
| 已有证据 | 避免重复做无效尝试 |

## 下周只先选一件事

把最值得完成的一件事写清楚，同时标出它的第一步。如果第一步仍然很模糊，往往说明任务还可以再拆小一点。

> 记录是为了减少下一次的阻力，不是为了证明这一周一直很忙。

写完之后关掉笔记，留出一些时间去散步。休息也是让工作能够持续的一部分。'),
(9, 9, '## 让主题切换从按钮开始

点击主题按钮时，以按钮的屏幕中心为圆心，逐步显示深色页面。切回浅色时，把旧的深色画面向同一个圆心收回，方向会更加直观。

### 圆心必须使用视口坐标

```javascript
const rect = switchElement.getBoundingClientRect()
const x = rect.left + rect.width / 2
const y = rect.top + rect.height / 2
const radius = Math.hypot(
  Math.max(x, innerWidth - x),
  Math.max(y, innerHeight - y)
)
```

页面发生滚动后，按钮在文档里的位置和在屏幕里的位置不同。使用视口坐标，可以让粘性导航栏中的动画继续对准按钮。

### 收尾比中间效果更容易被忽略

收回结束时，需要保留裁剪的最后一帧，直到旧画面被移除。如果裁剪先消失，完整的深色画面会重新露出一帧，看起来就是闪屏。

```javascript
const options = {
  duration: 600,
  fill: "forwards"
}
```

## 完整的验证

1. 检查展开与收回两个方向。
2. 在滚动后和手机尺寸下确认圆心。
3. 连续点击时确认状态不会错乱。
4. 检查最后一帧，而不只看动画中途。

浏览器不支持相关能力，或用户偏好减少动态效果时，直接完成主题切换也应保持可用。'),
(10, 10, '## 一条回复需要两个关系

评论列表通常只展示两层，但二级回复仍可能是在回答另一条二级回复。因此需要分别记录它属于哪条一级评论，以及它具体回复了谁。

| 字段 | 含义 |
| --- | --- |
| router_url | 评论所属文章路径 |
| parent_comment_id | 所属一级评论 |
| reply_comment_id | 直接回复目标 |
| status | 审核状态 |

### 一级评论与回复

一级评论的 parent_comment_id 为空。回复则始终指向所在讨论串的一级评论；如果直接目标是另一条回复，页面可以显示“回复某某”来补充上下文。

```text
文章
  一级评论 A
    回复 B：回答 A
    回复 C：回答 B
  一级评论 D
```

### 状态影响可见性

测试数据应当明确设置为审核通过，否则后台有记录，文章页面却可能显示零条评论。计数也需要与实际可见列表使用一致的筛选条件。

## 检查关联是否完整

准备数据时，先写入文章，再写一级评论，最后写入回复。每条回复的父评论和直接目标都要属于同一篇文章。

### 时间顺序

一级评论按最近讨论在前展示，讨论串内部按时间正序排列。加入不同时间的回复后，可以更容易看出顺序是否符合阅读习惯。'),
(11, 11, '## 测试数据需要形成一张完整的网

只有文章标题时，首页看起来有内容，但详情、标签和评论仍然可能为空。完整的数据需要覆盖从文章到分类、标签、正文和讨论的关联。

### 给不同页面不同的检查点

文章数量超过首页单页容量，才能看到分页；发布时间跨过月份，才能检查归档；聊天室消息超过三页，才能验证历史游标。

| 内容 | 数量设计 |
| --- | --- |
| 文章 | 超过一页，每篇有正文 |
| 评论 | 每篇包含一级评论和回复 |
| 聊天消息 | 足够向前加载多页 |
| 分类、标签 | 每项都有实际关联 |

### 让内容形式有变化

正文包含标题、引用、列表、表格和代码块。摘要有不同长度，封面使用本地文件。这样可以同时看到排版和图片加载方面的问题。

## 写入前先准备恢复入口

清理旧内容前保留数据库备份，插入新数据时使用事务。不要为测试内容删除后台账号和博客配置，否则内容测试会变成环境重建。

### 写入后检查真实页面

- 首页数量与分页是否正确。
- 每篇文章能否显示对应评论。
- 分类与标签计数是否一致。
- 聊天历史是否连续且没有重复。
- 搜索能否找到新文章。

这些检查连在一起，才能说明测试数据确实帮助系统进入可用状态。'),
(12, 12, '## 工具不多，步骤要稳

我的工具箱没有复杂的固定组合。更重要的是把常见动作变成几个容易重复的步骤，让排查过程有据可循，也能在中断之后继续。

### 先看差异，再下判断

修改前确认当前文件状态，读懂已有改动；出现问题时，先保存能重复现象的操作路径。清楚知道发生了什么，通常比立刻尝试很多修改更有效。

```text
观察现象
记录操作
缩小范围
验证假设
修复并复查
```

### 给重复操作留一个入口

数据库测试内容、常见请求和启动方式都值得留下简短记录。记录中需要说明适用环境和影响范围，让下一次使用不必重新猜测。

| 习惯 | 带来的变化 |
| --- | --- |
| 修改前查看差异 | 减少误覆盖 |
| 数据替换前备份 | 保留恢复入口 |
| 对照真实页面验证 | 发现接口之外的问题 |
| 结束时记录结果 | 降低下一次接手成本 |

## 最后检查目标

完成一个任务后，我会回到最初的操作再走一遍，而不是只确认修改的代码没有报错。真正需要交付的是可用的结果，以及足够清楚的说明。');

INSERT INTO t_article_category_rel (article_id, category_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 4), (5, 1), (6, 2),
(7, 3), (8, 4), (9, 1), (10, 2), (11, 3), (12, 4);

INSERT INTO t_article_tag_rel (article_id, tag_id) VALUES
(1, 1), (1, 2), (1, 9),
(2, 4), (2, 5), (2, 9),
(3, 1), (3, 4), (3, 9),
(4, 11), (4, 12),
(5, 1), (5, 3), (5, 8),
(6, 6), (6, 7), (6, 8),
(7, 1), (7, 4), (7, 7),
(8, 9), (8, 12),
(9, 2), (9, 3), (9, 10),
(10, 4), (10, 6), (10, 9),
(11, 6), (11, 9), (11, 10),
(12, 9), (12, 11), (12, 12);

-- Three top-level comments and two replies per article.
INSERT INTO t_comment (id, content, avatar, nickname, mail, website, router_url,
 create_time, update_time, is_deleted, reply_comment_id, parent_comment_id, status, reason) VALUES
(1, 'Props 和共享状态的边界解释得很清楚，我准备把表单草稿从全局状态里移出来。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/1', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(2, '多个页面需要保留同一份筛选条件时，也适合用 Pinia 吗？', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/1', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(3, '可以，但先判断是否需要共享；只需要链接可复现时，也可以考虑路由查询参数。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/1', NOW(), NOW(), 0, 2, 2, 2, ''),
(4, '明白了，我的场景还需要分享筛选后的地址，放在路由里更合适。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/1', NOW(), NOW(), 0, 3, 2, 2, ''),
(5, '配合文章卡片的例子读起来很顺，事件命名也值得统一一下。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/1', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(6, '空列表是正常结果这一点很实用，之前页面总是把空数据当异常。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/2', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(7, '分页数量的上限应该在前端限制，还是接口也要限制？', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/2', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(8, '接口也需要限制，前端校验方便操作，后端校验保证所有调用都有边界。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/2', NOW(), NOW(), 0, 7, 7, 2, ''),
(9, '我补了超大 size 的请求用例，确实发现之前没有处理这个边界。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/2', NOW(), NOW(), 0, 8, 7, 2, ''),
(10, '统一时间格式之后，列表和详情的展示终于一致了。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/2', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(11, '从首页一路走到评论区的检查方式很好，比只打开每个页面更容易发现问题。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/3', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(12, '分类和标签有时会重复，你是怎么区分它们的？', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/3', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(13, '分类保持少而稳定，标签描述具体主题。一篇项目记录也可能同时涉及前后端标签。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/3', NOW(), NOW(), 0, 12, 12, 2, ''),
(14, '这样归档和筛选各自承担不同作用，导航也更容易理解。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/3', NOW(), NOW(), 0, 13, 12, 2, ''),
(15, '准备跨月文章这个细节很有帮助，归档的月份分组也能一起测了。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/3', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(16, '先空出键盘前面一块地方，这个起点很容易执行，今天就试试。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/4', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(17, '临时想到的小任务会不会越记越多？', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/4', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(18, '会，所以每周会看一次收集区，把已经不重要的内容直接划掉。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/4', NOW(), NOW(), 0, 17, 17, 2, ''),
(19, '允许删除待办这点很重要，不然清单本身也会变成压力。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/4', NOW(), NOW(), 0, 18, 17, 2, ''),
(20, '整理好线材以后，桌面真的会清爽很多。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/4', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(21, '原来写了宽高也可能被 Flex 压缩，头像变椭圆的问题终于找到原因了。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/5', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(22, '小屏只显示搜索图标后，点击范围还需要单独处理吗？', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/5', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(23, '需要保留稳定的按钮宽高，不要让图标本身决定整个点击区域。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/5', NOW(), NOW(), 0, 22, 22, 2, ''),
(24, '我把按钮固定成方形，手机上点起来也方便多了。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/5', NOW(), NOW(), 0, 23, 22, 2, ''),
(25, '补测大字体很有必要，普通尺寸下没出现的问题一下就暴露了。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/5', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(26, '多查一条判断 hasMore 的思路简洁，最后一页不用再猜了。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/6', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(27, '刚好十条消息时，需要再请求一次才能知道没有更多吗？', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/6', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(28, '不需要。请求十一条却只拿到十条，就已经可以返回 hasMore 为 false。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/6', NOW(), NOW(), 0, 27, 27, 2, ''),
(29, '理解了，十条和十一条的边界确实应该各准备一组数据。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/6', NOW(), NOW(), 0, 28, 27, 2, ''),
(30, '用 ID 去重之后，历史请求和实时消息交叉返回也好处理了。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/6', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(31, '数据库有消息但页面没显示，按四个环节拆开以后排查快多了。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/7', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(32, '刷新后连接 ID 变了，历史里自己发的消息怎么识别？', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/7', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(33, '用持久化的用户标识判断归属，连接 ID 只用于管理当前这条连接。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/7', NOW(), NOW(), 0, 32, 32, 2, ''),
(34, '这样退出再进入也能识别旧消息，不会被当作别人的消息。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/7', NOW(), NOW(), 0, 33, 32, 2, ''),
(35, '断线时保留草稿这个细节很好，不然重新输入一遍很影响体验。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/7', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(36, '只写一页更容易坚持，我以前总把复盘写成一项大工程。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/8', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(37, '如果一周没有明显产出，复盘该写些什么？', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/8', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(38, '可以记录具体卡点和已有尝试，下周就不用从同一个起点重新开始。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/8', NOW(), NOW(), 0, 37, 37, 2, ''),
(39, '这个角度很好，解决问题的过程本身也值得留下。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/8', NOW(), NOW(), 0, 38, 37, 2, ''),
(40, '最后那句很喜欢，休息以后再回来，思路经常会更清楚。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/8', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(41, '圆心跟着按钮走，比全屏直接切色自然很多。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/9', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(42, '之前收回结束会闪一下，是深色图层又显示出来了吗？', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/9', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(43, '是的，裁剪比快照先清理了。保留结束帧，等快照移除后再释放动画即可。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/9', NOW(), NOW(), 0, 42, 42, 2, ''),
(44, '我也补了最后一帧的检查，单看动画中途确实发现不了这个问题。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/9', NOW(), NOW(), 0, 43, 42, 2, ''),
(45, '减少动态效果时直接切换主题也能正常用，这个兼容处理很好。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/9', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(46, '父评论和直接回复目标分开以后，楼中楼关系一下就清楚了。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/10', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(47, '回复二级评论时，parent_comment_id 还是指向最上面的一级评论吗？', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/10', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(48, '是的，这样展示仍是两层；reply_comment_id 再记录具体回答的是哪条回复。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/10', NOW(), NOW(), 0, 47, 47, 2, ''),
(49, '这条回复正好可以用来检查“回复某某”的显示效果。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/10', NOW(), NOW(), 0, 48, 47, 2, ''),
(50, '审核通过状态也要写进去，否则有数据但前台看不见，很容易误判。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/10', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(51, '超过一页的文章、跨月日期和多页消息，这组边界很有针对性。', '/demo/avatars/mu.png', '木木', 'mu@example.test', '', '/article/11', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(52, '重新导入数据时，分类和标签数量要怎么保持准确？', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/11', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(53, '根据关联表重新统计，而不是手写数量。这样内容调整后也不容易漏改。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/11', NOW(), NOW(), 0, 52, 52, 2, ''),
(54, '我把详情和搜索也走了一遍，关联完整比只看首页更重要。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/11', NOW(), NOW(), 0, 53, 52, 2, ''),
(55, '本地封面加载很稳定，断网时也能继续看测试页面。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/11', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(56, '先看差异再修改，这个习惯能省掉很多事后找回改动的时间。', '/demo/avatars/lin.png', '林间', 'lin@example.test', '', '/article/12', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(57, '你会把所有排查过程都写进文档吗？', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/12', NOW(), NOW(), 0, NULL, NULL, 2, ''),
(58, '只保留有用的结论、关键证据和下一步，重复尝试不必全部记流水账。', '/demo/avatars/power.png', 'power', 'power@example.test', '', '/article/12', NOW(), NOW(), 0, 57, 57, 2, ''),
(59, '精简到别人能接着做就够了，确实比堆日志更有帮助。', '/demo/avatars/yu.png', '小雨', 'yu@example.test', '', '/article/12', NOW(), NOW(), 0, 58, 57, 2, ''),
(60, '结束时回到原始操作再走一遍，这条我也加入自己的清单了。', '/demo/avatars/tang.png', '糖糖', 'tang@example.test', '', '/article/12', NOW(), NOW(), 0, NULL, NULL, 2, '');

UPDATE t_comment c
JOIN t_article a ON c.router_url = CONCAT('/article/', a.id)
SET c.create_time = DATE_ADD(a.create_time, INTERVAL (1 + MOD(c.id - 1, 5)) HOUR),
    c.update_time = DATE_ADD(a.create_time, INTERVAL (1 + MOD(c.id - 1, 5)) HOUR);

-- Synthetic identities do not refer to real QQ accounts.
SET @chat_start = DATE_SUB(NOW(), INTERVAL 180 MINUTE);
INSERT INTO t_chat_message (id, nickname, avatar, content, create_time, qq) VALUES
(1, 'power', '/demo/avatars/power.png', '欢迎来到公共聊天室，最近在整理博客的阅读和交流功能。', @chat_start, 'demo-power'),
(2, '林间', '/demo/avatars/lin.png', '大家好，刚从首页过来，新的文章列表看起来很清爽。', DATE_ADD(@chat_start, INTERVAL 3 MINUTE), 'demo-lin'),
(3, '木木', '/demo/avatars/mu.png', '我在看 Vue 组件通信那篇，准备顺手整理一下自己的状态管理。', DATE_ADD(@chat_start, INTERVAL 6 MINUTE), 'demo-mu'),
(4, '小雨', '/demo/avatars/yu.png', '文章下面的评论也能直接回复，适合把问题留在对应主题里。', DATE_ADD(@chat_start, INTERVAL 9 MINUTE), 'demo-yu'),
(5, '糖糖', '/demo/avatars/tang.png', '聊天室就用来随手交流，两个入口分工很清楚。', DATE_ADD(@chat_start, INTERVAL 12 MINUTE), 'demo-tang'),
(6, '林间', '/demo/avatars/lin.png', '你们平时是先写接口，还是先把页面结构搭出来？', DATE_ADD(@chat_start, INTERVAL 15 MINUTE), 'demo-lin'),
(7, 'power', '/demo/avatars/power.png', '我会先约定请求和响应，再用一条完整流程把前后端连起来。', DATE_ADD(@chat_start, INTERVAL 18 MINUTE), 'demo-power'),
(8, '木木', '/demo/avatars/mu.png', '我也喜欢先打通列表到详情，再补筛选和边界状态。', DATE_ADD(@chat_start, INTERVAL 21 MINUTE), 'demo-mu'),
(9, '小雨', '/demo/avatars/yu.png', '今天发现一个布局问题：头像写了宽高还是会被压成椭圆。', DATE_ADD(@chat_start, INTERVAL 24 MINUTE), 'demo-yu'),
(10, '林间', '/demo/avatars/lin.png', '可以检查一下 flex-shrink，导航里的固定按钮通常不应该继续收缩。', DATE_ADD(@chat_start, INTERVAL 27 MINUTE), 'demo-lin'),
(11, '糖糖', '/demo/avatars/tang.png', 'CSS 布局那篇正好讲了这个，我刚刚也看到了。', DATE_ADD(@chat_start, INTERVAL 30 MINUTE), 'demo-tang'),
(12, '小雨', '/demo/avatars/yu.png', '加上不收缩以后就正常了，手机端搜索图标也没有被挤走。', DATE_ADD(@chat_start, INTERVAL 33 MINUTE), 'demo-yu'),
(13, 'power', '/demo/avatars/power.png', '除了普通宽度，放大字体后也值得看一下，容易发现隐藏的空间问题。', DATE_ADD(@chat_start, INTERVAL 36 MINUTE), 'demo-power'),
(14, '木木', '/demo/avatars/mu.png', '主题按钮的圆形切换挺有意思，白天和黑夜的方向刚好相反。', DATE_ADD(@chat_start, INTERVAL 39 MINUTE), 'demo-mu'),
(15, '糖糖', '/demo/avatars/tang.png', '我试了滚动以后再切换，圆心还是对着按钮的。', DATE_ADD(@chat_start, INTERVAL 42 MINUTE), 'demo-tang'),
(16, '林间', '/demo/avatars/lin.png', '最后一帧也很重要，之前做过一次动画，收尾没处理好就会闪一下。', DATE_ADD(@chat_start, INTERVAL 45 MINUTE), 'demo-lin'),
(17, 'power', '/demo/avatars/power.png', '是的，旧画面移除之前要保持裁剪，不能先把完整图层露出来。', DATE_ADD(@chat_start, INTERVAL 48 MINUTE), 'demo-power'),
(18, '小雨', '/demo/avatars/yu.png', '这个细节只看中间截图还真不容易发现。', DATE_ADD(@chat_start, INTERVAL 51 MINUTE), 'demo-yu'),
(19, '木木', '/demo/avatars/mu.png', '我刚向上加载了历史消息，顺序是从早到晚的。', DATE_ADD(@chat_start, INTERVAL 54 MINUTE), 'demo-mu'),
(20, '林间', '/demo/avatars/lin.png', '新消息持续进来时，历史查询用游标会比偏移量稳定。', DATE_ADD(@chat_start, INTERVAL 57 MINUTE), 'demo-lin'),
(21, 'power', '/demo/avatars/power.png', '每次用最早一条消息的 ID 继续向前查，再按 ID 去重合并。', DATE_ADD(@chat_start, INTERVAL 60 MINUTE), 'demo-power'),
(22, '糖糖', '/demo/avatars/tang.png', '这样边聊天边向上翻，也不会把同一条消息加载两次。', DATE_ADD(@chat_start, INTERVAL 63 MINUTE), 'demo-tang'),
(23, '小雨', '/demo/avatars/yu.png', '有了足够多的记录，最后一页和“没有更多”状态才好测。', DATE_ADD(@chat_start, INTERVAL 66 MINUTE), 'demo-yu'),
(24, '木木', '/demo/avatars/mu.png', '我会特别试十条和十一条，刚好能覆盖多查一条的边界。', DATE_ADD(@chat_start, INTERVAL 69 MINUTE), 'demo-mu'),
(25, '林间', '/demo/avatars/lin.png', '文章评论的 parent 和 reply 两个字段也值得分开理解。', DATE_ADD(@chat_start, INTERVAL 72 MINUTE), 'demo-lin'),
(26, '糖糖', '/demo/avatars/tang.png', '我在评论系统那篇里看到示例了，一个确定讨论串，一个确定回复对象。', DATE_ADD(@chat_start, INTERVAL 75 MINUTE), 'demo-tang'),
(27, 'power', '/demo/avatars/power.png', '对，回复二级评论时仍然归在原来的一级评论下面。', DATE_ADD(@chat_start, INTERVAL 78 MINUTE), 'demo-power'),
(28, '小雨', '/demo/avatars/yu.png', '楼中楼回复按时间正序读起来更顺，外层则可以把新讨论放前面。', DATE_ADD(@chat_start, INTERVAL 81 MINUTE), 'demo-yu'),
(29, '木木', '/demo/avatars/mu.png', '准备数据的时候，也别忘了把可见评论设置为审核通过。', DATE_ADD(@chat_start, INTERVAL 84 MINUTE), 'demo-mu'),
(30, '林间', '/demo/avatars/lin.png', '我之前就遇到过数据库有记录，但前台一直显示零条的情况。', DATE_ADD(@chat_start, INTERVAL 87 MINUTE), 'demo-lin'),
(31, '糖糖', '/demo/avatars/tang.png', '除了写代码，大家会固定做每周复盘吗？', DATE_ADD(@chat_start, INTERVAL 90 MINUTE), 'demo-tang'),
(32, '小雨', '/demo/avatars/yu.png', '会，简单记一下做成的事、遇到的卡点，还有下周先做什么。', DATE_ADD(@chat_start, INTERVAL 93 MINUTE), 'demo-yu'),
(33, '木木', '/demo/avatars/mu.png', '控制在一页以内比较容易坚持，写太长反而会一直拖。', DATE_ADD(@chat_start, INTERVAL 96 MINUTE), 'demo-mu'),
(34, '林间', '/demo/avatars/lin.png', '我更喜欢给没完成的任务留一个具体下一步，下次能直接继续。', DATE_ADD(@chat_start, INTERVAL 99 MINUTE), 'demo-lin'),
(35, 'power', '/demo/avatars/power.png', '这也是开发工具箱那篇想记录的习惯：让下一次开始更轻松一点。', DATE_ADD(@chat_start, INTERVAL 102 MINUTE), 'demo-power'),
(36, '糖糖', '/demo/avatars/tang.png', '今天打算整理一下工作台，然后去散步。', DATE_ADD(@chat_start, INTERVAL 105 MINUTE), 'demo-tang'),
(37, '小雨', '/demo/avatars/yu.png', '赞同，换个环境回来，卡住的问题有时就想通了。', DATE_ADD(@chat_start, INTERVAL 108 MINUTE), 'demo-yu'),
(38, '木木', '/demo/avatars/mu.png', '我先把那几篇文章收藏好，晚点再看评论里的补充。', DATE_ADD(@chat_start, INTERVAL 111 MINUTE), 'demo-mu'),
(39, '林间', '/demo/avatars/lin.png', '这组内容可以把首页、归档、搜索和聊天室都串起来测试了。', DATE_ADD(@chat_start, INTERVAL 114 MINUTE), 'demo-lin'),
(40, 'power', '/demo/avatars/power.png', '欢迎继续交流。文章相关的问题也可以留在对应评论区，方便后来的读者查找。', DATE_ADD(@chat_start, INTERVAL 117 MINUTE), 'demo-power');

UPDATE t_category c
SET c.articles_total = (SELECT COUNT(*) FROM t_article_category_rel r WHERE r.category_id = c.id);
UPDATE t_tag t
SET t.articles_total = (SELECT COUNT(*) FROM t_article_tag_rel r WHERE r.tag_id = t.id);

INSERT INTO t_statistics_article_pv (pv_date, pv_count, create_time, update_time) VALUES
(DATE_SUB(@seed_day, INTERVAL 6 DAY), 88, NOW(), NOW()),
(DATE_SUB(@seed_day, INTERVAL 5 DAY), 105, NOW(), NOW()),
(DATE_SUB(@seed_day, INTERVAL 4 DAY), 96, NOW(), NOW()),
(DATE_SUB(@seed_day, INTERVAL 3 DAY), 132, NOW(), NOW()),
(DATE_SUB(@seed_day, INTERVAL 2 DAY), 154, NOW(), NOW()),
(DATE_SUB(@seed_day, INTERVAL 1 DAY), 118, NOW(), NOW()),
(@seed_day, 163, NOW(), NOW());

COMMIT;
