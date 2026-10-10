package com.careerforge.ai.controller;

import com.careerforge.ai.entity.Notification;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.repository.NotificationRepository;
import com.careerforge.ai.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public NotificationController(NotificationRepository notificationRepository, AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications() {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        notificationRepository.findById(id).ifPresent(n -> {
            if (n.getUser().getId().equals(currentUser.getId())) {
                n.setIsRead(true);
                notificationRepository.save(n);
            }
        });
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }
}
