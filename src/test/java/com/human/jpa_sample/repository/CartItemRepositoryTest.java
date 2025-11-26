package com.human.jpa_sample.repository;

// 장바구니에 장바구니 아이템을 담아 보기
// 1. 회원 생성 및 저장
// 2. 상품 생성 및 저장
// 3. 장바구니 생정 및 저장
// 4. 장바구니 상품 생성 및 저장
// 5. 테스트 로직 작성

import com.human.jpa_sample.constant.ItemSellStatus;
import com.human.jpa_sample.entity.Cart;
import com.human.jpa_sample.entity.CartItem;
import com.human.jpa_sample.entity.Item;
import com.human.jpa_sample.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional  // 테스트 이후 자동 롤백
@Slf4j
class CartItemRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository  cartItemRepository;

    // 회원 정보 생성 메서드
    public Member createMember() {
        Member member = new Member();
        member.setName("곰돌이");
        member.setEmail("jks2024@gmail.co,");
        member.setPwd("sphb8250");
        member.setImage("http://google.co.kr");
        return memberRepository.save(member);
    }

    // 상품 생성 메서드
    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    // 장바구니 생성 및 장바구니 담기
    @Test
    @DisplayName("장바구니 담기 테스트")
    public void cartItemTest() {
        // 회원과 상품 객체 생성 메서드 호출 및 참조 변수 생성
        Member member = createMember();
        Item item = createItem();

        // 회원에 대한 장바구니 생성
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setCartName("테스트 장바구니");
        cartRepository.save(cart);

        // 장바구니에 담을 상품 정보 생성
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(5);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // 저장된 장바구니 상품 조회
        CartItem findCartItem = cartItemRepository.findById(savedCartItem.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 검증
        log.info("저장된 상풍명 : {}", savedCartItem);
        log.info("조회된 상품명 : {}", findCartItem);
    }
}