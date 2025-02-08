package com.sadman.financial.service.impl;

import com.sadman.financial.entity.Notification;
import com.sadman.financial.entity.User;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.NotificationRepository;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.responses.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
        this.restTemplate = restTemplate;
    }

    @Autowired
    private UserRepository userRepository;

    // Generic method for sending notifications to Express
    public void sendNotification(Long userId, String activityType, String activityDetails) {
        // Constructing the notification message
        String message = activityType + " has been logged: " + activityDetails;

        // Creating the notification object
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRead(false);  // Default value for new notifications

        // Setting the user by fetching it from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));
        notification.setUser(user);

        // Saving the notification in the database
        notificationRepository.save(notification);

        // Sending the notification to the Express server
        sendNotificationToExpress(notification);
    }

    // Sending notification to Express.js WebSocket server
    private void sendNotificationToExpress(Notification notification) {
        // Prepare notification data
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("message", notification.getMessage());
        notificationData.put("userId", notification.getUser().getId());

        // Setting HTTP headers to indicate JSON content
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Creating HttpEntity with the notification data and headers
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(notificationData, headers);

        // URL of the Express server's endpoint
        String expressApiUrl = "http://localhost:3000/send-notification";

        // Sending the notification to Express via HTTP (POST method)
        restTemplate.exchange(expressApiUrl, HttpMethod.POST, entity, String.class);
    }




    // getting all the notifications using the user id
    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(
                userId,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        if (notifications.isEmpty()) {
            throw new CustomMessageException("No notifications found for user " + userId);
        }

        // Mapping the list of notifications to a list of NotificationResponse DTOs
        return notifications.stream()
                .map(NotificationResponse::select)
                .collect(Collectors.toList());
    }


    // not done
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
