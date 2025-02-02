package com.sadman.financial.service.impl;

import com.sadman.financial.entity.Notification;
import com.sadman.financial.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
        this.restTemplate = restTemplate;
    }

    // Generic method for sending notifications to Express
    public void sendNotification(Long userId, String activityType, String activityDetails) {
        // Construct the notification message
        String message = "A new " + activityType + " has been logged: " + activityDetails;

        // Save the notification in the database
        Notification notification = new Notification(message, userId);
        notificationRepository.save(notification);

        // Send the notification to the Express server
        sendNotificationToExpress(notification);
    }

    // Send notification to Express.js WebSocket server
    private void sendNotificationToExpress(Notification notification) {
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("message", notification.getMessage());
        notificationData.put("userId", notification.getUser().getId());

        // URL of the Express server's endpoint
        String expressApiUrl = "http://localhost:3000/send-notification";

        // Send the notification to Express via HTTP
        restTemplate.postForEntity(expressApiUrl, notificationData, String.class);
    }

    // Optionally, you can add a method to fetch all notifications for a user
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    // Optionally, a method to mark a notification as read
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
