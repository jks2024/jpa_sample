package com.human.jpa_sample.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class CommentWriteDto {
    private Long boardId;
    private String email;
    private String content;
}
