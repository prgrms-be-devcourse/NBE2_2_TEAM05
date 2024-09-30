package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.order.OrderRequestDTO;
import edu.example.dev_2_cc.entity.OrderItem;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.repository.OrderRepository;
import edu.example.dev_2_cc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Orders createOrder(OrderRequestDTO orderRequestDTO) {
        // OrderRequestDTO를 Orders 엔티티로 변환
        Orders order = toEntity(orderRequestDTO);

        // 주문을 DB에 저장
        return orderRepository.save(order);
    }

    // toEntity 메서드
    // OrderRequestDTO 작성하려고 했으나 DI 단계에서 DTO는 Spring의 관리 대상이 아니라 다른 bean을 주입받지 못해
    // Service단에 작성함
    // 엔티티에서 DTO로의 변환은 responseDTO에 생성자로 설정
    private Orders toEntity(OrderRequestDTO orderRequestDTO) {
        List<OrderItem> items = orderRequestDTO.getOrderItems().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(()->new RuntimeException("Product Not Found")); // Product 조회
                    return new OrderItem(product, item.getQuantity(), null); // OrderItem 생성
                })
                .collect(Collectors.toList());

        return Orders.builder()
                .email(orderRequestDTO.getEmail())
                .name(orderRequestDTO.getName())
                .address(orderRequestDTO.getAddress())
                .phoneNumber(orderRequestDTO.getPhoneNumber())
                .orderItems(items)
                .build();
    }




}

