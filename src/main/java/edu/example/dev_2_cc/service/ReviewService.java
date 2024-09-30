package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import edu.example.dev_2_cc.dto.review.ReviewResponseDTO;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.Review;
import edu.example.dev_2_cc.exception.ReviewException;
import edu.example.dev_2_cc.exception.ReviewTaskException;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import edu.example.dev_2_cc.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ReviewResponseDTO create(ReviewRequestDTO reviewRequestDTO) {
        try {
            Long productId = reviewRequestDTO.getProductId();
            String memberId = reviewRequestDTO.getMemberId();

            // 이미 존재하는 리뷰인지 확인
            if (reviewRepository.findByProduct_IdAndMember_Id(productId, memberId).isPresent()) {
                throw ReviewException.ALREADY_EXISTS.get(); // ALREADY_EXISTS 예외 던짐
            }

            Product product = productRepository.findById(productId).orElseThrow();
            Member member = memberRepository.findById(memberId).orElseThrow();

            Review review = reviewRequestDTO.toEntity(product, member);
            Review savedReview = reviewRepository.save(review);

            return new ReviewResponseDTO(savedReview);
        } catch (ReviewTaskException e) {
            // 이미 존재하는 리뷰 예외는 그대로 던지기
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            // 다른 예외 발생 시 NOT_CREATED 예외 던지기
            throw ReviewException.NOT_CREATED.get();
        }
    }


}
