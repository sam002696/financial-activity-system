package com.sadman.financial.repository;

import com.sadman.financial.entity.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndIsRead(Long userId, boolean isRead);
    List<Notification> findByUserId(Long userId, Sort sort);
}
