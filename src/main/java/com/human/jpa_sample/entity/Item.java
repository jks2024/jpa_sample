package com.human.jpa_sample.entity;

import com.human.jpa_sample.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity // 해당 클래스는 DB Table로 생성됨
@Table (name="item")
public class Item {
    @Id   // 해당 필드가 기본키임을 지정
    @Column(name="item_id")  // 테이블의 컬럼 이름을 item_id 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 해당 DB에 생성 전략을 위임
    private Long id;   // 상품 코드

    @Column(nullable = false, length = 50)  // 길이를 50자로 제한
    private String itemNm; // 상품명

    @Column(nullable = false)
    private int price;     // 가격

    @Column(nullable = false)
    private int stockNumber;  // 재고 수량

    @Lob  // 대용량 데이터
    @Column(nullable = false)
    private String itemDetail;  // 상품 상세 설명

    @Enumerated(EnumType.STRING)  // 문자열 자체가 전달 됨
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태, 열거형

    private LocalDateTime regTime;  // 등록 시간
    private LocalDateTime updateTime;  // 수정 시간
}
