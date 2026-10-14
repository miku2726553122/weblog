package com.example.weblog.admin.service;

import com.example.weblog.admin.model.vo.tag.AddTagReqVO;
import com.example.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.example.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.example.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.example.weblog.common.utils.PageResponse;
import com.example.weblog.common.utils.Response;

public interface AdminTagService {

    /**
     * 添加标签集合
     * @param addTagReqVO
     * @return
     */
    Response addTag(AddTagReqVO addTagReqVO);

    /**
     * 查询标签分页
     * @param findTagPageListReqVO
     * @return
     */
    PageResponse findTagList(FindTagPageListReqVO findTagPageListReqVO);

    /**
     * 删除标签
     * @param deleteTagReqVO
     * @return
     */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);

    /**
     * 标签模糊查询
     * @param searchTagReqVO
     * @return
     */
    Response searchTag(SearchTagReqVO searchTagReqVO);

    /**
     * 查询标签 Select 列表数据
     * @return
     */
    Response findTagSelectList();
}

