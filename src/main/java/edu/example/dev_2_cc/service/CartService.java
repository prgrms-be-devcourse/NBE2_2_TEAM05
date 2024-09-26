package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.cart.CartRequestDTO;
import edu.example.dev_2_cc.dto.cart.CartResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemRequestDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemResponseDTO;
import edu.example.dev_2_cc.entity.Cart;
import edu.example.dev_2_cc.entity.CartItem;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.repository.CartItemRepository;
import edu.example.dev_2_cc.repository.CartRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    //Cart 등록
    public CartResponseDTO create(CartRequestDTO cartRequestDTO) {
        // 사용자의 Cart 가 있는지 확인
        Member member = memberRepository.findById(cartRequestDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Cart savedCart = cartRepository.findByMemberId(cartRequestDTO.getMemberId())
                .orElseGet( () -> {
                    Cart cart = Cart.builder().member(member).build();
                    return cartRepository.save(cart);
                });

        //CartItem 처리
        List<CartItemRequestDTO> cartItems = cartRequestDTO.getCartItems();
        List<CartItem> savedCartItems = new ArrayList<>();

        for(CartItemRequestDTO cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CartItem savedCartItem = cartItemRepository.findByCartAndProduct(savedCart, product)
                    .orElseGet(() -> {
                        CartItem newCartItem = CartItem.builder()
                                .cart(savedCart)
                                .product(product)
                                .quantity(0)
                                .build();
                        return cartItemRepository.save(newCartItem);
                    });

            savedCartItem.changeQuantity(savedCartItem.getQuantity() + cartItem.getQuantity());

            savedCartItems.add(savedCartItem);
        }

        return new CartResponseDTO(savedCart, savedCartItems);
    }

    //Cart 조회
    public CartResponseDTO read(String memberId) {
        Cart foundCart = cartRepository.findByMemberId(memberId)
                .orElseThrow();

        return new CartResponseDTO(foundCart);
    }

    //전체 Cart 조회
    public List<CartResponseDTO> readAll() {
        return cartRepository.findAll().stream()
                .map(CartResponseDTO::new)
                .collect(Collectors.toList());
    }

    //Cart 수정 - CartItem 의 수량 변경
    public CartItemResponseDTO update(Long cartItemId, CartItemRequestDTO cartItemRequestDTO){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if(cartItemRequestDTO.getQuantity() == 0) {
            cartItemRepository.deleteById(cartItemId);
            return null;
        }

        cartItem.changeQuantity(cartItemRequestDTO.getQuantity());

        return new CartItemResponseDTO(cartItem);
    }

    //Cart 삭제 - Order 생성시 필요
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    //CartItem 삭제
    public void delete(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // 프론트에서의 요구에 따라



}

