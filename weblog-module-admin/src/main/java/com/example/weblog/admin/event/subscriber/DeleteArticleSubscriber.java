package com.example.weblog.admin.event.subscriber;

import com.example.weblog.admin.event.DeleteArticleEvent;
import com.example.weblog.admin.service.AdminStatisticsService;
import com.example.weblog.search.LuceneHelper;
import com.example.weblog.search.index.ArticleIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class DeleteArticleSubscriber {

    @Autowired
    private LuceneHelper luceneHelper;

    @Autowired
    private AdminStatisticsService statisticsService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(DeleteArticleEvent event) {
        Long articleId = event.getArticleId();

        String threadName = Thread.currentThread().getName();
        log.info("==> threadName: {}", threadName);
        log.info("==> 文章删除事件消费成功，articleId: {}", articleId);

        // 删除条件，根据文章 id 来删除
        Term condition = new Term(ArticleIndex.COLUMN_ID, String.valueOf(articleId));

        long count = luceneHelper.deleteDocument(ArticleIndex.NAME, condition);

        log.info("==> 删除文章对应 Lucene 文档结束，articleId: {}，受影响行数: {}", articleId, count);

        // 重新统计各分类下文章总数
        statisticsService.statisticsCategoryArticleTotal();
        log.info("==> 重新统计各分类下文章总数");

        // 重新统计各标签下文章总数
        statisticsService.statisticsTagArticleTotal();
        log.info("==> 重新统计各标签下文章总数");

    }
}
