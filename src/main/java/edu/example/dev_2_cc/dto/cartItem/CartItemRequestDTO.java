package edu.example.dev_2_cc.dto.cartItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequestDTO {
    private Long productId;
    private int quantity;
}
