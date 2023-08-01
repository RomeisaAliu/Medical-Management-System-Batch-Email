package com.example.email.Senders;

import com.example.medicalmanagement.model.NotificationType;

public interface NotificationSenderStrategy {
    void sendNotification(String doctorEmail, String message, NotificationType type);
    }