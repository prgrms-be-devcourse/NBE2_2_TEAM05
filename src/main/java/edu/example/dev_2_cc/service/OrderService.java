package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.order.OrderRequestDTO;
import edu.example.dev_2_cc.dto.order.OrderResponseDTO;
import edu.example.dev_2_cc.dto.order.OrderUpdateDTO;
import edu.example.dev_2_cc.dto.orderItem.OrderItemResponseDTO;
import edu.example.dev_2_cc.entity.OrderItem;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.entity.Product;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.OrderException;
import edu.example.dev_2_cc.exception.OrderTaskException;
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

    // 주문 생성
    public Orders createOrder(OrderRequestDTO orderRequestDTO) {
        // OrderRequestDTO를 Orders 엔티티로 변환
        Orders order = toEntity(orderRequestDTO);
        // 주문을 DB에 저장 (저장 후 ID가 생성됨)
        Orders savedOrder = orderRepository.save(order);

        // OrderItem과 Orders 객체 연결
        savedOrder.getOrderItems().forEach(item -> item.setOrders(savedOrder));

        return savedOrder;
    }

    // 전체 주문 조회
    public List<OrderResponseDTO> list() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponseDTO::new) // Orders 엔티티를 OrderResponseDTO로 변환
                .collect(Collectors.toList()); // 변환된 OrderResponseDTO 리스트로 반환
    }

    // 단일 주문 조회
    public Orders findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> OrderException.NOT_FOUND.get());
    }

    // 주문 상태 수정
    public Orders modifyStatus(OrderUpdateDTO orderUpdateDTO) {
        Orders order = findOrderById(orderUpdateDTO.getOrderId());
        order.changeOrderStatus(orderUpdateDTO.getOrderStatus());

        return orderRepository.save(order);
    }

    // 주문 삭제
    public void delete(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> OrderException.NOT_FOUND.get());

        orderRepository.delete(order);


    }

    // toEntity 메서드
    // OrderRequestDTO 작성하려고 했으나 DI 단계에서 DTO는 Spring의 관리 대상이 아니라 다른 bean을 주입받지 못해
    // Service단에 작성함
    // 엔티티에서 DTO로의 변환은 responseDTO에 생성자로 설정
    private Orders toEntity(OrderRequestDTO orderRequestDTO) {
        List<OrderItem> items = orderRequestDTO.getOrderItems().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(()->new RuntimeException("Product Not Found"));// Product 조회
                            //추후 OrderException으로 리팩토링 필요

                    OrderItem orderItem = new OrderItem(product, item.getQuantity());
                    orderItem.setOrders(null);
                    return orderItem;
                })
                .collect(Collectors.toList());

        Orders order = Orders.builder()
                .email(orderRequestDTO.getEmail())
                .name(orderRequestDTO.getName())
                .address(orderRequestDTO.getAddress())
                .phoneNumber(orderRequestDTO.getPhoneNumber())
                .orderItems(items)
                .build();

        // OrderItem과 Orders 객체 연결
        items.forEach(item -> item.setOrders(order));

        return order;
    }


}

