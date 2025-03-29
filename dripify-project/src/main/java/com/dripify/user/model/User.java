package com.dripify.user.model;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.product.model.Product;
import com.dripify.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String imageUrl;

    private String description;

    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts = new HashSet<>();

    @OneToMany(mappedBy = "reviewee")
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive;

    private LocalDate lastModifiedUsername;

    private LocalDate lastModifiedEmail;

    private LocalDate lastModifiedPassword;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

}
