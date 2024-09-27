package edu.example.dev_2_cc.dto.orderItem;

import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderItemResponseDTO {
    private Long orderItemId;      // 주문 항목 ID
    private Long productId;        // 상품 ID
    private Integer quantity;      // 수량
    private String pName;          // 상품 이름
    private double price;          // 상품 가격
}
