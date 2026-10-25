package com.example.weblog.web.model.vo.chatroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindChatMessagePageListRspVO {

    /**
     * 聊天消息
     */
    private List<ChatMessageVO> messages;

    /**
     * 是否还有下一页
     */
    private Boolean hasMore;

}
