package com.dripify.notification.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewsletterEmailRequest {

    private String subject;

    private String emailType;

    private UUID userId;

    private String firstName;
}
