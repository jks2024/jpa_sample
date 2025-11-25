package com.human.jpa_sample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDateTime;

@Entity  // class로 DB Table 생성하기 위해 사용하는 어노테이션
@Table(name = "member")  // DB Table 생성 시 DB 테이블 이름을 지정 (지정하지 않으면 카멜표기법 -> 스네이크 표기법이 변경되어서 생성 됨)
@Getter  // Getter Method 자동 생성
@Setter  // Setter Method 자동 생성
@NoArgsConstructor  // 매개변수가 없는 기본 생성자 생성
@ToString(exclude = "pwd")   // pwd 는 ToString 변환시 제외
public class Member {
    @Id  // PK 역할을 하면 JPA에서는 반드시 있어여 함
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 생성전략을 DB 기준을 따름
    @Column(name = "member_id")
    private Long id;   // 클래스에서 해당 필드는 id로 만들어지지민 DB Table의 컬럼명은 member_id로 생성

    @Column(length = 100)  // 문자열 길이를 100글자로 제한
    private String name;

    @Column(nullable = false)  // NULL을 허용하지 않음
    private String pwd;

    @Column(unique = true, length = 150)  // 중복허용을 하지않고 길이를 150byte로 제한
    private String email;

    @Column(length = 255)
    private String image;

    private LocalDateTime regDate;  // reg_date로 컬럼이름이 생성 됨

    @PrePersist  // JPA Entity Maneger가 insert 직전에 해당 메서드를 자동으로 호출 하는 기능
    public void prePersist() {
        this.regDate = LocalDateTime.now();  // 현재 시간을 생성시간 이정 해줌
    }
}
