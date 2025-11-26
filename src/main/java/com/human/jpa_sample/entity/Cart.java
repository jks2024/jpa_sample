package com.human.jpa_sample.entity;
// Memeber <-> Cart : 1대1 단방향 연관관계 매핑

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter @Setter @ToString
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cartName;

    @OneToOne  // 회원 엔티티와 일대일 매핑
    @JoinColumn(name="member_id")  // 매핑할 외래키 지정  (member table의 pk를 fk로 지정)
    private Member member;  // 맴버 객체를 추가, 실제 동작에서는 회원이 이미 가입되어 있기 때문에 findByEmail() 가져와서 대입
}
