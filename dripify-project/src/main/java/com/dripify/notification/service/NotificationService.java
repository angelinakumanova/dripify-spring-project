package com.dripify.notification.service;

import com.dripify.notification.client.NotificationClient;
import com.dripify.notification.client.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {
    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void upsertNotificationPreference(UUID userId, boolean newsletterEnabled, String contactData) {
        UpsertNotificationPreference dto = UpsertNotificationPreference.builder()
                .userId(userId)
                .notificationType("EMAIL")
                .newsletterEnabled(newsletterEnabled)
                .contactData(contactData)
                .build();


        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(dto);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc failed] Can't save user preference for user with id [{}]", userId);
            }
        } catch (Exception e) {
            log.warn("Unable to call notification-svc.");
        }
    }

    public void sendWelcomeEmail(UUID userId, String userFirstName) {
        WelcomeEmailRequest dto = WelcomeEmailRequest.builder()
                .subject("Welcome To Dripify!!")
                .emailType("WELCOME")
                .userId(userId)
                .userFirstName(userFirstName)
                .build();


        try {
            ResponseEntity<Void> response = notificationClient.sendWelcomeEmail(dto);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc] Could not send Welcome email to user with id: {}", userId);
            }

        } catch (Exception e) {
            log.warn("Can't send welcome email to user with id [{}] due to 500 Internal Server Error.", userId);
        }

    }

    public void sendConfirmationOrder(UUID userId, String fullName, String address,
                                      String phoneNumber, String courier, String paymentMethod) {


        OrderCreateEmailRequest dto = OrderCreateEmailRequest.builder()
                .subject("Your order is confirmed!")
                .emailType("ORDER_CONFIRMATION")
                .userId(userId)
                .fullName(fullName)
                .address(address)
                .phoneNumber(phoneNumber)
                .courier(courier)
                .paymentMethod(paymentMethod)
                .build();


        try {
            ResponseEntity<Void> response = notificationClient.sendOrderConfirmationEmail(dto);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc] Could not send order confirmation email to user with id: {}", userId);
            }

        } catch (Exception e) {
            log.warn("Can't send order confirmation email to user with id [{}] due to 500 Internal Server Error.", userId);
        }

    }

    public void sendNewOrderEmail(UUID userId, String fullName, String address,
                                  String phoneNumber, String courier, String paymentMethod) {
        OrderCreateEmailRequest dto = OrderCreateEmailRequest.builder()
                .subject("You've got a new order!")
                .emailType("NEW_ORDER")
                .userId(userId)
                .fullName(fullName)
                .address(address)
                .phoneNumber(phoneNumber)
                .courier(courier)
                .paymentMethod(paymentMethod)
                .build();


        try {
            ResponseEntity<Void> response = notificationClient.sendNewOrderEmail(dto);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc] Could not send new order email to user with id: {}", userId);
            }

        } catch (Exception e) {
            log.warn("Can't send new order email to user with id [{}] due to 500 Internal Server Error.", userId);
        }
    }

    public void sendShippedOrderEmail(UUID userId, Long orderId, BigDecimal totalAmount, String paymentMethod,
                                      String courier, String address) {
        OrderShippedEmailRequest orderShipped = OrderShippedEmailRequest.builder()
                .subject("Your order has been shipped!")
                .emailType("SHIPPED_ORDER")
                .userId(userId)
                .orderId(orderId)
                .totalAmount(totalAmount)
                .paymentMethod(paymentMethod)
                .courier(courier)
                .address(address)
                .build();

        try {
            ResponseEntity<Void> response = notificationClient.sendShippedOrderEmail(orderShipped);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc] Could not send Order Shipped email to user with id: {}", userId);
            }
        } catch (Exception e) {
            log.warn("Can't send Order Shipped email to user with id [{}] due to 500 Internal Server Error.", userId);
        }
    }

    public NotificationPreference getNotificationPreference(UUID userId) {

        ResponseEntity<NotificationPreference> httpResponse = notificationClient.getNotificationPreferenceByUser(userId);

        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Notification preference for user [%s] not found.".formatted(userId));
        }

        return httpResponse.getBody();
    }

    public void updateNotificationPreference(UUID userId, boolean enabled) {
        try {
            notificationClient.changeNotificationPreference(userId, enabled);
        } catch (Exception e) {
            log.warn("Can't update notification preferences for user with id = [{}].", userId);
        }
    }
}
