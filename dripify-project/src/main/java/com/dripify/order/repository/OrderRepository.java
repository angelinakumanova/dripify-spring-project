package com.dripify.order.repository;

import com.dripify.order.model.Order;
import com.dripify.order.model.OrderStatus;
import com.dripify.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByPurchaser(User purchaser);
}
