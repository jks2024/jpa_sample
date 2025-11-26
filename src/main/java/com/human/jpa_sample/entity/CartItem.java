package com.human.jpa_sample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class CartItem {
    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne  // 여러 CartItem은 하나의 Cart에 속하므로 N:1 관계
    @JoinColumn(name="cart_id")  // Cart의 PK를 참조키로 사용 함
    private Cart cart;  // 이미 만들어져 있는 Cart 객체 대입

    @ManyToOne  // 여러 CartItem이 하나의 Item(상품)에 속할 수 있으므르ㅗ N:1
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

}
