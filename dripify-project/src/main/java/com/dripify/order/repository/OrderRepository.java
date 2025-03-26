package com.dripify.order.repository;

import com.dripify.order.model.Order;
import com.dripify.order.model.OrderItem;
import com.dripify.order.model.OrderStatus;
import com.dripify.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findBySellerAndStatus(User user, OrderStatus orderStatus);

    List<Order> findByPurchaserOrderByCreatedOnDesc(User purchaser);

    List<Order> findBySellerOrderByCreatedOnDesc(User seller);

    Optional<Order> findById(Long id);

    List<Order> getByStatusAndSeller(OrderStatus status, User seller);
}
