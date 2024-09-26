package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
