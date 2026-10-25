package com.example.weblog.web.service;

import com.example.weblog.common.utils.Response;
import com.example.weblog.web.model.vo.chatroom.FindChatMessagePageListReqVO;
import com.example.weblog.web.model.vo.chatroom.FindChatMessagePageListRspVO;
import com.example.weblog.web.model.vo.chatroom.OnlineUserVO;

import java.util.List;

public interface ChatRoomService {

    /**
     * 获取历史消息
     * @param findChatMessagePageListReqVO
     * @return
     */
    Response<FindChatMessagePageListRspVO> findHistoryMessages(FindChatMessagePageListReqVO findChatMessagePageListReqVO);

    /**
     * 获取所有在线用户
     * @return
     */
    Response<List<OnlineUserVO>> findOnlineUsers();
}
