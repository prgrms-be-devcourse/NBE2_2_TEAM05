package edu.example.dev_2_cc.dto.review;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.entity.Review;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestDTO {
    private String content;
    private int star;

    @NotEmpty
    private String memberId;

    @NotEmpty
    private Long productId;

    public Review toEntity (Product product, Member member){
        Review review = Review.builder().content(content).star(star).member(member).product(product).build();
        return review;
    }
}