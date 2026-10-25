package com.example.weblog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_chat_message")
public class ChatMessageDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String nickname;

    private String avatar;

    private String content;

    private LocalDateTime createTime;

    private String qq;

}
