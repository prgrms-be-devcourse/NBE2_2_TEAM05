package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
