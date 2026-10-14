package com.example.weblog.admin.event;

import org.springframework.context.ApplicationEvent;
import lombok.Getter;

@Getter
public class UpdateArticleEvent extends ApplicationEvent {

    private long articleId;

    public UpdateArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
