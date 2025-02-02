package com.sadman.financial.controller.Notification;

import com.sadman.financial.entity.Notification;
import com.sadman.financial.service.impl.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notifications")
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Get all notifications for a specific user",
            description = "Retrieve all notifications for a specific user based on the userId",
            responses = {
                    @ApiResponse(
                            description = "Successfully retrieved notifications",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Notification.class)
                            )
                    ),
                    @ApiResponse(
                            description = "User not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/user/{userId}")
    public List<Notification> getNotifications(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @Operation(
            summary = "Mark notification as read",
            description = "Marks a notification as read based on the notificationId",
            responses = {
                    @ApiResponse(
                            description = "Notification marked as read successfully",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            description = "Notification not found",
                            responseCode = "404"
                    )
            }
    )
    @PostMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }
}
