package edu.example.dev_2_cc.dto.review;

import edu.example.dev_2_cc.entity.Review;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewUpdateDTO {

    @NotEmpty
    private Long reviewId;

    private String content;
    private int star;

    public Review toEntity(){
        Review review = Review.builder().reviewId(reviewId).content(content).star(star).build();

        return review;
    }
}
