package com.example.weblog.web.websocket;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.weblog.common.domain.dos.ChatMessageDO;
import com.example.weblog.common.domain.mapper.ChatMessageMapper;
import com.example.weblog.common.utils.JsonUtil;
import com.example.weblog.web.enums.ChatRoomMessageTypeEnum;
import com.example.weblog.web.model.vo.chatroom.WebSocketChatMessageVO;
import com.example.weblog.web.model.vo.chatroom.OnlineUserVO;
import com.example.weblog.web.model.vo.chatroom.OnlineUsersMessageVO;
import com.example.weblog.web.model.vo.chatroom.UserInfoVO;
import com.example.weblog.web.utils.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ServerEndpoint("/ws/chat") // 定义 WebSocket 端点
public class ChatWebSocketServer {

    /**
     * 存储所有在线用户的会话 Session
     */
    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();
    /**
     * 存储用户信息
     */
    private static final Map<String, UserInfoVO> USER_INFO_MAP = new ConcurrentHashMap<>();
    /**
     * 时间格式化
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    /**
     * session.getId() 到 sessionKey 的映射（sessionKey 可能是 qq，也可能是 session.getId()）
     */
    private static final Map<String, String> SESSION_ID_TO_KEY_MAP = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        // 获取昵称、头像、qq 号
        Map<String, List<String>> params = session.getRequestParameterMap();
        String nickname = getFieldValueFromParam("nickname", params);
        String avatar = getFieldValueFromParam("avatar", params);
        String qq = getFieldValueFromParam("qq", params);

        // 如果是以 qq 号方式加入的聊天室，则以 qq 号作为会话 Key; 否则，以 sessionId 作为 Key
        String sessionKey = StringUtils.isNotBlank(qq) ? qq : session.getId();
        // 保存 session.getId() 到 sessionKey 的映射关系
        SESSION_ID_TO_KEY_MAP.put(session.getId(), sessionKey);
        // 保存会话
        SESSION_MAP.put(session.getId(), session);

        // 保存用户信息（如果使用 QQ 登录，则保存 QQ 号）
        USER_INFO_MAP.put(session.getId(), new UserInfoVO(nickname, avatar, qq));

        int count = SESSION_MAP.size();

        log.info("## 用户 [sessionKey:{}] [昵称:{}] 加入聊天室，当前在线人数: {}", sessionKey, nickname, count);

        // 返回自己的 sessionKey 给前端（只发送当前会话）
        sendMessage(session, buildSessionIdMessage(sessionKey));
        // 广播系统消息，告诉所有人，有用户加入了聊天室
        broadcastMessage(buildMessage(ChatRoomMessageTypeEnum.SYSTEM.getCode(), nickname, null, "加入了聊天室"));

        // 广播在线用户列表
        broadcastOnlineUsers();
    }

    /**
     * 构建返回 sessionId 消息 JSON 字符串
     */
    private String buildSessionIdMessage(String sessionId) {
        return JsonUtil.toJsonString(WebSocketChatMessageVO.builder().type(ChatRoomMessageTypeEnum.INIT.getCode()).sessionId(sessionId).build());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        // 通过 session.getId() 查找 sessionKey
        String sessionKey = SESSION_ID_TO_KEY_MAP.remove(session.getId());

        if (StringUtils.isBlank(sessionKey)) {
            log.warn("## 未找到对应的 sessionKey，[session.getId:{}]", session.getId());
            return;
        }

        // 如果会话存在
        if (SESSION_MAP.remove(session.getId(), session)) {
            // 获取用户信息
            UserInfoVO userInfoVO = USER_INFO_MAP.remove(session.getId());
            String nickname = Objects.nonNull(userInfoVO) ? userInfoVO.getNickname() : null;

            int count = SESSION_MAP.size();

            log.info("## 用户 [sessionKey:{}] [昵称:{}] 离开了聊天室，当前在线人数: {}", sessionKey, nickname, count);

            // 广播系统消息，告诉所有在线用户，有人离开了
            broadcastMessage(buildMessage(ChatRoomMessageTypeEnum.SYSTEM.getCode(), nickname, null, "离开了聊天室"));
            // 广播在线用户列表
            broadcastOnlineUsers();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        // 通过 session.getId() 查找 sessionKey
        String sessionKey = SESSION_ID_TO_KEY_MAP.get(session.getId());

        if (StringUtils.isBlank(sessionKey)) {
            log.warn("## 未找到对应的 sessionKey，[sessionId:{}]", session.getId());
            return;
        }

        // 获取对应用户信息
        UserInfoVO userInfoVO = USER_INFO_MAP.get(session.getId());

        // 校验用户信息是否存在
        if (Objects.isNull(userInfoVO)) {
            log.warn("## 用户信息不存在，[sessionKey:{}]", sessionKey);
            return;
        }

        String nickname = userInfoVO.getNickname();
        String avatar = userInfoVO.getAvatar();
        String qq = userInfoVO.getQq();


        log.info("## 收到用户 [sessionKey:{}] [昵称:{}] 的消息: {}", sessionKey, nickname, message);

        // 保存消息到数据库
        ChatMessageDO savedMessage = saveMessage(nickname, avatar, message.trim(), qq);


        // 广播聊天消息，通知所有在线用户（携带发送者的 sessionKey）
        broadcastChatMessage(savedMessage, sessionKey);
    }

    /**
     * 广播聊天消息给所有在线用户
     * 给发送者返回带 sessionKey 的消息，给其他用户返回不带 sessionKey 的消息
     */
    private void broadcastChatMessage(ChatMessageDO savedMessage, String senderSessionKey) {
        // 构建不带 sessionKey 的消息（发送给其他用户）
        String messageWithoutSessionKey = JsonUtil.toJsonString(WebSocketChatMessageVO.builder()
                .id(savedMessage.getId())
                .type(ChatRoomMessageTypeEnum.CHAT.getCode())
                .nickname(savedMessage.getNickname())
                .avatar(savedMessage.getAvatar())
                .content(savedMessage.getContent())
                .time(savedMessage.getCreateTime().format(TIME_FORMATTER))
                .onlineCount(SESSION_MAP.size())
                .sessionId(null)
                .build());

        // 构建带 sessionKey 的消息（发送给发送者自己）
        String messageWithSessionKey = JsonUtil.toJsonString(WebSocketChatMessageVO.builder()
                .id(savedMessage.getId())
                .type(ChatRoomMessageTypeEnum.CHAT.getCode())
                .nickname(savedMessage.getNickname())
                .avatar(savedMessage.getAvatar())
                .content(savedMessage.getContent())
                .time(savedMessage.getCreateTime().format(TIME_FORMATTER))
                .onlineCount(SESSION_MAP.size())
                .sessionId(senderSessionKey)
                .build());

        // 遍历所有在线用户
        SESSION_MAP.forEach((sessionId, session) -> {
            // 如果当前会话是发送者，则发送带 sessionKey 的消息；否则发送不带 sessionKey 的消息
            if (Objects.equals(SESSION_ID_TO_KEY_MAP.get(sessionId), senderSessionKey)) {
                sendMessage(session, messageWithSessionKey);
            } else {
                sendMessage(session, messageWithoutSessionKey);
            }
        });
    }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("## WebSocket 连接发生错误：", error);
    }

    /**
     * 发送消息给指定客户端
     */
    private void sendMessage(Session session, String message) {
        try {
            synchronized (session) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException | IllegalStateException e) {
            log.error("## 发送消息失败：", e);
        }
    }

    /**
     * 广播消息给所有在线用户
     */
    private void broadcastMessage(String message) {
        SESSION_MAP.values().forEach(session -> sendMessage(session, message));
    }

    /**
     * 获取对应参数
     *
     * @param fieldName
     * @param params
     * @return
     */
    private String getFieldValueFromParam(String fieldName, Map<String, List<String>> params) {
        // 如果包含对应字段，且不为空，则取出
        if (params.containsKey(fieldName) && !CollectionUtils.isEmpty(params.get(fieldName))) {
            return params.get(fieldName).get(0);
        }
        return null;
    }

    /**
     * 构建消息 JSON 字符串
     */
    private String buildMessage(Integer type, String nickname, String avatar, String content) {
        return JsonUtil.toJsonString(WebSocketChatMessageVO.builder().type(type).nickname(nickname).avatar(avatar).content(content).time(LocalDateTime.now().format(TIME_FORMATTER)).onlineCount(SESSION_MAP.size()).build());
    }

    /**
     * 保存消息到数据库
     */
    private ChatMessageDO saveMessage(String nickname, String avatar, String content, String qq) {
        ChatMessageDO chatMessage = ChatMessageDO.builder()
                .nickname(nickname)
                .avatar(avatar)
                .content(content)
                .qq(qq)
                .createTime(LocalDateTime.now())
                .build();
        ChatMessageMapper chatMessageMapper = SpringContext.getBean(ChatMessageMapper.class);
        chatMessageMapper.insert(chatMessage);
        return chatMessage;
    }

    /**
     * 广播在线用户列表
     */
    private void broadcastOnlineUsers() {
        // 构建在线用户列表 VO
        List<OnlineUserVO> users = getOnlineUsers();
        OnlineUsersMessageVO msg = OnlineUsersMessageVO.builder().type(ChatRoomMessageTypeEnum.ONLINE_USERS.getCode()).onlineCount(users.size()).users(users).build();

        // 广播消息
        broadcastMessage(JsonUtil.toJsonString(msg));
    }

    /**
     * 获取在线用户列表
     */
    public static List<OnlineUserVO> getOnlineUsers() {
        List<OnlineUserVO> users = new ArrayList<>();
        USER_INFO_MAP.forEach((sessionId, userInfo) -> {
            users.add(OnlineUserVO.builder().sessionId(sessionId).nickname(userInfo.getNickname()).avatar(userInfo.getAvatar()).online(true).build());
        });
        return users;
    }

}
