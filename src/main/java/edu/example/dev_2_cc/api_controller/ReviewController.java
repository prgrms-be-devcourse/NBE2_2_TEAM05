package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import edu.example.dev_2_cc.dto.review.ReviewResponseDTO;
import edu.example.dev_2_cc.dto.review.ReviewUpdateDTO;
import edu.example.dev_2_cc.entity.Review;
import edu.example.dev_2_cc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId, @Validated @RequestBody ReviewUpdateDTO reviewUpdateDTO) {
        return ResponseEntity.ok(reviewService.update(reviewUpdateDTO));
    }
}
