package edu.example.dev_2_cc.dto.order;

import edu.example.dev_2_cc.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateDTO {
    private Long orderId;
    private OrderStatus orderStatus;
}
