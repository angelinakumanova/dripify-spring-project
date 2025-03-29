package com.dripify.order.service;

import com.dripify.cart.service.ShoppingCartService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.notification.service.NotificationService;
import com.dripify.order.model.Order;
import com.dripify.order.model.OrderItem;
import com.dripify.order.model.OrderStatus;
import com.dripify.order.repository.OrderItemRepository;
import com.dripify.order.repository.OrderRepository;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.review.model.Review;
import com.dripify.user.model.User;
import com.dripify.web.dto.OrderCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CloudinaryService cloudinaryService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ShoppingCartService shoppingCartService, ProductService productService, NotificationService notificationService, CloudinaryService cloudinaryService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.notificationService = notificationService;
        this.cloudinaryService = cloudinaryService;
    }

    public List<Order> getAllDeliveredOrdersByUserSeller(User user) {
        return orderRepository.findBySellerAndStatus(user, OrderStatus.DELIVERED);
    }

    public Map<LocalDate, List<Order>> getPurchasedByUser(User user) {
        return orderRepository.findByPurchaserOrderByCreatedOnDesc(user).stream().collect(Collectors.groupingBy(o -> o.getCreatedOn().toLocalDate()));
    }

    public Map<LocalDate, List<Order>> getSoldByUser(User user) {
        return orderRepository.findBySellerOrderByCreatedOnDesc(user).stream().collect(Collectors.groupingBy(o -> o.getCreatedOn().toLocalDate()));
    }


    @Transactional
    public List<Order> createNewOrder(OrderCreateRequest dto, User user) {

        if (user.getShoppingCart().getProducts().isEmpty()) {
            throw new IllegalArgumentException("You can't create an order without any products");
        }

        List<Order> orders = initializeOrdersPerUser(dto, user);


        String fullAddress = String.format("%s, %s, %s", dto.getPurchaserAddress(), dto.getCity().name(), dto.getCountry().name());
        String fullPhoneNumber = String.format("+359 %s", dto.getPurchaserPhoneNumber());
        notificationService.sendConfirmationOrder(user.getId(), dto.getPurchaserFullName(),
                fullAddress, fullPhoneNumber, dto.getOrderDeliveryCourier().name(), dto.getOrderPayment().name());

        sendOrderEmailToSellers(dto, user, fullAddress, fullPhoneNumber);

        shoppingCartService.clearCart(user.getShoppingCart());

        return orders;
    }



    public void addReviewToOrder(Order order, Review review) {

        if (order.getReview() != null) {
            throw new IllegalArgumentException("This order already has a review");
        }

        order.setReview(review);
        orderRepository.save(order);
    }

    public List<Order> getPendingOrdersBySeller(User seller) {
        return orderRepository.getByStatusAndSeller(OrderStatus.PENDING, seller);
    }

    public void changeOrderStatus(User user, Long orderId, OrderStatus orderStatus) {
        Order order = getById(orderId);

        if (order.getSeller() != user && order.getPurchaser() != user) {
            throw new IllegalArgumentException("You can't change the order status of this order.");
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);

        if (OrderStatus.SHIPPED.equals(orderStatus)) {
            String address = String.format("%s, %s, %s", order.getPurchaserAddress(), order.getCity().toString(), order.getCountry().toString());
            notificationService.sendShippedOrderEmail(order.getPurchaser().getId(),
                    order.getId(), order.getTotalPrice(), order.getOrderPayment().toString(),
                    order.getOrderDeliveryCourier().toString(),address);
        }
    }

    public Order validateOrderAccess(User seller, User purchaser, Long orderId) {
        Order order = getById(orderId);

        if (!order.getPurchaser().equals(purchaser)) {
            throw new IllegalArgumentException("You can't leave a review for this order.");
        }

        if (!order.getSeller().equals(seller)) {
            throw new IllegalArgumentException("This order does not belong to this seller");
        }

        if (order.getReview() != null) {
            throw new IllegalArgumentException("This order already has a review");
        }

        return order;
    }

    public Order getById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    private void sendOrderEmailToSellers(OrderCreateRequest dto, User user, String fullAddress, String fullPhoneNumber) {
        user.getShoppingCart()
                .getProducts()
                .stream()
                .map(Product::getSeller)
                .forEach(seller -> notificationService.sendNewOrderEmail(seller.getId(),
                        dto.getPurchaserFullName(), fullAddress, fullPhoneNumber,
                        dto.getOrderDeliveryCourier().name(), dto.getOrderPayment().name()));
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
                String imageUrl = cloudinaryService.copyImageForOrder(p.getImages().getFirst().getImageUrl());

                OrderItem orderItem = OrderItem.builder()
                        .order(order)
                        .imageUrl(imageUrl)
                        .name(p.getName())
                        .price(p.getPrice())
                        .gender(p.getGender())
                        .category(p.getCategory())
                        .size(p.getSize())
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
                .purchaserAddress(dto.getPurchaserAddress())
                .purchaserPhoneNumber(dto.getPurchaserPhoneNumber())
                .country(dto.getCountry())
                .city(dto.getCity())
                .orderPayment(dto.getOrderPayment())
                .orderDeliveryCourier(dto.getOrderDeliveryCourier())
                .status(OrderStatus.PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }





}
