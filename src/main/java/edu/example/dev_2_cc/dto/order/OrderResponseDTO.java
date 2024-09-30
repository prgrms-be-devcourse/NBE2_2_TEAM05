package edu.example.dev_2_cc.dto.order;

import edu.example.dev_2_cc.dto.orderItem.OrderItemResponseDTO;
import edu.example.dev_2_cc.entity.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
