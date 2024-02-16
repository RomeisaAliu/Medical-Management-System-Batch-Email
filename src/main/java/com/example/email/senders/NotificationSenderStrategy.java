package com.example.email.senders;


import com.example.medicalmanagement.model.ContactInfo;
import org.springframework.stereotype.Repository;

public interface NotificationSenderStrategy {
    void sendNotification(ContactInfo recipientContactInfo , String message);
}