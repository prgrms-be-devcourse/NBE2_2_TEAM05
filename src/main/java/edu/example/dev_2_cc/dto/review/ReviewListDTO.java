package edu.example.dev_2_cc.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListDTO {
    private Long reviewId;
    private String content;
    private int star;
    private String memberId;
    private Long productId;
}
