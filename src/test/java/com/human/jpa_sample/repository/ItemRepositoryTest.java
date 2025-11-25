package com.human.jpa_sample.repository;

import com.human.jpa_sample.constant.ItemSellStatus;
import com.human.jpa_sample.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest  // Test logic 작성
@TestPropertySource(locations="classpath:application-test.properties")
@Slf4j   // Log 메세지 출력
class ItemRepositoryTest {
    @Autowired   // 의존성 주입을 받기 위해 사용하는 어노테이션
    ItemRepository itemRepository; // 필드를 통한 의존성 주입

    @Test // 이 메서드가 테스트 대상임을 명시
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();   // 빈생성자를 통해 Item 클래스에 대한 객체 생성
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000);
            item.setItemDetail("테스트 상품의 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item); // insert와 동일
            log.debug("savedItem : {}", savedItem);
        }

    }

    @Test
    @DisplayName("상품 조회 테스트")
    public void findByItemNmTest() {
        this.createItemTest();  // 테스트를 위해 item 객체 생성
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품8");
        for (Item item : itemList) log.info("상품 조회 테스트 : {}", item);
    }

    @Test
    @DisplayName("1. OR 조건 테스트 ")
    public void findByItemNmOrItemDetailTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository
                .findByItemNmOrItemDetail("테스트 상품1", "테스트 상품의 상세 설명5");

    }





}