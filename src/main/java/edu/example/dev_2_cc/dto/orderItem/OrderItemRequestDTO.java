package edu.example.dev_2_cc.dto.orderItem;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderItemRequestDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;        // 상품 ID
    @NotNull(message = "Quantity is required")
    private Integer quantity;      // 수량
}
