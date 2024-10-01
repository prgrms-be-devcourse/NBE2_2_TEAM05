package edu.example.dev_2_cc.repository;

import edu.example.dev_2_cc.entity.OrderItem;
import edu.example.dev_2_cc.entity.OrderStatus;
import edu.example.dev_2_cc.entity.Orders;
import edu.example.dev_2_cc.entity.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Log4j2
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll(); // 기존 주문 삭제
        productRepository.deleteAll(); // 기존 제품 삭제

        // 테스트를 위한 Product 생성 및 저장
        product = Product.builder()
                .pName("Test Product")
                .price(100L)
                .description("Test Description")
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productRepository.save(product);
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void testCreateOrder() {
        // Given
        Orders order = Orders.builder()
                .email("test@example.com")
                .name("Test User")
                .address("Test Address")
                .phoneNumber("010-1234-5678")
                .build();

        OrderItem orderItem = new OrderItem(product, 2);
        order.addOrderItem(orderItem);  // 주문 항목 추가

        // When
        Orders savedOrder = orderRepository.save(order);

        // Then
        assertNotNull(order.getOrderItems()); // orderItems가 null이 아님을 확인
        assertThat(order.getOrderItems()).hasSize(1);  // item이 추가되었는지 확인

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderItems()).hasSize(1);
        assertThat(savedOrder.getOrderItems().get(0).getProduct()).isEqualTo(product);
    }

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void testFindAllOrders() {
        // Given
        Orders order1 = Orders.builder()
                .email("test1@example.com")
                .name("Test User 1")
                .address("Test Address 1")
                .phoneNumber("010-1111-1111")
                .build();
        order1.addOrderItem(new OrderItem(product, 1));
        orderRepository.save(order1);

        Orders order2 = Orders.builder()
                .email("test2@example.com")
                .name("Test User 2")
                .address("Test Address 2")
                .phoneNumber("010-2222-2222")
                .build();
        order2.addOrderItem(new OrderItem(product, 2));
        orderRepository.save(order2);

        // When
        List<Orders> orders = orderRepository.findAll();

        // Then
        assertThat(orders).hasSize(2); // 두 개의 주문이 저장되어 있어야 함
    }

    @Test
    @DisplayName("주문 조회 테스트")
    void testFindOrderById() {
        // Given
        Orders order = Orders.builder()
                .email("test@example.com")
                .name("Test User")
                .address("Test Address")
                .phoneNumber("010-1234-5678")
                .build();

        OrderItem orderItem = new OrderItem(product, 2);
        order.addOrderItem(orderItem);
        Orders savedOrder = orderRepository.save(order);

        // When
        Orders foundOrder = orderRepository.findById(savedOrder.getOrderId()).orElse(null);

        // Then
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("주문 상태 수정 테스트")
    void testUpdateOrderStatus() {
        // Given
        Orders order = Orders.builder()
                .email("test@example.com")
                .name("Test User")
                .address("Test Address")
                .phoneNumber("010-1234-5678")
                .build();

        OrderItem orderItem = new OrderItem(product, 2);
        order.addOrderItem(orderItem);
        Orders savedOrder = orderRepository.save(order);

        // When
        savedOrder.changeOrderStatus(OrderStatus.valueOf("SHIPPED"));
        Orders updatedOrder = orderRepository.save(savedOrder);

        // Then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    @DisplayName("주문 삭제 테스트")
    void testDeleteOrder() {
        // Given
        Orders order = Orders.builder()
                .email("test@example.com")
                .name("Test User")
                .address("Test Address")
                .phoneNumber("010-1234-5678")
                .build();

        OrderItem orderItem = new OrderItem(product, 2);
        order.addOrderItem(orderItem);
        Orders savedOrder = orderRepository.save(order);

        // When
        orderRepository.delete(savedOrder);

        // Then
        Orders deletedOrder = orderRepository.findById(savedOrder.getOrderId()).orElse(null);
        assertThat(deletedOrder).isNull(); // 삭제된 주문이 존재하지 않아야 함
    }


}
