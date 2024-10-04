package edu.example.dev_2_cc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Email
    private String email;
    private String phoneNumber;
    private String name;
    private String address;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderItem> orderItems= new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.APPROVED;


    public Orders( Member member, String email, String phoneNumber, String name, String address, List<OrderItem> orderItems) {
        this.member = member;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.address = address;
        this.orderItems = orderItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrders(this); // 이곳에서 OrderItem의 Orders를 설정
    }

    // 주문 항목 제거 메서드
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        if (orderItem != null) {
            orderItem.setOrders(null); // 양방향 관계 설정 해제
        }
    }
}
