package com.example.weblog.web.service;

import com.example.weblog.common.utils.Response;
import com.example.weblog.web.model.vo.archive.FindArchiveArticlePageListReqVO;
import com.example.weblog.web.model.vo.archive.FindArticleDetailReqVO;

public interface ArchiveService {
    /**
     * 获取文章归档分页数据
     * @param findArchiveArticlePageListReqVO
     * @return
     */
    Response findArchivePageList(FindArchiveArticlePageListReqVO findArchiveArticlePageListReqVO);

}

