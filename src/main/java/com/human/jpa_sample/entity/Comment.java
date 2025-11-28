package com.human.jpa_sample.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Comment {
    // 댓글 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    // 게시글과 ManyToOne 관계
    @ManyToOne(fetch = FetchType.LAZY)  // 지연 전략, 해당 내용이 실제로 필요할 때까지 로딩 하지 않음
    @JoinColumn(name = "board_id")
    private Board board;

    // 회원과 ManyToOne 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 댓글 내용
    @Column(length = 1000)
    private String content;

    // 댓글 작성 시간
    private LocalDateTime regTime;
    @PrePersist
    public void prePersist() {
        this.regTime = LocalDateTime.now();
    }
}
