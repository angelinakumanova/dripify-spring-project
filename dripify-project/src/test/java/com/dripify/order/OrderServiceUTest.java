package com.dripify.order;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.service.ShoppingCartService;
import com.dripify.category.model.Category;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.notification.service.NotificationService;
import com.dripify.order.model.*;
import com.dripify.order.repository.OrderItemRepository;
import com.dripify.order.repository.OrderRepository;
import com.dripify.order.service.OrderService;
import com.dripify.product.model.Product;
import com.dripify.product.model.ProductImage;
import com.dripify.product.model.enums.Size;
import com.dripify.product.service.ProductService;
import com.dripify.review.model.Review;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.web.dto.OrderCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private ProductService productService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void givenUser_whenGetAllDeliveredOrdersByUserSeller_thenCallsRepositoryCorrectly() {
        // Given
        User seller = new User();
        List<Order> deliveredOrders = List.of(new Order(), new Order());

        when(orderRepository.findBySellerAndStatus(seller, OrderStatus.DELIVERED))
                .thenReturn(deliveredOrders);

        // When
        List<Order> result = orderService.getAllDeliveredOrdersByUserSeller(seller);

        // Then
        verify(orderRepository, times(1))
                .findBySellerAndStatus(seller, OrderStatus.DELIVERED);
        assertSame(deliveredOrders, result);
    }

    @Test
    void givenUser_whenGetPurchasedByUser_thenReturnsGroupedOrdersByDate() {
        // Given
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now().minusDays(1);
        User user = new User();
        Order order1 = new Order();
        order1.setCreatedOn(date1.atStartOfDay());
        Order order2 = new Order();
        order2.setCreatedOn(date1.atStartOfDay());

        Order order3 = new Order();
        order3.setCreatedOn(date2.atStartOfDay());

        List<Order> orders = List.of(order1, order2, order3);

        when(orderRepository.findByPurchaserOrderByCreatedOnDesc(user)).thenReturn(orders);

        // When
        Map<LocalDate, List<Order>> result = orderService.getPurchasedByUser(user);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.containsKey(date1));
        assertTrue(result.containsKey(date2));
        assertEquals(2, result.get(date1).size());
        assertEquals(1, result.get(date2).size());

        verify(orderRepository, times(1)).findByPurchaserOrderByCreatedOnDesc(user);
    }

    @Test
    void givenUser_whenGetSoldByUser_thenReturnsGroupedOrdersByDate() {
        // Given
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now().minusDays(1);
        User user = new User();
        Order order1 = new Order();
        order1.setCreatedOn(date1.atStartOfDay());
        Order order2 = new Order();
        order2.setCreatedOn(date1.atStartOfDay());

        Order order3 = new Order();
        order3.setCreatedOn(date2.atStartOfDay());

        List<Order> orders = List.of(order1, order2, order3);

        when(orderRepository.findBySellerOrderByCreatedOnDesc(user)).thenReturn(orders);

        // When
        Map<LocalDate, List<Order>> result = orderService.getSoldByUser(user);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.containsKey(date1));
        assertTrue(result.containsKey(date2));
        assertEquals(2, result.get(date1).size());
        assertEquals(1, result.get(date2).size());

        verify(orderRepository, times(1)).findBySellerOrderByCreatedOnDesc(user);

    }


    @Test
    void happyPath_whenCreateNewOrder() {
        // Given
        User user = User.builder().id(UUID.randomUUID()).build();
        ShoppingCart shoppingCart = new ShoppingCart();
        user.setShoppingCart(shoppingCart);

        ProductImage productImage = new ProductImage();
        productImage.setImageUrl("image.com");
        Product product = Product.builder()
                .name("Test Product")
                .images(List.of(productImage))
                .price(BigDecimal.valueOf(22))
                .gender(Gender.WOMEN)
                .category(new Category())
                .size(Size.M)
                .seller(new User())
                .build();
        user.getShoppingCart().getProducts().add(product);

        OrderCreateRequest dto = new OrderCreateRequest();
        dto.setPurchaserFullName("Full Name");
        dto.setPurchaserAddress("Address");
        dto.setPurchaserPhoneNumber("333 333 333");
        dto.setCountry(Country.BULGARIA);
        dto.setCity(City.BALCHIK);
        dto.setOrderPayment(OrderPayment.CARD);
        dto.setOrderDeliveryCourier(OrderDeliveryCourier.SPEEDY);

        String fullAddress = String.format("%s, %s, %s", dto.getPurchaserAddress(), dto.getCity().name(), dto.getCountry().name());
        String fullPhoneNumber = String.format("+359 %s", dto.getPurchaserPhoneNumber());
        // When
        List<Order> orders = orderService.createNewOrder(dto, user);

        // Then

        verify(orderRepository, times(orders.size())).save(any(Order.class));
        verify(notificationService, times(1)).sendConfirmationOrder(user.getId(), dto.getPurchaserFullName(),
                fullAddress, fullPhoneNumber, dto.getOrderDeliveryCourier().name(), dto.getOrderPayment().name());
        verify(shoppingCartService, times(1)).clearCart(user.getShoppingCart());
    }

    @Test
    void givenUserShoppingCartIsEmpty_whenCreateNewOrder_thenThrowsException() {
        // Given
        User user = new User();
        user.setShoppingCart(new ShoppingCart());

        // Then && When
        assertThrows(IllegalArgumentException.class, () -> orderService.createNewOrder(new OrderCreateRequest(), user));
        verify(notificationService, never()).sendConfirmationOrder(any(UUID.class), anyString(),
                anyString(), anyString(), anyString(), anyString());
        verify(shoppingCartService, never()).clearCart(user.getShoppingCart());

    }

    @Test
    void happyPath_whenAddReviewToOrder() {
        // Given
        Order order = new Order();
        Review review = new Review();

        when(orderRepository.save(order)).thenReturn(order);

        // When
        orderService.addReviewToOrder(order, review);

        // Then
        assertSame(order.getReview(), review);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void givenOrderWithReview_whenAddReviewToOrder_thenThrowsException() {
        // Given
        Order order = new Order();
        order.setReview(new Review());

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> orderService.addReviewToOrder(order, new Review()));
        verify(orderRepository, never()).save(order);
    }

    @Test
    void givenSellerWithPendingOrders_whenGetPendingOrdersBySeller_thenReturnsPendingOrders() {
        // Given
        User seller = new User();
        seller.setId(UUID.randomUUID());
        seller.setUsername("seller1");

        Order pendingOrder = new Order();
        pendingOrder.setId(2L);
        pendingOrder.setStatus(OrderStatus.PENDING);
        pendingOrder.setSeller(seller);

        List<Order> pendingOrders = List.of(pendingOrder);
        when(orderRepository.getByStatusAndSeller(OrderStatus.PENDING, seller)).thenReturn(pendingOrders);

        // When
        List<Order> result = orderService.getPendingOrdersBySeller(seller);

        // Then
        assertEquals(1, result.size());
        assertSame(pendingOrder, result.getFirst());
    }

    @Test
    void givenExistingPendingOrderAndUserSeller_whenChangeOrderStatus_thenReturnsOrderWithShippedStatus() {
        // Given
        User seller = new User();
        Order order = Order.builder()
                .id(2L)
                .seller(seller)
                .purchaser(User.builder().id(UUID.randomUUID()).build())
                .status(OrderStatus.PENDING)
                .city(City.BALCHIK)
                .country(Country.BULGARIA)
                .purchaserAddress("Address")
                .totalPrice(BigDecimal.TEN)
                .orderPayment(OrderPayment.CARD)
                .orderDeliveryCourier(OrderDeliveryCourier.SPEEDY)
                .build();

        String address = String.format("%s, %s, %s", order.getPurchaserAddress(), order.getCity().toString(), order.getCountry().toString());

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        // When
        orderService.changeOrderStatus(seller, 2L, OrderStatus.SHIPPED);

        // Then
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(notificationService, times(1)).sendShippedOrderEmail(order.getPurchaser().getId(), order.getId(),
                order.getTotalPrice(), order.getOrderPayment().toString(),
                order.getOrderDeliveryCourier().toString(), address);

    }

    @Test
    void givenExistingDeliveredOrderAndUserPurchaser_whenChangeOrderStatus_thenReturnsOrderWithDeliveredStatus() {
        // Given
        User purchaser = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder()
                .id(2L).purchaser(purchaser)
                .status(OrderStatus.SHIPPED)
                .purchaserAddress("Address")
                .totalPrice(BigDecimal.TEN)
                .orderPayment(OrderPayment.CARD)
                .orderDeliveryCourier(OrderDeliveryCourier.SPEEDY)
                .build();

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        // When
        orderService.changeOrderStatus(purchaser, 2L, OrderStatus.DELIVERED);

        // Then
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(notificationService, never()).sendShippedOrderEmail(any(UUID.class), anyLong(),
                any(BigDecimal.class), anyString(),
                anyString(), anyString());
    }

    @Test
    void givenNoValidPurchaserOrSellerForOrder_whenChangeOrderStatus_thenThrowsException() {
        // Given
        Order order = Order.builder().seller(new User()).purchaser(new User()).build();

        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(new User(), 2L, OrderStatus.DELIVERED));
        verify(orderRepository, never()).save(order);
        verify(notificationService, never()).sendShippedOrderEmail(any(UUID.class), anyLong(),
                any(BigDecimal.class), anyString(),
                anyString(), anyString());
    }

    @Test
    void givenNonExistentOrder_whenChangeOrderStatus_thenThrowsException() {
        // Given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then && When
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(null, 2L, OrderStatus.DELIVERED));
        verify(orderRepository, never()).save(any());
        verify(notificationService, never()).sendShippedOrderEmail(any(UUID.class), anyLong(),
                any(BigDecimal.class), anyString(),
                anyString(), anyString());
    }

    @Test
    void givenDifferentPurchaserToOrderPurchaser_whenValidateOrderAccess_thenThrowsException() {
        // Given
        Order order = Order.builder().id(2L).purchaser(User.builder().id(UUID.randomUUID()).build()).build();
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        User differentPurchaser = User.builder().id(UUID.randomUUID()).build();

        // Then && When
        assertThrows(IllegalArgumentException.class, () -> orderService.validateOrderAccess(new User(), differentPurchaser, 2L));
    }

    @Test
    void givenDifferentSellerToOrderSeller_whenValidateOrderAccess_thenThrowsException() {
        // Given
        User purchaser = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder()
                .id(2L)
                .purchaser(purchaser)
                .seller(User.builder().id(UUID.randomUUID()).build()).build();
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));
        User differentSeller = User.builder().id(UUID.randomUUID()).build();

        // Then && When
        assertThrows(IllegalArgumentException.class, () -> orderService.validateOrderAccess(differentSeller, purchaser, 2L));
    }

    @Test
    void givenOrderWithReview_whenValidateOrderAccess_thenThrowsException() {
        // Given
        User purchaser = User.builder().id(UUID.randomUUID()).build();
        User seller = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder()
                .id(2L)
                .purchaser(purchaser)
                .seller(seller)
                .review(new Review())
                .build();
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> orderService.validateOrderAccess(seller, purchaser, 2L));
    }

    @Test
    void happyPath_whenValidateOrderAccess() {
        // Given
        User purchaser = User.builder().id(UUID.randomUUID()).build();
        User seller = User.builder().id(UUID.randomUUID()).build();
        Order order = Order.builder()
                .id(2L)
                .purchaser(purchaser)
                .seller(seller)
                .build();
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order));

        // When
        Order returnedOrder = orderService.validateOrderAccess(seller, purchaser, 2L);

        assertEquals(order, returnedOrder);
    }


}
