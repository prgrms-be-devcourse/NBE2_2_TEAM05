package edu.example.dev_2_cc.dto.order;

import edu.example.dev_2_cc.dto.orderItem.OrderItemRequestDTO;
import edu.example.dev_2_cc.entity.OrderItem;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

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
