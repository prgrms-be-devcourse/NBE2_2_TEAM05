package edu.example.dev_2_cc.dto.api_controller;

import edu.example.dev_2_cc.dto.cart.CartRequestDTO;
import edu.example.dev_2_cc.dto.cart.CartResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemRequestDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemResponseDTO;
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

    @PostMapping
    public ResponseEntity<CartResponseDTO> create(
            @Validated @RequestBody CartRequestDTO cartRequestDTO) {
        return ResponseEntity.ok(cartService.create(cartRequestDTO));
    }

    @GetMapping("/{memberId}") //이후 검색 조건에 따라 수정 필요
    public ResponseEntity<CartResponseDTO> read(
            @PathVariable("memberId") String memberId) {
        return ResponseEntity.ok(cartService.read(memberId));
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> readAll() {
        return ResponseEntity.ok(cartService.readAll());
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> update(
            @PathVariable("cartItemId") Long cartItemId,
            @Validated @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return ResponseEntity.ok(cartService.update(cartItemId, cartItemRequestDTO));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable("cartItemId") Long cartItemId){
        return ResponseEntity.ok(Map.of("result", "success"));
    }

}
