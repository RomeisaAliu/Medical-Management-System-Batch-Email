package com.example.email.senders;


import com.example.medicalmanagement.model.ContactInfo;

public interface NotificationSenderStrategy {
    void sendNotification(ContactInfo recipientContactInfo , String message);
}