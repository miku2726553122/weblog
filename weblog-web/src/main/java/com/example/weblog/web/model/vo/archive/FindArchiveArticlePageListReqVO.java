package com.example.weblog.web.model.vo.archive;

import com.example.weblog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@Builder
@ApiModel(value = "文章归档分页 VO")
public class FindArchiveArticlePageListReqVO extends BasePageQuery {
}

