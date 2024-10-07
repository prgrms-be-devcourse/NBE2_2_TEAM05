package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import edu.example.dev_2_cc.dto.review.ReviewResponseDTO;
import edu.example.dev_2_cc.dto.review.ReviewUpdateDTO;
import edu.example.dev_2_cc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cc/mypage")
public class MypageController {
    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.create(reviewRequestDTO));
    }

    @PutMapping("/review/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId, @Validated @RequestBody ReviewUpdateDTO reviewUpdateDTO) {
        return ResponseEntity.ok(reviewService.update(reviewUpdateDTO));
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok(Map.of("message", "Review deleted"));
    }

}
