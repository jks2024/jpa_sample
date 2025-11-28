package com.human.jpa_sample.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString @NoArgsConstructor
public class BoardResDto {
    private Long boardId;
    private String email;
    private String title;
    private String content;
    private String img;
    private LocalDateTime regDate;
}
