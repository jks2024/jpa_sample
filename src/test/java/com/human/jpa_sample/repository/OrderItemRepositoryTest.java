package com.human.jpa_sample.repository;
// 1. 회원 가입
// 2. 상품 등록
// 3. 주문서 생성
// 4. 주문 목록 작성
// 5. 주문 목록 검증 (생성된 주문 목록과 조회한 주문 목록이 같은지 확인)

import com.human.jpa_sample.constant.ItemSellStatus;
import com.human.jpa_sample.constant.OrderStatus;
import com.human.jpa_sample.entity.Item;
import com.human.jpa_sample.entity.Member;
import com.human.jpa_sample.entity.Order;
import com.human.jpa_sample.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Slf4j
class OrderItemRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderItemRepository orderItemRepository;
    @PersistenceContext EntityManager em;

    // 회원 정보 생성 메서드
    public Member createMember() {
        Member member = new Member();
        member.setName("곰돌이");
        member.setEmail("jks2024@gmail.co,");
        member.setPwd("sphb8250");
        member.setImage("http://google.co.kr");
        return memberRepository.save(member);  // insert와 update
    }

    // 상품 생성 메서드
    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setItemDetail("테스트 상품 상세 설명");
        item.setPrice(10000);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    // 주문서 생성 메서드
    public Order createOrder() {
        Member member = createMember();
        Order order = new Order();
        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // 주문 목록 작성
    @Test
    @DisplayName("상품 주문 단위 테스트")
    public void orderItemTest() {
        Order order = createOrder();
        Item item = createItem();
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(item);
        orderItem.setOrderPrice(35000); // 가격
        orderItem.setCount(2);  // 주문 수량
        orderItem.setRegTime(LocalDateTime.now());

        // 주문 목록 저장
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // 영속성 컨텍스트 사용 (이건 테스트 환경이라서 사용)
        em.flush();  // DB에 즉시 반영
        em.clear();  // 1타 캐시 비우기

        // 주문 목록 검증 : 저장된 항목 조회해서 가져 오기
        OrderItem findOrderItem = orderItemRepository.findById(orderItem.getId())
                .orElseThrow(EntityNotFoundException::new);

        log.info("============================================");
        log.info("주문 상품에 대한 저장 정보 : {}", savedOrderItem);
        log.info("주문 상품에 대한 조회 정보 : {}", findOrderItem);
        log.info("============================================");
    }
}