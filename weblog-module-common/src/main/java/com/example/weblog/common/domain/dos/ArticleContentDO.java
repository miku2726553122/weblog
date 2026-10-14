package com.example.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_article_content")
public class ArticleContentDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;

    private String content;

}

