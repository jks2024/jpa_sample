package com.human.jpa_sample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem {  // order_item 으로 테이블이 생성 됨
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;  // 관례상 이렇게 사용, findById()는 @Id로 확인

    @ManyToOne
    @JoinColumn(name = "item_id") // item의 PK를 참조키로 사용
    private Item item;  // 주문 아이템을 만들기 위해서는 미리 제품이 있어야 하고, 한개의 제품의 여러개의 주문 목록이 될 수 있음 (N:1)

    @ManyToOne
    @JoinColumn(name = "order_id")  // order의 PK를 참조키로 사용
    private Order order;  // 주문 목록을 담기 위해서는 주문서가 미리 만들어져 있어야 함

    private int orderPrice;
    private int count;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
