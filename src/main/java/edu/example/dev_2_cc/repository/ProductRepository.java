package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
