# 本地演示数据

`demo-data.sql` 用于替换本机 `weblog` 数据库中的内容数据：

- 12 篇文章，包含摘要、Markdown 正文、代码、表格及列表。
- 4 个分类、12 个标签，关联数量按实际关系重新统计。
- 60 条已审核评论，每篇 3 条一级评论和 2 条回复，其中包含对回复的回复。
- 40 条聊天消息，支持连续加载 4 页历史记录。
- 最近 7 天的演示访问统计。

文章日期分布在导入日期之前的不同月份，聊天消息均早于导入时间。评论邮箱使用 `example.test`，聊天身份使用 `demo-` 前缀。

SQL 使用事务替换上述内容，保留 `t_user`、`t_user_role` 和 `t_blog_settings`。执行前应先备份这 9 张内容表；本次操作的备份位于 `backups/before-demo-20261025-175727/`，包含原始 SQL 和两处搜索索引副本，备份目录已排除 Git 跟踪。

在本地 MySQL 客户端中选择 `weblog` 数据库后，可以通过 `SOURCE scripts/demo-data.sql;` 重新导入。需使用 UTF-8 连接，且不要启用忽略错误的 `--force` 选项。重新导入后重启后端，使现有初始化逻辑重建文章搜索索引。本次导入的数据库和搜索索引均已同步完成。

## 本地图片

封面和头像位于前端 `public/demo/`，数据库只保存 `/demo/` 开头的路径，页面加载时不依赖外部图片服务。5 张字母头像为本地生成的演示图片。

4 张封面来自 Unsplash，已调整为 960 × 600 的 WebP：

- `frontend.webp`：[工作台](https://images.unsplash.com/photo-1498050108023-c5249f4df085)
- `backend.webp`：[电子电路](https://images.unsplash.com/photo-1518770660439-4636190af475)
- `project.webp`：[数据分析](https://images.unsplash.com/photo-1551288049-bebda4e38f71)
- `life.webp`：[日常工作](https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d)
