package com.dripify.order.service;

import com.dripify.cart.service.ShoppingCartService;
import com.dripify.notification.service.NotificationService;
import com.dripify.order.model.Order;
import com.dripify.order.model.OrderItem;
import com.dripify.order.model.OrderStatus;
import com.dripify.order.repository.OrderItemRepository;
import com.dripify.order.repository.OrderRepository;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.user.model.User;
import com.dripify.web.dto.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShoppingCartService shoppingCartService, ProductService productService, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.notificationService = notificationService;
    }

    public List<Order> getAllDeliveredOrdersByUserSeller(User user) {
        return orderRepository.findBySellerAndStatus(user, OrderStatus.DELIVERED);
    }

    public Map<LocalDate, List<Order>> getPurchasedByUser(User user) {
        return orderRepository.findByPurchaserOrderByCreatedOnDescIdDesc(user).stream().collect(Collectors.groupingBy(Order::getCreatedOn));
    }

    public Map<LocalDate, List<Order>> getProductsBySeller(User user) {
        return orderRepository.findBySellerOrderByCreatedOnDescIdDesc(user).stream().collect(Collectors.groupingBy(Order::getCreatedOn));
    }


    @Transactional
    public List<Order> createNewOrder(OrderCreateRequest dto, User user) {

        if (user.getShoppingCart().getProducts().isEmpty()) {
            throw new IllegalArgumentException("You can't create an order without any products");
        }

        List<Order> orders = initializeOrdersPerUser(dto, user);
        shoppingCartService.clearCart(user.getShoppingCart());

        String fullAddress = String.format("%s, %s, %s", dto.getPurchaserAddress(), dto.getCity().name(), dto.getCountry().name());
        String fullPhoneNumber = String.format("+359 %s", dto.getPurchaserPhoneNumber());
        notificationService.sendOrderConfirmationEmail(user.getId(), dto.getPurchaserFullName(),
                fullAddress, fullPhoneNumber, dto.getOrderDeliveryCourier().name(), dto.getOrderPayment().name());


        return orders;
    }



    private List<Order> initializeOrdersPerUser(OrderCreateRequest dto, User purchaser) {
        Map<User, List<Product>> productsBySeller = purchaser.getShoppingCart().getProducts().stream()
                .collect(Collectors.groupingBy(Product::getSeller));

        return productsBySeller.entrySet().stream().map((entry) -> {
            User sellerUser = entry.getKey();
            List<Product> products = entry.getValue();

            Order order = initializeOrder(dto, purchaser, sellerUser);
            BigDecimal totalPrice = products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalPrice(totalPrice);

            List<OrderItem> orderItemList = products.stream().map(p -> {
                productService.deactivateProduct(p, sellerUser);

                OrderItem orderItem = OrderItem.builder()
                        .order(order)
                        .mainImageUrl(p.getImages().getFirst().getImageUrl())
                        .name(p.getName())
                        .price(p.getPrice())
                        .gender(p.getGender().name())
                        .category(p.getCategory().getName())
                        .size(p.getSize().name())
                        .build();

                return orderItemRepository.save(orderItem);
            }).toList();

            order.setProducts(orderItemList);
            orderRepository.save(order);

            return order;
        }).toList();
    }

    private Order initializeOrder(OrderCreateRequest dto, User purchaser, User seller) {
        return Order.builder()
                .purchaser(purchaser)
                .seller(seller)
                .purchaserFullName(dto.getPurchaserFullName())
                .purchaserEmail(dto.getPurchaserEmail())
                .purchaserAddress(dto.getPurchaserAddress())
                .purchaserPhoneNumber(dto.getPurchaserPhoneNumber())
                .country(dto.getCountry())
                .city(dto.getCity())
                .orderPayment(dto.getOrderPayment())
                .orderDeliveryCourier(dto.getOrderDeliveryCourier())
                .status(OrderStatus.PENDING)
                .createdOn(LocalDate.now())
                .build();
    }


}
