package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}