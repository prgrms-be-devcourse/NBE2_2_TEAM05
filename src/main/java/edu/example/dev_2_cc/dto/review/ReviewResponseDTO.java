package edu.example.dev_2_cc.dto.review;

import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
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
    private Member member;

    @NotEmpty
    private Product product;

    private String content;
    private int star;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
