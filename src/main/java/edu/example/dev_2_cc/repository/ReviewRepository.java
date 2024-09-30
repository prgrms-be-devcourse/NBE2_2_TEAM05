package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.productId = :productId AND r.member.memberId = :memberId")
    Optional<Review> findByProduct_IdAndMember_Id(@Param("productId") Long productId, @Param("memberId") String memberId);

}


