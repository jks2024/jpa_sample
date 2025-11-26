package com.human.jpa_sample.repository;

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
class OrderRepositoryTest {
    @Autowired OrderRepository orderRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;
    // @Autowired OrderItemRepository orderItemRepository;
    @PersistenceContext EntityManager em;

    @Test
    @DisplayName("영속성 전이(Cascade) 테스트")
    public void cascadeTest() {
        Order order = new Order();  // 주문서 객체 생성

        // 상품 3개 생성 주문 목록 생성
        for (int i = 0; i < 3; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품 " + i);
            item.setPrice(10000 + i);
            item.setStockNumber(100);
            item.setItemDetail("상품 상세 설명 테스트 " + i);
            itemRepository.save(item);

            // 주문 목록 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000 + i);
            orderItem.setOrder(order);  // 자식이 부모 객체를 참조
            // 부모가 자식 객체를 생성 함 (orderItemRepository.save(orderItem))
            // 반대로 부모가 delete()되면 자식도 함께 delete() 됨
            order.getOrderItems().add(orderItem);
        }

        // 주문 저장을 위해 회원 정보 생성
        Member member = new Member();
        member.setName("안유진");
        member.setEmail("ayj@gmail.com");
        member.setPwd("sphb8250");
        member.setImage("http://google.co.kr");
        memberRepository.save(member);

        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        // orderItems 리스트에 3개의 상품이 들어 있는 상태에서 Order만 저장
        // CascadeType.ALL 때문에 orderItem들도 자동으로 insert 쿼리가 나감
        orderRepository.save(order);

        em.flush();  // DB에 강제로 쓰기(insert) (원래는 에티티매니저가 적절한 시점에 쓰기 작업 진행)
        em.clear();

        // 저장된 주문 조회
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        log.info("주문 ID : {}", savedOrder.getId());
        log.info("주문 상품 개수 : {}", savedOrder.getOrderItems().size());
        log.info("첫번째 상품명 : {}", savedOrder.getOrderItems().get(0).getItem());;

        // 고아 객체 제거하기 : 위의 코드를 재 사용해서 조회를 해보고 이 후에 List에 대해 remove(인덱스)를 했을 때 delete쿼리가 날아가는 지 확인
        log.info("=============== 고아 객체 제거 로직 시작 ===============");
        savedOrder.getOrderItems().remove(0);
        em.flush();
        em.clear();
        Order finalOrder = orderRepository.findById(order.getId())
                        .orElseThrow(EntityNotFoundException::new);
        log.info("고아 객체 제거 후 상품 개수 확인 : {}", finalOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("지연 로딩 쿼리 시점 확인 테스트")
    public void lazyLoadingTest() {
        Order order = new Order();

        for (int i = 0; i < 2; i++) {
            Item item = new Item();
            item.setItemNm("지연로딩 상품 " + i);
            item.setPrice(10000);
            item.setStockNumber(100);
            item.setItemDetail("상세 설명 " + i);
            itemRepository.save(item);

            // 주문 목록 생성
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000 + i);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        // 주문 저장을 위해 회원 정보 생성
        Member member = new Member();
        member.setName("안유진");
        member.setEmail("ayj@gmail.com");
        member.setPwd("sphb8250");
        member.setImage("http://google.co.kr");
        memberRepository.save(member);

        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        em.flush();  // DB에 강제로 쓰기(insert) (원래는 에티티매니저가 적절한 시점에 쓰기 작업 진행)
        em.clear();

        log.info("================ Order 조회 시작 ==================");
        // 조회 쿼리가 한번만 나감
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        log.info("주문자 이메일 조회 : {}", savedOrder.getMember().getEmail());  // 주문 아이템에 대한 쿼리 안나감

        log.info("================= OrderItem 실제 사용 ===============");  // 이때 조회 쿼리가 나감
        log.info("아이템의 개수 : {}", savedOrder.getOrderItems().size());
    }

}