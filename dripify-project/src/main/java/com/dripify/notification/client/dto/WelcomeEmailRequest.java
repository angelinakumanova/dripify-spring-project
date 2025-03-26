package com.dripify.notification.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class WelcomeEmailRequest {
    private String subject;

    private String emailType;

    private UUID userId;

    private String userFirstName;
}
