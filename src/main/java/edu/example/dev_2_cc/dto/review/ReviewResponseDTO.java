package edu.example.dev_2_cc.dto.review;

import edu.example.dev_2_cc.entity.Review;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReviewResponseDTO {

    @NotEmpty
    private Long reviewId;

    @NotEmpty
    private String memberId;

    @NotEmpty
    private Long productId;

    private String content;
    private int star;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponseDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.memberId = review.getMember().getMemberId();
        this.productId = review.getProduct().getProductId();
        this.content = review.getContent();
        this.star = review.getStar();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}