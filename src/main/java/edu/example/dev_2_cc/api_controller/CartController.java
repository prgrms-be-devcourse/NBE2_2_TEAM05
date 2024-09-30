package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.cart.CartRequestDTO;
import edu.example.dev_2_cc.dto.cart.CartResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemUpdateDTO;
import edu.example.dev_2_cc.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cc/cart")
public class CartController {
    private final CartService cartService;

    //Cart 등록
    @PostMapping
    public ResponseEntity<CartResponseDTO> create(
            @Validated @RequestBody CartRequestDTO cartRequestDTO) {
        return ResponseEntity.ok(cartService.create(cartRequestDTO));
    }

    //Cart 조회
    @GetMapping("/{memberId}") //이후 검색 조건에 따라 수정 필요
    public ResponseEntity<CartResponseDTO> read(
            @PathVariable("memberId") String memberId) {
        return ResponseEntity.ok(cartService.read(memberId));
    }

    //Cart 전체 조회(관리자?)
    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> readAll() {
        return ResponseEntity.ok(cartService.readAll());
    }

    //CartItem 수량 수정
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> update(
            @PathVariable("cartItemId") Long cartItemId,
            @Validated @RequestBody CartItemUpdateDTO cartItemUpdateDTO) {
        return ResponseEntity.ok(cartService.update(cartItemUpdateDTO));
    }

    //CartItem 삭제(단일 상품 지우기)
    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable("cartItemId") Long cartItemId){
        cartService.delete(cartItemId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //Cart 삭제(장바구니 비우기)
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, String>> deleteCart(
            @PathVariable("cartId") Long cartId){
        cartService.deleteCart(cartId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }


}
