package edu.example.dev_2_cc.api_controller;

import edu.example.dev_2_cc.dto.order.OrderRequestDTO;
import edu.example.dev_2_cc.dto.order.OrderResponseDTO;
import edu.example.dev_2_cc.dto.order.OrderUpdateDTO;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cc/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문 생성
    //상품에서 바로 주문 생성 기능으로 주석처리 하지 않음
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO orderRequestDTO) {
        Orders order = orderService.createOrder(orderRequestDTO);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);

        return ResponseEntity.ok(orderResponseDTO);
    }

//    //주문 전체 조회
//    @GetMapping
//    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
//        List<OrderResponseDTO> orderList = orderService.list();
//        return ResponseEntity.ok(orderList);
//
//    }
//
//    //단일 주문 조회
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderResponseDTO> getOrder(
//            @PathVariable Long orderId) {
//        Orders order = orderService.findOrderById(orderId);
//        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);
//
//        return ResponseEntity.ok(orderResponseDTO);
//    }

//    //배송 상태 수정
//    @PutMapping("/{orderId}")
//    public ResponseEntity<OrderResponseDTO> updateOrder(
//            @PathVariable Long orderId,
//            @RequestBody OrderUpdateDTO orderUpdateDTO) {
//
//        //updateDTO에 orderId 설정
//        orderUpdateDTO.setOrderId(orderId);
//        //배송 상태 수정
//        Orders order = orderService.modifyStatus(orderUpdateDTO);
//        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);
//
//        return ResponseEntity.ok(orderResponseDTO);
//
//    }
//
//    //주문 삭제
//    @DeleteMapping("/{orderId}")
//    public ResponseEntity<Map<String, String>> deleteOrder(
//            @PathVariable Long orderId) {
//        orderService.delete(orderId); // 서비스에서 주문 삭제 로직 호출
//        // 메시지를 담은 응답
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "주문이 성공적으로 삭제되었습니다.");
//
//        return ResponseEntity.ok(response); // 200 OK와 함께 응답 본문 반환
//    }
//    //주문 전체 조회 memberId
//    @GetMapping("/list/{memberId}")
//    public ResponseEntity<List<OrderResponseDTO>> getOrdersMember(
//            @PathVariable String memberId) {
//        List<OrderResponseDTO> orderList = orderService.findOrderByMemberId(memberId);
//        return ResponseEntity.ok(orderList);
//    }
}
