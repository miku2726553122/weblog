package com.example.weblog.web.model.vo.chatroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketChatMessageVO {
    private Long id;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private String time;

    /**
     * 在线人数
     */
    private Integer onlineCount;
    /**
     * 返回会话 ID
     */
    private String sessionId;
}
