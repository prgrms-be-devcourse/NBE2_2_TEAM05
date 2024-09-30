package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import edu.example.dev_2_cc.dto.review.ReviewResponseDTO;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.Review;
import edu.example.dev_2_cc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.create(reviewRequestDTO));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReview(@PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(reviewService.read(reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok(Map.of("message", "Review deleted"));
    }
}
