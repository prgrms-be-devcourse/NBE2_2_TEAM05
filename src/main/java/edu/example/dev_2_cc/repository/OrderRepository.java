package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
