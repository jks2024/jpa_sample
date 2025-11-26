package com.human.jpa_sample.repository;

import com.human.jpa_sample.constant.ItemSellStatus;
import com.human.jpa_sample.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // @Query : 조건이 많거나, 복잡한 쿼리를 작성해야 하는 경우 쿼리메서드로 작성하기 힘든 경우가 있음
    // - JPQL(Java Persistence Query Language) Query : 객체지향 언어를 동채 복잡한 쿼리를 저리
    // - Native Query : 각 DB의 SQL 문 작성
    @Query(value = "SELECT i from Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "SELECT * FROM item WHERE item_detail LIKE %:itemDetail% ORDER BY price DESC", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

    // JPQL QUERY
    /* * 1. 가격 범위(min ~ max)와 판매 상태(status)를 동시에 조회
     * - JPQL은 DB 테이블(item)이 아닌 엔티티 객체(Item i)를 대상으로 작성합니다.
     * - 변수 바인딩은 :변수명 형식을 사용합니다.
     */
    @Query(value = "SELECT i FROM Item i WHERE i.price BETWEEN :minPrice AND :maxPrice AND i.itemSellStatus = :status")
    List<Item> findByPriceRangeAndStatus(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("status") ItemSellStatus status);

    /*
     * 2. 상품 이름(itemNm)이나 상세 설명(itemDetail)에 특정 키워드가 포함된 상품 조회
     * - OR 조건과 LIKE 문을 결합한 형태입니다.
     * - %:keyword% 처럼 파라미터 앞뒤로 와일드카드를 붙여주는 것이 핵심입니다. (서비스 계층에서 붙여서 넘기거나 쿼리에서 결합)
     * - 여기서는 파라미터 자체에 %를 포함해서 넘겨준다고 가정하거나, CONCAT을 사용할 수 있습니다.
     */
    @Query("SELECT i FROM Item i WHERE i.itemNm LIKE %:keyword% OR i.itemDetail LIKE %:keyword%")
    List<Item> findByKeyword(@Param("keyword") String kwyword);

    /*
     * 3. 특정 가격 이상인 상품을 조회하되, 최근 등록된 순(id 내림차순)으로 정렬
     * - ORDER BY절에 엔티티의 필드명(i.id)을 사용합니다.
     */
    @Query("SELECT i FROM Item i WHERE i.price >= :price ORDER BY i.id DESC")
    List<Item> findHighPriceItems(@Param("price") Integer price);

    // Native QUERY
    /*
     * 1. 가격 범위 조회 (SQL)
     * - nativeQuery = true 옵션을 반드시 줘야 합니다.
     * - 엔티티 필드명(itemDetail)이 아닌 DB 컬럼명(item_detail, price 등)을 사용해야 합니다.
     * - * 을 사용해 모든 컬럼을 가져오면 Item 엔티티에 매핑됩니다.
     */
    @Query(value = "SELECT * FROM item WHERE price BETWEEN :minPrice AND :maxPrice", nativeQuery = true)
    List<Item> findByPriceRangeNative(@Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);

    /*
     * 2. 상품명으로 정확히 일치하는 상품 찾기 (SQL)
     * - 가장 기본적인 WHERE 절 사용 예시입니다.
     */
    @Query(value = "SELECT * FROM item WHERE item_nm = :itemNm", nativeQuery = true)
    List<Item> findByItemNmNative(@Param("itemNm") String itemNm);


    /*
     * 3. 가장 비싼 상품 5개만 조회 (Limit 사용)
     * - JPQL은 limit 키워드를 직접 지원하지 않는 경우가 많아(Pageable 사용 권장),
     * 간단한 Top N 조회는 Native Query가 편할 때가 있습니다. (MySQL 기준 LIMIT)
     */
    @Query(value = "SELECT * FROM item ORDER BY price DESC LIMIT 5", nativeQuery = true)
    List<Item> findTop5ExpensiveItemsNative();

}
