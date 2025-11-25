package com.human.jpa_sample.repository;

import com.human.jpa_sample.constant.ItemSellStatus;
import com.human.jpa_sample.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Spring Container에 Bean 객체 등록 (싱글톤)

public interface ItemRepository extends JpaRepository<Item, Long> {
    // JpaRepository를 상속 받았기 때문에 기본적인 CRUD는 이미 적용되어 있음

    // 쿼리메서드 : 간단한 네이밍 규칙으로 설계명세를 작성하면 hibernate가 실제 SQL을 만들어 줌
    // find + (엔티티 이름) + By + 변수이름, 반드시 카멜표기법 준수, 만약 오타내면 바로 죽어 버림
    List<Item> findByItemNm(String itemNm);  // SELECT item_nm FROM member WHERE item_nm = ?

    // 1. OR 조건: 상품 이름이 A이거나 상품 상세 설명이 B인 경우
    // SQL: SELECT * FROM item WHERE item_nm = ? OR item_detail = ?
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // 2. LessThan 조건: 가격이 특정 가격보다 작은 상품 조회
    // SQL: SELECT * FROM item WHERE price < ?
    List<Item> findByPriceLessThan(Integer price);

    // 3. OrderBy 정렬: 가격이 특정 가격보다 작은 상품을 조회하되, 가격이 높은 순(내림차순)으로 정렬
    // SQL: SELECT * FROM item WHERE price < ? ORDER BY price DESC
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // 4. AND 조건 + GreaterThan: 가격이 특정 가격보다 크고, 판매 상태가 특정 상태인 경우
    // SQL: SELECT * FROM item WHERE price > ? AND item_sell_status = ?
    List<Item> findByPriceGreaterThanAndItemSellStatus(Integer price, ItemSellStatus itemSellStatus);

    // 5. Like 검색: 상품 상세 설명에 특정 단어가 포함된 경우 (부분 일치)
    // SQL: SELECT * FROM item WHERE item_detail LIKE ?
    List<Item> findByItemDetailLike(String itemDetail);
}
