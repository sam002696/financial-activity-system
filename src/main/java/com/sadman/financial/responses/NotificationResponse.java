package com.sadman.financial.responses;

import com.sadman.financial.entity.Notification;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private String message;
    private boolean isRead;
    private Long userId;


    public static NotificationResponse select(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setMessage(notification.getMessage());
        response.setRead(notification.isRead());
        response.setUserId(notification.getUser().getId());
        return response;
    }
}
