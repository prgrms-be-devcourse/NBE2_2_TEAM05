package edu.example.dev_2_cc.dto.order;

import edu.example.dev_2_cc.dto.orderItem.OrderItemRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private String email;           // 사용자 이메일
    private String name;            // 사용자 이름
    private String address;         // 배송 주소
    private String phoneNumber;     // 전화번호
    private List<OrderItemRequestDTO> orderItems; // 주문 항목 리스트
}
