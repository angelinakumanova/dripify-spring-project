package com.dripify.product.model;

import com.dripify.category.model.Category;
import com.dripify.order.model.Order;
import com.dripify.product.model.enums.*;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    private Order order;

    @OneToMany(mappedBy = "product")
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

    @PrePersist
    @PreUpdate
    public void validateSize() {
        if ("clothing".equalsIgnoreCase(category.getParentCategory().getName()) && SizeCategory.SHOE.equals(size.getCategory())) {
            throw new IllegalArgumentException("Invalid clothing size: " + size);
        } else if ("shoes".equalsIgnoreCase(category.getParentCategory().getName()) && SizeCategory.CLOTHING.equals(size.getCategory())) {
            throw new IllegalArgumentException("Invalid shoe size: " + size);
        } else if ("accessories".equalsIgnoreCase(category.getParentCategory().getName()) && size != null) {
            throw new IllegalArgumentException("Product of type accessories must NOT have size.");
        }
    }

}
