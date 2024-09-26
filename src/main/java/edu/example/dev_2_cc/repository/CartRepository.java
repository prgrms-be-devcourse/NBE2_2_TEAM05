package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    //cartId로 해당 카트의 총 가격을 구하는 쿼리 입니다
    @Query(" SELECT SUM(p.price * ci.quantity) FROM CartItem ci JOIN ci.product p WHERE ci.cart.cartId = :cartId ")
    Long totalPrice(@Param("cartId") Long cartId);
}
