package edu.example.dev_2_cc.dto.review;

import edu.example.dev_2_cc.entity.Review;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewRequestDTO {
    private String content;
    private int star;

    public Review toEntity(){
        Review review = Review.builder().content(content).star(star).build();

        return review;
    }
}
