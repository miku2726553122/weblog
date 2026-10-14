package com.example.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.weblog.common.domain.dos.ArticleDO;
import com.example.weblog.common.domain.mapper.ArticleMapper;
import com.example.weblog.common.domain.mapper.CategoryMapper;
import com.example.weblog.common.domain.mapper.TagMapper;
import com.example.weblog.common.utils.Response;
import com.example.weblog.web.model.vo.statistics.FindStatisticsInfoRspVO;
import com.example.weblog.web.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public Response findInfo() {
        // 查询文章总数
        Long articleTotalCount = articleMapper.selectCount(Wrappers.emptyWrapper());

        // 查询分类总数
        Long categoryTotalCount = categoryMapper.selectCount(Wrappers.emptyWrapper());

        // 查询标签总数
        Long tagTotalCount = tagMapper.selectCount(Wrappers.emptyWrapper());

        // 总浏览量
        List<ArticleDO> articleDOS = articleMapper.selectAllReadNum();
        Long pvTotalCount = 0L;

        if (!CollectionUtils.isEmpty(articleDOS)){
            // 所有 read_num 相加
            pvTotalCount = articleDOS.stream().mapToLong(ArticleDO::getReadNum).sum();
        }
        // 组装 VO 类

        FindStatisticsInfoRspVO vo=FindStatisticsInfoRspVO.builder()
                .articleTotalCount(articleTotalCount)
                .categoryTotalCount(categoryTotalCount)
                .tagTotalCount(tagTotalCount)
                .pvTotalCount(pvTotalCount)
                .build();

        return Response.success(vo);
    }
}
