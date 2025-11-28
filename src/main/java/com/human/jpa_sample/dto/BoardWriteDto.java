package com.human.jpa_sample.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class BoardWriteDto {
    private String email;  // 현재는 작성자 확인을 위해 이메일을 전달 함
    private String title;  // 게시글 제목
    private String content;  // 게시글 내용
    private String img;   // 이미지
}
