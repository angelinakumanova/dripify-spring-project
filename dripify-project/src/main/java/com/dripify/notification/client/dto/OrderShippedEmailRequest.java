package com.dripify.notification.client.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderShippedEmailRequest {

    private String subject;

    private String emailType;

    private UUID userId;

    private Long orderId;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private String courier;

    private String address;
}
