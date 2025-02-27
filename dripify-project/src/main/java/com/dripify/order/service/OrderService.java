package com.dripify.order.service;

import com.dripify.order.model.Order;
import com.dripify.order.model.OrderStatus;
import com.dripify.order.repository.OrderRepository;
import com.dripify.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllDeliveredOrdersByUser(User user) {
        return orderRepository.findBySenderAndStatus(user, OrderStatus.DELIVERED);
    }
}
