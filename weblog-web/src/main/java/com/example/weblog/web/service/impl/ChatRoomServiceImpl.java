package com.example.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.weblog.common.domain.dos.ChatMessageDO;
import com.example.weblog.common.domain.mapper.ChatMessageMapper;
import com.example.weblog.common.utils.Response;
import com.example.weblog.web.enums.ChatRoomMessageTypeEnum;
import com.example.weblog.web.model.vo.chatroom.*;
import com.example.weblog.web.service.ChatRoomService;
import com.example.weblog.web.websocket.ChatWebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    /**
     * 获取历史消息
     *
     * @param findChatMessagePageListReqVO
     * @return
     */
    @Override
    public Response<FindChatMessagePageListRspVO> findHistoryMessages(FindChatMessagePageListReqVO findChatMessagePageListReqVO) {
        // 每页展示 10 条消息
        int pageSize = 10;
        // 游标消息 ID
        Long lastId = findChatMessagePageListReqVO.getLastId();
        // 当前用户的 sessionId
        String sessionId = findChatMessagePageListReqVO.getSessionId();

        // 查询指定页码记录
        List<ChatMessageDO> messages = Objects.isNull(lastId)
                ? chatMessageMapper.selectRecentMessages(pageSize + 1)
                : chatMessageMapper.selectMessagesBefore(lastId, pageSize + 1);
        boolean hasMore = messages.size() > pageSize;
        messages = new ArrayList<>(messages.subList(0, Math.min(messages.size(), pageSize)));

        // DO 实体类转 VO 实体类
        FindChatMessagePageListRspVO vo = FindChatMessagePageListRspVO.builder()
                .messages(Collections.emptyList())
                .hasMore(false)
                .build();

        if (!CollectionUtils.isEmpty(messages)) {
            // 倒序变正序
            Collections.reverse(messages);

            List<ChatMessageVO> voList = messages.stream().map(chatMessageDO -> {
                // 判断是否是当前用户发送的消息
                // 如果 DO 的 qq 不为空，并且入参提交的 sessionId 和 qq 号一致，则是自己发送的消息
                boolean isSelf = StringUtils.isNotBlank(chatMessageDO.getQq())
                        && StringUtils.isNotBlank(sessionId)
                        && Objects.equals(sessionId, chatMessageDO.getQq());

                return ChatMessageVO.builder()
                        .id(chatMessageDO.getId())
                        .type(ChatRoomMessageTypeEnum.CHAT.getCode())
                        .nickname(chatMessageDO.getNickname())
                        .avatar(chatMessageDO.getAvatar())
                        .content(chatMessageDO.getContent())
                        .time(chatMessageDO.getCreateTime().format(ChatWebSocketServer.TIME_FORMATTER))
                        .isSelf(isSelf)
                        .build();
            }).collect(Collectors.toList());

            vo = FindChatMessagePageListRspVO.builder()
                    .messages(voList)
                    .hasMore(hasMore)
                    .build();
        }

        return Response.success(vo);
    }

    @Override
    public Response<List<OnlineUserVO>> findOnlineUsers() {
        // 获取所有在线用户
        List<OnlineUserVO> onlineUsers = ChatWebSocketServer.getOnlineUsers();
        return Response.success(onlineUsers);
    }
}

