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


        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(dto);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc failed] Can't save user preference for user with id [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.warn("Unable to call notification-svc.");
        }
    }

    public void sendWelcomeEmail(UUID userId, String userFirstName) {
        WelcomeEmailRequest dto = WelcomeEmailRequest.builder()
                .subject("Welcome To Dripify!!")
                .bodyTemplate("welcome-email")
                .userId(userId)
                .userFirstName(userFirstName)
                .build();


        try {
            ResponseEntity<Void> response = notificationClient.sendWelcomeEmail(dto);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc] Could not send Welcome email to user with id: {%s}".formatted(userId));
            }

        } catch (Exception e) {
            log.warn("Can't send welcome email to user with id [%s] due to 500 Internal Server Error.".formatted(userId));
        }

    }
}
