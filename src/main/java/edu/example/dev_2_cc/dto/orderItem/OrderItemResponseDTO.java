package edu.example.dev_2_cc.dto.orderItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.example.dev_2_cc.dto.product.ProductResponseDTO;
import edu.example.dev_2_cc.entity.OrderItem;
import edu.example.dev_2_cc.entity.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderItemResponseDTO {
    private Long orderItemId;      // 주문 항목 ID
//    private Long productId;        // 상품 ID
    private Integer quantity;      // 수량
//    private String pName;          // 상품 이름
//    private double price;          // 상품 가격

    private ProductResponseDTO product;

    //엔티티에서 dto로 변환
    public OrderItemResponseDTO(OrderItem orderItem) {
        this.orderItemId = orderItem.getOrderItemId();
        this.quantity = orderItem.getQuantity();
        this.product = new ProductResponseDTO(orderItem.getProduct());
    }
}
