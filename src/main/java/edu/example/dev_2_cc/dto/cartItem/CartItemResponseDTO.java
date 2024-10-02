package edu.example.dev_2_cc.dto.cartItem;

import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.CartItem;
import edu.example.dev_2_cc.entity.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class CartItemResponseDTO {
    private Long cartItemId;
    private ProductResponseDTO product;
    private int quantity;

    public CartItemResponseDTO(CartItem cartItem) {
        this.cartItemId = cartItem.getCartItemId();
        this.product = new ProductResponseDTO(cartItem.getProduct());
        this.quantity = cartItem.getQuantity();
    }
}
