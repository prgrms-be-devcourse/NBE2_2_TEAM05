package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.review.*;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.Review;
import edu.example.dev_2_cc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/review")
@Log4j2
public class ReviewController {
    private final ReviewService reviewService;

//    @PostMapping
//    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
//        return ResponseEntity.ok(reviewService.create(reviewRequestDTO));
//    }
//
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId, @Validated @RequestBody ReviewUpdateDTO reviewUpdateDTO) {
//        return ResponseEntity.ok(reviewService.update(reviewUpdateDTO));
//    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReview(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewService.read(reviewId));
    }


//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("reviewId") Long reviewId) {
//        reviewService.delete(reviewId);
//        return ResponseEntity.ok(Map.of("message", "Review deleted"));
//    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<ReviewListDTO>> getReviewListByMemberId(@PathVariable("memberId") String memberId, PageRequestDTO pageRequestDTO) {
        Page<ReviewListDTO> reviewList = reviewService.getListByMemberId(memberId, pageRequestDTO);

        return ResponseEntity.ok(reviewList);
    }

    // productId를 기반으로 리뷰 리스트를 반환하는 메서드
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewListDTO>> getReviewListByProductId(
            @PathVariable("productId") Long productId,
            @Validated PageRequestDTO pageRequestDTO) {

        log.info("getList by productId ----- " + pageRequestDTO);

        // ReviewService에서 productId를 기준으로 리뷰 리스트를 가져옴
        Page<ReviewListDTO> reviewList = reviewService.getListByProductId(productId, pageRequestDTO);

        return ResponseEntity.ok(reviewList);
    }
}
