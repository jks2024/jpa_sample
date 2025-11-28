package com.human.jpa_sample.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardResDto {
    private Long boardId;
    private String email;
    private String title;
    private String content;
    private String img;
    private LocalDateTime regDate;
}
