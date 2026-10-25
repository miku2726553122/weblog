package com.example.weblog.web.websocket;

import com.example.weblog.common.domain.dos.ChatMessageDO;
import com.example.weblog.common.domain.mapper.ChatMessageMapper;
import com.example.weblog.web.utils.SpringContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ChatWebSocketServerTest {
    private final ChatWebSocketServer server = new ChatWebSocketServer();
    private final ObjectMapper json = new ObjectMapper();
    private final List<Session> sessions = new ArrayList<>();
    private final Map<String, List<String>> received = new HashMap<>();
    private ChatMessageMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = mock(ChatMessageMapper.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(context.getBean(ChatMessageMapper.class)).thenReturn(mapper);
        new SpringContext(context);
    }

    @AfterEach
    void tearDown() {
        sessions.forEach(server::onClose);
    }

    @Test
    void closingAnOlderQqConnectionDoesNotRemoveTheNewConnection() throws Exception {
        Session older = connect("old", "123456");
        Session newer = connect("new", "123456");

        server.onClose(older);

        assertEquals(1, ChatWebSocketServer.getOnlineUsers().size());
        assertEquals("new", ChatWebSocketServer.getOnlineUsers().get(0).getSessionId());
        stubInsert();
        server.onMessage("hello", newer);
        assertEquals("hello", lastChat("new").get("content").asText());
    }

    @Test
    void bothQqTabsReceiveSavedMessagesWithTheSameDatabaseId() throws Exception {
        Session first = connect("first", "123456");
        Session second = connect("second", "123456");
        connect("other", "654321");
        stubInsert();

        server.onMessage(" hello ", first);

        JsonNode firstMessage = lastChat("first");
        JsonNode secondMessage = lastChat("second");
        JsonNode otherMessage = lastChat("other");
        assertEquals(42, firstMessage.get("id").asLong());
        assertEquals("hello", firstMessage.get("content").asText());
        assertEquals("123456", firstMessage.get("sessionId").asText());
        assertEquals(firstMessage, secondMessage);
        assertTrue(otherMessage.get("sessionId").isNull());
        assertEquals(firstMessage.get("id"), otherMessage.get("id"));

        server.onMessage("reply", second);
        assertEquals("reply", lastChat("second").get("content").asText());
        verify(mapper, times(2)).insert(any(ChatMessageDO.class));
    }

    @Test
    void failedRecipientDoesNotPreventDeliveryToTheSender() throws Exception {
        Session broken = connect("broken", "123456");
        Session sender = connect("sender", "654321");
        RemoteEndpoint.Basic brokenRemote = broken.getBasicRemote();
        doThrow(new IllegalStateException("closed")).when(brokenRemote).sendText(anyString());
        stubInsert();

        assertDoesNotThrow(() -> server.onMessage("hello", sender));

        assertEquals("hello", lastChat("sender").get("content").asText());
    }

    @Test
    void customNicknameReceivesItsSessionIdentity() throws Exception {
        Session guest = connect("guest", "");
        stubInsert();

        server.onMessage("hello", guest);

        assertEquals("guest", lastChat("guest").get("sessionId").asText());
    }

    @Test
    void blankMessagesAreNotPersisted() throws Exception {
        Session guest = connect("guest", "");

        server.onMessage(" \n ", guest);

        verify(mapper, never()).insert(any(ChatMessageDO.class));
    }

    private void stubInsert() {
        doAnswer(invocation -> {
            ChatMessageDO message = invocation.getArgument(0);
            message.setId(42L);
            return 1;
        }).when(mapper).insert(any(ChatMessageDO.class));
    }

    private Session connect(String id, String qq) throws IOException {
        Session session = mock(Session.class);
        RemoteEndpoint.Basic remote = mock(RemoteEndpoint.Basic.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("nickname", Collections.singletonList("Alice"));
        params.put("avatar", Collections.singletonList(""));
        params.put("qq", Collections.singletonList(qq));
        when(session.getId()).thenReturn(id);
        when(session.isOpen()).thenReturn(true);
        when(session.getRequestParameterMap()).thenReturn(params);
        when(session.getBasicRemote()).thenReturn(remote);
        List<String> inbox = new ArrayList<>();
        received.put(id, inbox);
        doAnswer(invocation -> {
            inbox.add(invocation.getArgument(0));
            return null;
        }).when(remote).sendText(anyString());
        sessions.add(session);
        server.onOpen(session);
        return session;
    }

    private JsonNode lastChat(String id) throws IOException {
        List<String> inbox = received.get(id);
        for (int index = inbox.size() - 1; index >= 0; index--) {
            JsonNode message = json.readTree(inbox.get(index));
            if (message.get("type").asInt() == 1) return message;
        }
        throw new AssertionError("No chat message received by " + id);
    }
}
