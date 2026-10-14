package com.example.weblog.web.model.vo.article;

import com.example.weblog.common.model.BasePageQuery;
import io.swagger.annotations.*;
import lombok.*;

@Data
@Builder
@ApiModel(value = "首页查询文章分页 VO")
public class FindIndexArticlePageListReqVO extends BasePageQuery {
}

