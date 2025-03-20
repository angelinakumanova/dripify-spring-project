package com.dripify.order.model;

import com.dripify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User purchaser;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> products;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private String purchaserFullName;

    @Column(nullable = false)
    private String purchaserAddress;

    @Column(nullable = false)
    private String purchaserPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderPayment orderPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderDeliveryCourier orderDeliveryCourier;

    @Column(nullable = false)
    private LocalDateTime createdOn;
}
