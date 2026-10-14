package com.example.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_article_category_rel")
public class ArticleCategoryRelDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;

    private Long categoryId;
}

