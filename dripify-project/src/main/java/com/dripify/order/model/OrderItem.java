package com.dripify.order.model;

import com.dripify.category.model.Category;
import com.dripify.product.model.enums.Size;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Order order;

    @Column(nullable = false)
    private String mainImageUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;


}
