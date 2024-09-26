package edu.example.dev_2_cc.dto.cartItem;

import edu.example.dev_2_cc.entity.CartItem;
import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long cartItemId;
    private Long productId;
    private int quantity;

    public CartItemResponseDTO(CartItem cartItem) {
        this.cartItemId = cartItem.getCartItemId();
        this.productId = cartItem.getProduct().getProductId();
        this.quantity = cartItem.getQuantity();
    }
}
