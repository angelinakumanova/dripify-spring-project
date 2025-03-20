package com.dripify.order.service;

import com.dripify.order.model.Order;
import com.dripify.order.model.OrderItem;
import com.dripify.order.model.OrderStatus;
import com.dripify.order.repository.OrderItemRepository;
import com.dripify.order.repository.OrderRepository;
import com.dripify.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllDeliveredOrdersByUserSeller(User user) {
        return orderItemRepository.findBySellerAndOrderStatus(user, OrderStatus.DELIVERED);
    }

    public List<Order> getPurchasedByUser(User user) {
        return orderRepository.findByPurchaser(user);
    }
}
