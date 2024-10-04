package edu.example.dev_2_cc.service;

import edu.example.dev_2_cc.dto.order.OrderRequestDTO;
import edu.example.dev_2_cc.dto.order.OrderResponseDTO;
import edu.example.dev_2_cc.dto.order.OrderUpdateDTO;
import edu.example.dev_2_cc.dto.orderItem.OrderItemResponseDTO;
import edu.example.dev_2_cc.entity.*;
import edu.example.dev_2_cc.exception.MemberException;
import edu.example.dev_2_cc.exception.OrderException;
import edu.example.dev_2_cc.exception.OrderTaskException;
import edu.example.dev_2_cc.exception.ProductException;
import edu.example.dev_2_cc.repository.MemberRepository;
import edu.example.dev_2_cc.repository.OrderItemRepository;
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
    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;

    // 주문 생성
    public Orders createOrder(OrderRequestDTO orderRequestDTO) {

        //member 테이블에 있는지 없는지 확인 -> 없으면 예외
        Member foundMember = memberRepository.findById(orderRequestDTO.getMemberId()).orElseThrow(MemberException.NOT_FOUND::get);

        //각각 requestBody에 썼으면 그걸로, 비어 있으면 member에 저장되어 있는 정보들로
        String email = orderRequestDTO.getEmail().orElse(foundMember.getEmail());
        String name = orderRequestDTO.getName().orElse(foundMember.getName());
        String address = orderRequestDTO.getAddress().orElse(foundMember.getAddress());
        String phoneNumber = orderRequestDTO.getPhoneNumber().orElse(foundMember.getPhoneNumber());

        // 각 상품에 대한 재고 확인 (forEach 사용)
        orderRequestDTO.getOrderItems().forEach(orderItemDTO -> {
            Product foundProduct = productRepository.findById(orderItemDTO.getProductId())
                    .orElseThrow(ProductException.NOT_FOUND::get);

            // 재고 부족 시 예외 발생
            if (foundProduct.getStock() < orderItemDTO.getQuantity()) {
                throw OrderException.NOT_ENOUGH_STOCK.get();
            }

            foundProduct.changeStock(foundProduct.getStock() - orderItemDTO.getQuantity());
            productRepository.save(foundProduct);

        });

        // orderItem 리스트 생성
        List<OrderItem> orderItems = orderRequestDTO.getOrderItems().stream()
                .map(orderItemDTO -> {
                    // 상품을 다시 조회
                    Product foundProduct = productRepository.findById(orderItemDTO.getProductId())
                            .orElseThrow(ProductException.NOT_FOUND::get);

                    OrderItem orderItem = new OrderItem(foundProduct, orderItemDTO.getQuantity(), null);

                    return orderItem;
                })
                .collect(Collectors.toList());

        // 오더 엔티티 생성
        Orders order = new Orders(foundMember, email, name, address, phoneNumber, orderItems);
        log.info("Order contains " + order.getOrderItems().size() + " items before saving.");

        //주문 저장
        Orders savedOrder = orderRepository.save(order);
        log.info("Order saved with ID: " + savedOrder.getOrderId());

        savedOrder.getOrderItems().forEach(orderItem -> orderItem.setOrders(savedOrder));

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

        log.info("Order ID: " + order.getOrderId() + " has " + order.getOrderItems().size() + " items.");

        if(order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw OrderException.ALREADY_DELIVERED.get();
        }

        order.getOrderItems().forEach(orderItem -> {
            log.info("productId : " + orderItem.getProduct().getProductId() + " quantity : " + orderItem.getQuantity() );
            Product product = orderItem.getProduct();
            product.changeStock(product.getStock() + orderItem.getQuantity());

            productRepository.save(product);
        });

        // 주문 항목 삭제
        order.getOrderItems().forEach(orderItem -> {
            orderItemRepository.delete(orderItem); // orderItemRepository를 사용해 삭제
        });

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
                .email(orderRequestDTO.getEmail().orElseThrow())
                .name(orderRequestDTO.getName().orElseThrow())
                .address(orderRequestDTO.getAddress().orElseThrow())
                .phoneNumber(orderRequestDTO.getPhoneNumber().orElseThrow())
                .orderItems(items)
                .build();

        // OrderItem과 Orders 객체 연결
        items.forEach(item -> item.setOrders(order));

        return order;
    }


}

