package com.dripify.product.model;

import com.dripify.category.model.Category;
import com.dripify.order.model.Order;
import com.dripify.product.model.enums.*;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "imageUrl")
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User seller;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Brand brand;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Material material;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "product_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;


}
