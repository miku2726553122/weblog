package com.example.weblog.admin.event.subscriber;

import com.example.weblog.admin.event.PublishArticleEvent;
import com.example.weblog.admin.service.AdminStatisticsService;
import com.example.weblog.common.constant.Constants;
import com.example.weblog.common.domain.dos.ArticleContentDO;
import com.example.weblog.common.domain.dos.ArticleDO;
import com.example.weblog.common.domain.mapper.ArticleContentMapper;
import com.example.weblog.common.domain.mapper.ArticleMapper;
import com.example.weblog.search.LuceneHelper;
import com.example.weblog.search.index.ArticleIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class PublishArticleSubscriber {

    @Autowired
    private LuceneHelper luceneHelper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private AdminStatisticsService statisticsService;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(PublishArticleEvent event) {
        Long articleId = event.getArticleId();

        String threadName = Thread.currentThread().getName();
        log.info("==> threadName: {}", threadName);
        log.info("==> 文章发布事件消费成功，articleId: {}", articleId);

        // 搜索新发布的文章（事务已提交，能查到最新数据）
        ArticleDO articleDO = articleMapper.selectById(articleId);
        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);

        // 构建文档
        Document document = new Document();
        // ID 字段必须使用 StringField，避免分词导致精确匹配失败
        document.add(new StringField(ArticleIndex.COLUMN_ID, String.valueOf(articleId), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_TITLE, articleDO.getTitle(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_COVER, articleDO.getCover(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_SUMMARY, articleDO.getSummary(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_CONTENT, articleContentDO.getContent(), Field.Store.YES));
        document.add(new TextField(ArticleIndex.COLUMN_CREATE_TIME, Constants.DATE_TIME_FORMATTER.format(articleDO.getCreateTime()), Field.Store.YES));

        // 添加文档
        long count = luceneHelper.addDocument(ArticleIndex.NAME, document);

        log.info("==> 添加文章对应 Lucene 文档结束，articleId: {}，受影响行数: {}", articleId, count);

        // 重新统计各分类下文章总数
        statisticsService.statisticsCategoryArticleTotal();
        log.info("==> 重新统计各分类下文章总数");

        // 重新统计各标签下文章总数
        statisticsService.statisticsTagArticleTotal();
        log.info("==> 重新统计各标签下文章总数");
    }
}
