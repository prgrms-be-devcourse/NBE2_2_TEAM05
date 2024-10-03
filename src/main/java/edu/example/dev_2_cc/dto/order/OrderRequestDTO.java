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
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private String memberId;

    private Optional<String> email = Optional.empty();           // 사용자 이메일
    private Optional<String> name = Optional.empty();           // 사용자 이름
    private Optional<String> address = Optional.empty();           // 배송 주소
    private Optional<String> phoneNumber = Optional.empty();           // 전화 번호
    private List<OrderItemRequestDTO> orderItems; // 주문 항목 리스트


}
