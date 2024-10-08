package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.cart.CartRequestDTO;
import edu.example.dev_2_cc.dto.cart.CartResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemRequestDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemUpdateDTO;
import edu.example.dev_2_cc.entity.Cart;
import edu.example.dev_2_cc.entity.CartItem;
import edu.example.dev_2_cc.entity.Member;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.exception.CartException;
import edu.example.dev_2_cc.repository.CartItemRepository;
import edu.example.dev_2_cc.repository.CartRepository;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import edu.example.dev_2_cc.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final SecurityUtil securityUtil;

    //Cart 등록
    public CartResponseDTO create(CartRequestDTO cartRequestDTO) {
        // 사용자의 Cart 가 있는지 확인
//        Member member = memberRepository.findById(cartRequestDTO.getMemberId())
//                .orElseThrow(CartException.NOT_FOUND::get);
        Member member = memberRepository.findById(securityUtil.getCurrentUser().getMemberId())
                .orElseThrow(CartException.NOT_FOUND::get);

        try {
            Cart savedCart = cartRepository.findByMemberId(member.getMemberId())
                    .orElseGet(() -> {
                        Cart cart = Cart.builder().member(member).build();
                        return cartRepository.save(cart);
                    });

            //CartItem 처리
            List<CartItemRequestDTO> cartItems = cartRequestDTO.getCartItems();
            List<CartItem> savedCartItems = new ArrayList<>();

            for (CartItemRequestDTO cartItem : cartItems) {
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
            Long totalPrice = cartRepository.totalPrice(savedCart.getCartId());

            return new CartResponseDTO(savedCart, savedCartItems, totalPrice);
        } catch (Exception e) {
            log.error("예외 발생 코드 : " + e.getMessage());
            throw CartException.NOT_REGISTERED.get();
        }
    }

    //Cart 조회
    public CartResponseDTO read(String memberId) {
        Cart foundCart = cartRepository.findByMemberId(memberId)
                .orElseThrow(CartException.NOT_FOUND::get);
        Long totalPrice = cartRepository.totalPrice(foundCart.getCartId());

        return new CartResponseDTO(foundCart, totalPrice);
    }

    //전체 Cart 조회
    public List<CartResponseDTO> readAll() {
        List<Cart> carts = cartRepository.findAll();

        List<CartResponseDTO> cartResponseList = new ArrayList<>();
        for (Cart cart : carts) {
            Long totalPrice = cartRepository.totalPrice(cart.getCartId());
            cartResponseList.add(new CartResponseDTO(cart, totalPrice));
        }

        return cartResponseList;
//        return cartRepository.findAll().stream()
//                .map(CartResponseDTO::new)
//                .collect(Collectors.toList());
    }

    //Cart 수정 - CartItem 의 수량 변경
    public CartItemResponseDTO update(CartItemUpdateDTO cartItemUpdateDTO){
        Long cartItemId = cartItemUpdateDTO.getCartItemId();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        try {
            if(cartItemUpdateDTO.getQuantity() == 0) {
                cartItemRepository.deleteById(cartItemId);
                return null;
            }

            cartItem.changeQuantity(cartItemUpdateDTO.getQuantity());

            return new CartItemResponseDTO(cartItem);
        } catch (Exception e) {
            log.error("예외 발생 코드 : " + e.getMessage());
            throw CartException.NOT_MODIFIED.get();
        }
    }

    //Cart 삭제 - Order 생성시 필요
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartException.NOT_FOUND::get);
        try {
            cartRepository.delete(cart);
        } catch (Exception e) {
            log.error("예외 발생 코드 : " + e.getMessage());
            throw CartException.NOT_REMOVED.get();
        }
    }

    //CartItem 삭제
    public void delete(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(CartException.NOT_FOUND::get);
        try {
            cartItemRepository.delete(cartItem);
        } catch (Exception e) {
            log.error("예외 발생 코드 : " + e.getMessage());
            throw CartException.NOT_REMOVED.get();
        }
    }

    // 프론트에서의 요구에 따라



}

