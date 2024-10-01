package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.review.*;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.Review;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.exception.ReviewException;
import edu.example.dev_2_cc.exception.ReviewTaskException;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import edu.example.dev_2_cc.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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


    public ReviewResponseDTO update(ReviewUpdateDTO reviewUpdateDTO) {
        Optional<Review> foundReview = reviewRepository.findById(reviewUpdateDTO.getReviewId());
        Review review = foundReview.orElseThrow(ReviewException.NOT_FOUND::get);

        try {
            review.changeContent(reviewUpdateDTO.getContent());
            review.changeStar(reviewUpdateDTO.getStar());

            return new ReviewResponseDTO(reviewRepository.save(review));

        }catch (Exception e){
            log.error(e.getMessage());
            throw ReviewException.NOT_UPDATED.get();
        }
    }

    public ReviewResponseDTO read(Long reviewId) {
        try{
            Optional<Review> foundReview = reviewRepository.findById(reviewId);
            Review review = foundReview.get();
            return new ReviewResponseDTO(review);
        }catch (Exception e){
            log.error(e.getMessage());
            throw ReviewException.NOT_FOUND.get();
        }
    }

    public void delete(Long reviewId) {
        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        Review review = foundReview.orElseThrow(ReviewException.NOT_FOUND::get);

        try{
            reviewRepository.delete(review);
        }catch (Exception e){
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_DELETED.get();
        }

    }

    public Page<ReviewListDTO> getListByMemberId(String memberId, PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("createdAt").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            Page<Review> reviews = reviewRepository.findByMember_Id(memberId, pageable);

            return reviews.map(review -> new ReviewListDTO(
                    review.getReviewId(),
                    review.getContent(),
                    review.getStar(),
                    review.getMember().getMemberId(),
                    review.getProduct().getProductId()
            ));
        }catch (Exception e){
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_FETCHED.get();
        }
    }

    public Page<ReviewListDTO> getListByProductId(Long productId, PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("reviewId").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);

            // Review를 Page로 조회
            Page<Review> reviews = reviewRepository.findReviewsByProductId(productId, pageable);

            // Review를 ReviewListDTO로 변환
            return reviews.map(review -> new ReviewListDTO(
                    review.getReviewId(),
                    review.getContent(),
                    review.getStar(),
                    review.getMember().getMemberId(),
                    review.getProduct().getProductId()
            ));

        } catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ReviewException.NOT_FOUND.get();
        }
    }
}
