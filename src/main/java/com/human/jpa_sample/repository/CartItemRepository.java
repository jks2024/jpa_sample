package com.human.jpa_sample.repository;

import com.human.jpa_sample.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
