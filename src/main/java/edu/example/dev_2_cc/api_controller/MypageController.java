package edu.example.dev_2_cc.api_controller;


import edu.example.dev_2_cc.dto.cart.CartResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemResponseDTO;
import edu.example.dev_2_cc.dto.cartItem.CartItemUpdateDTO;
import edu.example.dev_2_cc.dto.member.MemberResponseDTO;
import edu.example.dev_2_cc.dto.member.MemberUpdateDTO;
import edu.example.dev_2_cc.dto.order.OrderRequestDTO;
import edu.example.dev_2_cc.dto.order.OrderResponseDTO;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.MemberTaskException;
import edu.example.dev_2_cc.service.CartService;
import edu.example.dev_2_cc.service.MemberService;
import edu.example.dev_2_cc.service.OrderService;
import org.springframework.http.HttpStatus;
import edu.example.dev_2_cc.dto.review.ReviewRequestDTO;
import edu.example.dev_2_cc.dto.review.ReviewResponseDTO;
import edu.example.dev_2_cc.dto.review.ReviewUpdateDTO;
import edu.example.dev_2_cc.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cc/mypage")
@Log4j2
@RequiredArgsConstructor
public class MypageController {
    private final MemberService memberService;
    private final ReviewService reviewService;
    private final OrderService orderService;
    private final CartService cartService;

    //-----------------------------------------회원 정보 수정-------------------------------------------------

    // 마이페이지 내에서 회원의 직접 정보 수정 (권한 수정 미포함)
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable String memberId,
            @Validated @RequestBody MemberUpdateDTO memberUpdateDTO
    ) {
//        //updateDTO에 memberId 설정
//        memberUpdateDTO.setMemberId(memberId);

        //회원 정보 수정
        MemberResponseDTO response = memberService.modify(memberId, memberUpdateDTO);

        return ResponseEntity.ok(response);
    }

    // 마이페이지 내에서 회원 탈퇴
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable String memberId) {
        try {
            memberService.delete(memberId); // 서비스에서 회원 삭제 로직 호출
            return ResponseEntity.noContent().build(); // 성공적으로 삭제된 경우 204 No Content 응답
        } catch (MemberTaskException e) {
            if (e.getMessage().equals(MemberException.NOT_FOUND.get().getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Error deleting member"));
        }
    }
    //-----------------------------------------------장바구니----------------------------------------------------

    //Cart 조회
    @GetMapping("/cart/{memberId}") //이후 검색 조건에 따라 수정 필요
    public ResponseEntity<CartResponseDTO> read(
            @PathVariable("memberId") String memberId) {
        return ResponseEntity.ok(cartService.read(memberId));
    }

    //CartItem 수량 수정
    @PutMapping("/cart/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> update(
            @PathVariable("cartItemId") Long cartItemId,
            @Validated @RequestBody CartItemUpdateDTO cartItemUpdateDTO) {
        log.info(cartItemUpdateDTO);
        return ResponseEntity.ok(cartService.update(cartItemUpdateDTO));
    }

    //CartItem 삭제(단일 상품 지우기)
    @DeleteMapping("/cartitem/{cartItemId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable("cartItemId") Long cartItemId){

        cartService.delete(cartItemId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //Cart 삭제(장바구니 비우기)
    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Map<String, String>> deleteCart(
            @PathVariable("cartId") Long cartId){
        cartService.deleteCart(cartId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }


    //------------------------------------------------주문------------------------------------------------------

    @PostMapping("/order")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO orderRequestDTO) {
        Orders order = orderService.createOrder(orderRequestDTO);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);

        return ResponseEntity.ok(orderResponseDTO);
    }

    //단일 주문 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrder(
            @PathVariable Long orderId) {
        Orders order = orderService.findOrderById(orderId);
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

    //----------------------------------------------------리뷰----------------------------------------------------


    @PostMapping("/review")
    public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.create(reviewRequestDTO));
    }

    @PutMapping("/review/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long reviewId, @Validated @RequestBody ReviewUpdateDTO reviewUpdateDTO) {
        return ResponseEntity.ok(reviewService.update(reviewUpdateDTO));
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok(Map.of("message", "Review deleted"));
    }


}

