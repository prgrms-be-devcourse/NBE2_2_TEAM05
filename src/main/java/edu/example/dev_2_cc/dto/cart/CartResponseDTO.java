package edu.example.dev_2_cc.dto.cart;

import edu.example.dev_2_cc.dto.cartItem.CartItemResponseDTO;
import edu.example.dev_2_cc.entity.Cart;
import edu.example.dev_2_cc.entity.CartItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartResponseDTO {
    private Long cartId;

    //이후 memberDTO 로 수정 필요
    private String memberId;

    private List<CartItemResponseDTO> cartItems;



    public CartResponseDTO(Cart cart) {
        this.cartId = cart.getCartId();
        this.memberId = cart.getMember().getMemberId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CartResponseDTO(Cart cart, List<CartItem> savedCartItems) {
        this.cartId = cart.getCartId();
        this.memberId = cart.getMember().getMemberId();
        this.cartItems = savedCartItems.stream()
                .map(cartItem -> new CartItemResponseDTO(cartItem))
                .collect(Collectors.toList());
    }


}
