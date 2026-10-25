package com.example.weblog.web.controller;

import com.example.weblog.common.aspect.ApiOperationLog;
import com.example.weblog.common.utils.Response;
import com.example.weblog.web.model.vo.chatroom.FindChatMessagePageListReqVO;
import com.example.weblog.web.model.vo.chatroom.FindChatMessagePageListRspVO;
import com.example.weblog.web.model.vo.chatroom.OnlineUserVO;
import com.example.weblog.web.service.ChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Api(tags = "聊天室")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/message/history")
    @ApiOperation(value = "获取历史消息")
    @ApiOperationLog(description = "获取历史消息")
    public Response<FindChatMessagePageListRspVO> findHistoryMessages(@RequestBody @Validated FindChatMessagePageListReqVO findChatMessagePageListReqVO) {
        return chatRoomService.findHistoryMessages(findChatMessagePageListReqVO);
    }

    @PostMapping("/online/users")
    @ApiOperation(value = "获取所有在线用户")
    @ApiOperationLog(description = "获取所有在线用户")
    public Response<List<OnlineUserVO>> findOnlineUsers() {
        return chatRoomService.findOnlineUsers();
    }
}
