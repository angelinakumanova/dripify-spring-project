package com.dripify.notification.client;

import com.dripify.notification.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @GetMapping("/preferences")
    ResponseEntity<NotificationPreference> getNotificationPreferenceByUser(@RequestParam(name = "userId") UUID userId);

    @PostMapping("/preferences")
    ResponseEntity<Void> upsertNotificationPreference(@RequestBody UpsertNotificationPreference upsertNotificationPreference);

    @PutMapping("/preferences")
    ResponseEntity<NotificationPreference> changeNotificationPreference(@RequestParam(name = "userId") UUID userId,
                                                                        @RequestParam(name = "enabled") boolean enabled);

    @PostMapping("/emails/welcome")
    ResponseEntity<Void> sendWelcomeEmail(@RequestBody WelcomeEmailRequest welcomeEmailRequest);

    @PostMapping("/emails/order/confirmation")
    ResponseEntity<Void> sendOrderConfirmationEmail(@RequestBody OrderCreateEmailRequest orderCreateEmailRequest);

    @PostMapping("/emails/order/new")
    ResponseEntity<Void> sendNewOrderEmail(@RequestBody OrderCreateEmailRequest orderCreateEmailRequest);

    @PostMapping("/emails/order/shipped")
    ResponseEntity<Void> sendShippedOrderEmail(@RequestBody OrderShippedEmailRequest orderShippedEmailRequest);
}
