package com.human.jpa_sample.entity;

import com.human.jpa_sample.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 한명의 회원은 여러 번 주문할 수 있으므로 회원 <-> 주문은 1:N 관계 매핑
@Entity
@Table(name = "orders")
@Getter @Setter @ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne  // N:1 관계
    @JoinColumn(name = "member_id") // 회원 객체의 PK
    private Member member;   // 회원 객체의 주소를 전달 받음

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)  // DB 에 전달될 때 상수의 문자열이 저장됨
    private OrderStatus orderStatus;  // ORDER, CANCEL 만 올수 있음

    private LocalDateTime regTime;  // 등록일
    private LocalDateTime updateTime; // 수정일

    // 여기가 주인이 아니므로 mappedBy 속성으로 연관관계의 주인을 설정
    // 부모 엔티티의 모든 상태를 자식에서 전달 함, 부모가 지워 질 때 자식을 함께 삭제하는 경우
    // orphanRemoval = true : 고아 객체 제거, 부모와 자식 관계가 끊어지는 경우 DB를 자동 삭제하는 기능, 주로 한군데에서만 참조되는 엔티티에 사용
    @OneToMany (mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY  // 실제 데이터가 필요한 순간까지 DB 조회를 미루는 것, @OneToMany 기본값이 LAZY
    )
    private List<OrderItem> orderItems = new ArrayList<>();
}
