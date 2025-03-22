package com.dripify.notification.client;

import com.dripify.notification.client.dto.OrderConfirmationEmailRequest;
import com.dripify.notification.client.dto.UpsertNotificationPreference;
import com.dripify.notification.client.dto.WelcomeEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @PostMapping("/preferences")
    ResponseEntity<Void> upsertNotificationPreference(@RequestBody UpsertNotificationPreference upsertNotificationPreference);

    @PostMapping("/emails/welcome")
    ResponseEntity<Void> sendWelcomeEmail(@RequestBody WelcomeEmailRequest welcomeEmailRequest);

    @PostMapping("/emails/order/confirmation")
    ResponseEntity<Void> sendOrderConfirmationEmail(@RequestBody OrderConfirmationEmailRequest orderConfirmationEmailRequest);
}
