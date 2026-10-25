package com.example.weblog.web.service.impl;

import com.example.weblog.common.domain.dos.ChatMessageDO;
import com.example.weblog.common.domain.mapper.ChatMessageMapper;
import com.example.weblog.web.model.vo.chatroom.ChatMessageVO;
import com.example.weblog.web.model.vo.chatroom.FindChatMessagePageListReqVO;
import com.example.weblog.web.model.vo.chatroom.FindChatMessagePageListRspVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {
    @Mock
    private ChatMessageMapper chatMessageMapper;

    @InjectMocks
    private ChatRoomServiceImpl service;

    @Test
    void emptyHistoryReturnsAnEmptyPage() {
        when(chatMessageMapper.selectRecentMessages(11)).thenReturn(Collections.emptyList());

        FindChatMessagePageListRspVO page = service.findHistoryMessages(
                new FindChatMessagePageListReqVO(null, null)).getData();

        assertNotNull(page);
        assertTrue(page.getMessages().isEmpty());
        assertFalse(page.getHasMore());
    }

    @Test
    void historyReturnsTenMessagesInChronologicalOrderAndChecksForAnExtraRow() {
        when(chatMessageMapper.selectRecentMessages(11)).thenReturn(messages(11));

        FindChatMessagePageListRspVO page = service.findHistoryMessages(
                new FindChatMessagePageListReqVO(null, "123456")).getData();

        assertEquals(10, page.getMessages().size());
        assertEquals(Long.valueOf(2), page.getMessages().get(0).getId());
        assertEquals(Long.valueOf(11), page.getMessages().get(9).getId());
        assertTrue(page.getMessages().stream().allMatch(ChatMessageVO::getIsSelf));
        assertTrue(page.getHasMore());
    }

    @Test
    void exactlyTenRemainingRowsAreTheLastPage() {
        when(chatMessageMapper.selectMessagesBefore(20L, 11)).thenReturn(messages(10));

        FindChatMessagePageListRspVO page = service.findHistoryMessages(
                new FindChatMessagePageListReqVO(20L, "another-user")).getData();

        assertFalse(page.getHasMore());
        assertEquals(Long.valueOf(1), page.getMessages().get(0).getId());
        assertTrue(page.getMessages().stream().noneMatch(ChatMessageVO::getIsSelf));
        verify(chatMessageMapper).selectMessagesBefore(20L, 11);
    }

    @Test
    void visitorsDoNotOwnMessages() {
        when(chatMessageMapper.selectRecentMessages(11)).thenReturn(messages(1));

        FindChatMessagePageListRspVO page = service.findHistoryMessages(
                new FindChatMessagePageListReqVO(null, null)).getData();

        assertFalse(page.getMessages().get(0).getIsSelf());
    }

    private List<ChatMessageDO> messages(int count) {
        List<ChatMessageDO> messages = new ArrayList<>();
        for (long id = count; id > 0; id--) {
            messages.add(ChatMessageDO.builder()
                    .id(id)
                    .nickname("Alice")
                    .qq("123456")
                    .content("message-" + id)
                    .createTime(LocalDateTime.of(2026, 10, 25, 12, 0))
                    .build());
        }
        return messages;
    }
}
