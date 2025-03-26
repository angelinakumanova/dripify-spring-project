package com.dripify.notification.client.dto;

import lombok.Data;

@Data
public class NotificationPreference {

    private String type;

    private boolean isNewsletterEnabled;

    private String contactData;
}
