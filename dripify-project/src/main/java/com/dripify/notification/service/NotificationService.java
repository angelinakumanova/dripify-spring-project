package com.dripify.notification.service;

import com.dripify.notification.client.NotificationClient;
import com.dripify.notification.client.dto.UpsertNotificationPreference;
import com.dripify.notification.client.dto.WelcomeEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        ResponseEntity<Void> response = notificationClient.upsertNotificationPreference(dto);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("[Feign call to notification-svc] Failed to upsert notification preference for user with id: {%s}".formatted(userId));
        }
    }

    public void sendWelcomeEmail(UUID userId, String userFirstName) {
        WelcomeEmailRequest dto = WelcomeEmailRequest.builder()
                .userId(userId)
                .userFirstName(userFirstName)
                .build();

        ResponseEntity<Void> response = notificationClient.sendWelcomeEmail(dto);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("[Feign call to notification-svc] Could not send Welcome email to user with id: {%s}".formatted(userId));
        }
    }
}
