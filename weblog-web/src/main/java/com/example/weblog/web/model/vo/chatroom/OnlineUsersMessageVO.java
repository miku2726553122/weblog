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
public class OnlineUsersMessageVO {

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 在线用户列表
     */
    private List<OnlineUserVO> users;

    /**
     * 在线人数
     */
    private Integer onlineCount;
}
