package com.example.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_article_tag_rel")
public class ArticleTagRelDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;

    private Long tagId;
}

