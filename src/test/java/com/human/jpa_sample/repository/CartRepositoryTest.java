package com.human.jpa_sample.repository;

import com.human.jpa_sample.entity.Cart;
import com.human.jpa_sample.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest // Spring Boot Test Logic 임을 나타내는 어노테이션
@Transactional  // 물리적인 작언을 하나의 논리적인 작업을 묶을 때 사용, 원래는 실패 시 자동 롤백이지만 테스트 로직에서는 무조건 롤백
@Slf4j
@TestPropertySource(locations="classpath:application-test.properties")  // 테스트에서 사용하는 포로퍼티 지정
class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext  // JPA의 EntityManager를 주입, 일반적인 경우에는 사용 필요성이 별로 없음
    EntityManager em;

    // 회원 엔티티 생성 => 실제 로직에서는 회원 가입 후 장바구니를 만들기 때문에 이 작업은 회원 조회로 대체 됨
    public Member createMemberInfo() {
        Member member = new Member();
        member.setEmail("jks2024@gmail.com");
        member.setPwd("sphb8250");
        member.setName("곰돌이사육사");
        member.setImage("http://google.co.kr");
        member.setRegDate(LocalDateTime.now());
        return member;
    }

    @Test
    @DisplayName("장바구니 회원 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMemberInfo();
        memberRepository.save(member);  // insert 구문과 동일, 회원 정보를 객체로 만들어서 DB insert

        Cart cart = new Cart();
        cart.setCartName("곰돌이 카트");
        cart.setMember(member);
        cartRepository.save(cart);  // 장바구니 생성

        em.flush();  // 영속성 컨텍스트에 데이터 저장 후 flush() 호출하여 데이터베이스에 반영
        em.clear();  // 영속성 컨텍스트를 비움

        Cart saveCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        log.info("cart : {}", saveCart);
        if (saveCart.getMember().getEmail().equals(member.getEmail())) {
            log.info("장바구니가 정상적으로 생성 되었습니다.");
        }
    }






}