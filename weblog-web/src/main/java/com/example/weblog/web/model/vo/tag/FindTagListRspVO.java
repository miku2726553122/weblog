package com.example.weblog.web.model.vo.tag;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindTagListRspVO {
    private Long id;
    private String name;
    private Integer articlesTotal;
}

