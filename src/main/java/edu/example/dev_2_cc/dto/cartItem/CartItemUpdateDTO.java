package edu.example.dev_2_cc.dto.cartItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateDTO {
    private Long cartItemId;
    private int quantity;
}
