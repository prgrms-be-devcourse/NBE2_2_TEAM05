package edu.example.dev_2_cc.dto.cart;

import edu.example.dev_2_cc.dto.cartItem.CartItemRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartRequestDTO {
    private List<CartItemRequestDTO> cartItems;


}
