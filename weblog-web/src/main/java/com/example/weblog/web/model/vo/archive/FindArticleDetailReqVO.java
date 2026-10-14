package com.example.weblog.web.model.vo.archive;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询文章详情 VO")
public class FindArticleDetailReqVO {
    /**
     * 文章 ID
     */
    private Long articleId;
}
