package com.dripify.order.model;

import com.dripify.review.model.Review;
import com.dripify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "order")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private User purchaser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User seller;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> products;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private String purchaserFullName;

    @Column(nullable = false)
    private String purchaserEmail;

    @Column(nullable = false)
    private String purchaserAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private City city;

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
