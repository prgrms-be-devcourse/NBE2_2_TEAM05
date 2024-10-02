package edu.example.dev_2_cc.dto.order;

import edu.example.dev_2_cc.dto.orderItem.OrderItemResponseDTO;
import edu.example.dev_2_cc.entity.OrderStatus;
import edu.example.dev_2_cc.entity.Orders;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderResponseDTO {
    private Long orderId;          // 주문 ID
    private String email;          // 사용자 이메일
    private String name;           // 사용자 이름
    private String address;        // 배송 주소
    private String phoneNumber;    // 전화번호
    private List<OrderItemResponseDTO> orderItems; // 주문 항목 리스트
    private LocalDateTime createdAt; // 주문 생성 시간
    private LocalDateTime updatedAt; // 주문 수정 시간


    //엔티티에서 dto로 변환하는 생성자
    public OrderResponseDTO(Orders order) {
        this.orderId = order.getOrderId();
        this.email = order.getEmail();
        this.name = order.getName();
        this.address = order.getAddress();
        this.phoneNumber = order.getPhoneNumber();
        // 추가적으로 주문 항목 DTO로 변환하는 로직을 구현해야 합니다.
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponseDTO::new) // 각 OrderItem을 OrderItemResponseDTO로 변환
                .collect(Collectors.toList());
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }
}
