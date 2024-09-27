package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Cart;
import edu.example.dev_2_cc.entity.CartItem;
import edu.example.dev_2_cc.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
