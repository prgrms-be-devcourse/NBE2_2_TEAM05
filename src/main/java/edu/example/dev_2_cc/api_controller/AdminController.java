package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.cart.CartResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.dto.order.OrderResponseDTO;
import edu.example.dev_2_cc.dto.order.OrderUpdateDTO;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.service.CartService;
import edu.example.dev_2_cc.service.MemberService;
import edu.example.dev_2_cc.service.OrderService;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import edu.example.dev_2_cc.dto.product.ProductRequestDTO;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.dto.product.ProductUpdateDTO;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cc/admin")
public class AdminController {
    private final ProductService productService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final CartService cartService;

    //-----------------------------------------회원 관리-----------------------------------------------------

    // 관리자의 회원 전체 조회
    @GetMapping("/memberlist")
    public ResponseEntity<List<MemberResponseDTO>> getAllMembers() {
        List<MemberResponseDTO> memberList = memberService.list();
        return ResponseEntity.ok(memberList);
    }

    // 관리자의 단일 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMember(
            @PathVariable String memberId
    ) {
        return ResponseEntity.ok(memberService.readMember(memberId));
    }

    // 관리자의 회원 정보 수정(권한 수정 포함)
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable String memberId,
            @Validated @RequestBody MemberUpdateDTO memberUpdateDTO
    ) {
//        //updateDTO에 memberId 설정
//        memberUpdateDTO.setMemberId(memberId);

        //회원 정보 수정
        MemberResponseDTO response = memberService.modifyAdmin(memberId, memberUpdateDTO);

        return ResponseEntity.ok(response);
    }

    //------------------------------------------------상품 관리---------------------------------------------------

    @PostMapping("/product")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.create(productRequestDTO));
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateDTO productUpdateDTO) {
        if(!productId.equals(productUpdateDTO.getProductId())) {
            throw ProductException.NOT_FOUND.get();
        }
        return ResponseEntity.ok(productService.update(productUpdateDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok(Map.of("message", "Product deleted"));
    }
    //-----------------------------------------------장바구니----------------------------------------------------

    //Cart 전체 조회
    @GetMapping("/cart")
    public ResponseEntity<List<CartResponseDTO>> readAll() {
        return ResponseEntity.ok(cartService.readAll());
    }

    //------------------------------------------------주문 관리--------------------------------------------------

    //주문 전체 조회
    @GetMapping("/order")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orderList = orderService.list();
        return ResponseEntity.ok(orderList);

    }

    //단일 주문 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @PathVariable Long orderId) {
        Orders order = orderService.findOrderById(orderId);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);

        return ResponseEntity.ok(orderResponseDTO);
    }

    //배송 상태 수정
    @PutMapping("/order/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateDTO orderUpdateDTO) {

        //updateDTO에 orderId 설정
        orderUpdateDTO.setOrderId(orderId);
        //배송 상태 수정
        Orders order = orderService.modifyStatus(orderUpdateDTO);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);

        return ResponseEntity.ok(orderResponseDTO);

    }

    //주문 삭제
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Map<String, String>> deleteOrder(
            @PathVariable Long orderId) {
        orderService.delete(orderId); // 서비스에서 주문 삭제 로직 호출
        // 메시지를 담은 응답

        Map<String, String> response = new HashMap<>();
        response.put("message", "주문이 성공적으로 삭제되었습니다.");

        return ResponseEntity.ok(response); // 200 OK와 함께 응답 본문 반환
    }

    
}
