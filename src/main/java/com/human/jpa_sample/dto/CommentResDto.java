package com.human.jpa_sample.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class CommentResDto {
    private Long commentId;
    private Long boardId;
    private String email;
    private String content;
    private LocalDateTime regDate;
}
