package edu.example.dev_2_cc.dto.review;

import edu.example.dev_2_cc.entity.Review;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewUpdateDTO {

    @NotNull
    private Long reviewId;

    private String content;

    @Min(0)
    private int star;

    public Review toEntity(){
        Review review = Review.builder().reviewId(reviewId).content(content).star(star).build();

        return review;
    }
}
