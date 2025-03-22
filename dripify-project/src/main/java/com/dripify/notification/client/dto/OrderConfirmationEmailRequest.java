package com.dripify.notification.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderConfirmationEmailRequest {

    private String subject;

    private String bodyTemplate;

    private UUID userId;

    private String fullName;

    private String address;

    private String phoneNumber;

    private String courier;

    private String paymentMethod;
}
