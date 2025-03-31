package com.dripify.service;

import com.dripify.notification.client.NotificationClient;
import com.dripify.notification.client.dto.OrderCreateEmailRequest;
import com.dripify.notification.client.dto.UpsertNotificationPreference;
import com.dripify.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void givenSuccessfulResponse_whenUpsertNotificationPreference_thenUpsertNotificationPreference() {
        // Given
        UUID userId = UUID.randomUUID();
        boolean newsletterEnabled = true;
        String email = "test@example.com";

        ResponseEntity<Void> httpResponse = new ResponseEntity<>(HttpStatus.CREATED);
        when(notificationClient.upsertNotificationPreference(any(UpsertNotificationPreference.class))).thenReturn(httpResponse);

        // When
        notificationService.upsertNotificationPreference(userId, newsletterEnabled, email);

        // Then
        verify(notificationClient, times(1)).upsertNotificationPreference(any(UpsertNotificationPreference.class));

    }

//    @Test
//    void givenFailedResponse_whenUpsertNotificationPreference_thenNoExceptionIsThrown() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        boolean newsletterEnabled = true;
//        String email = "test@example.com";
//
//        ResponseEntity<Void> httpResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        when(notificationClient.upsertNotificationPreference(any(UpsertNotificationPreference.class))).thenReturn(httpResponse);
//
//
//        // When
//        notificationService.upsertNotificationPreference(userId, newsletterEnabled, email);
//
//        verify(notificationClient, times(1)).upsertNotificationPreference(any(UpsertNotificationPreference.class));
//    }

    @Test
    void givenSuccessfulResponse_whenSendConfirmationOrder_thenSendEmail() {
        // Given
        UUID userId = UUID.randomUUID();
        String fullName = "John Doe";
        String address = "123 Street, City, Country";
        String phoneNumber = "+359123456789";
        String courier = "Courier X";
        String paymentMethod = "Credit Card";

        ResponseEntity<Void> successfulResponse = new ResponseEntity<>(HttpStatus.OK);
        when(notificationClient.sendOrderConfirmationEmail(any(OrderCreateEmailRequest.class)))
                .thenReturn(successfulResponse);

        // When
        notificationService.sendConfirmationOrder(userId, fullName, address, phoneNumber, courier, paymentMethod);

        // Then
        verify(notificationClient, times(1)).sendOrderConfirmationEmail(any(OrderCreateEmailRequest.class));
    }

//    @Test
//    void givenFailedResponse_whenSendConfirmationOrder_thenLogError() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        String fullName = "John Doe";
//        String address = "123 Street, City, Country";
//        String phoneNumber = "+359123456789";
//        String courier = "Courier X";
//        String paymentMethod = "Credit Card";
//
//        ResponseEntity<Void> failureResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        when(notificationClient.sendOrderConfirmationEmail(any(OrderCreateEmailRequest.class)))
//                .thenReturn(failureResponse);
//
//        Logger mockLogger = mock(org.slf4j.Logger.class);
//
//        // When
//        notificationService.sendConfirmationOrder(userId, fullName, address, phoneNumber, courier, paymentMethod);
//
//        // Then
//        verify(notificationClient, times(1)).sendOrderConfirmationEmail(any(OrderCreateEmailRequest.class));
//
//        verify(mockLogger, times(1)).error(anyString(), eq(userId));
//    }
//
//    @Test
//    void givenException_whenSendConfirmationOrder_thenLogWarning() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        String fullName = "John Doe";
//        String address = "123 Street, City, Country";
//        String phoneNumber = "+359123456789";
//        String courier = "Courier X";
//        String paymentMethod = "Credit Card";
//
//        // Mock the notificationClient to throw an exception
//        when(notificationClient.sendOrderConfirmationEmail(any(OrderCreateEmailRequest.class)))
//                .thenThrow(new RuntimeException("Internal error"));
//
//        // Mock the logger to capture logs
//        Logger mockLogger = mock(Logger.class);
//        ReflectionTestUtils.setField(orderService, "log", mockLogger);
//
//        // When
//        orderService.sendConfirmationOrder(userId, fullName, address, phoneNumber, courier, paymentMethod);
//
//        // Then
//        // Verify that the method was called
//        verify(notificationClient, times(1)).sendOrderConfirmationEmail(any(OrderCreateEmailRequest.class));
//
//        // Verify that a warning is logged when exception occurs
//        verify(mockLogger, times(1)).warn(anyString(), eq(userId));
//    }
}
