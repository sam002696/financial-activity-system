package com.sadman.financial.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Column(name = "is_read")  // Renamed the 'read' column to 'is_read'
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification() {
    }

    public Notification(String message, Long userId) {
        this.message = message;
        this.isRead = false;  // Default value for new notifications
        this.user = new User();
        this.user.setId(userId);
    }

    // Getters and setters
}
