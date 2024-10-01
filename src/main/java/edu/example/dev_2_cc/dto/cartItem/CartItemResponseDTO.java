package edu.example.dev_2_cc.dto.cartItem;

import edu.example.dev_2_cc.entity.CartItem;
import edu.example.dev_2_cc.entity.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class CartItemResponseDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String filename;
    private int quantity;
    private long productPrice;

    public CartItemResponseDTO(CartItem cartItem) {
        this.cartItemId = cartItem.getCartItemId();
        this.productId = cartItem.getProduct().getProductId();
        this.productName = cartItem.getProduct().getPName();
        this.quantity = cartItem.getQuantity();
        this.productPrice = cartItem.getProduct().getPrice();
    }
}
