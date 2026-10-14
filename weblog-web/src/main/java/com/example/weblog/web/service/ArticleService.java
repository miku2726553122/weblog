package com.example.weblog.web.service;

import com.example.weblog.common.utils.Response;
import com.example.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;
import com.example.weblog.web.model.vo.archive.FindArticleDetailReqVO;

public interface ArticleService {
    /**
     * 获取首页文章分页数据
     * @param findIndexArticlePageListReqVO
     * @return
     */
    Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO);

    /**
     * 获取文章详情
     * @param findArticleDetailReqVO
     * @return
     */
    Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO);
}

