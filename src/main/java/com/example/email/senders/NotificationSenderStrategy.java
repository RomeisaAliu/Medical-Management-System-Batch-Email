package com.example.email.senders;



public interface NotificationSenderStrategy {
    void sendNotification(String recipientContactInfo , String message);
}