package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByMember(Member member);
}
