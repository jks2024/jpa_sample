package com.human.jpa_sample.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Board {
    // JPA 내부에 관리하는 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;  // 게시글번호로 사용 함

    // 제목은 문자열로 길이를 256으로 제한
    @Column(length = 256, nullable = false)
    private String title;

    // 대용량 텍스트 작성을 위해 @Lob 사용, 내용 필드
    @Lob
    private String content;

    // 작성 날짜 및 시간
    private LocalDateTime createTime;

    // 이미지 경로
    private String image;

    // 시간은 Entity Manager가 insert 하기 직전에 자동으로 추가 되도록 함
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }

    // N:1관계로 회원 객체 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
