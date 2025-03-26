package com.dripify.scheduler;

import com.dripify.notification.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {


    private final NotificationService notificationService;

    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 0 * * 1")
    public void sendWeeklyNewsletterToUsers() {
        notificationService.sendNewsletter();
    }

}
