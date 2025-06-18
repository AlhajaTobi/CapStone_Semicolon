package com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ;

import jakarta.mail.MessagingException;

public interface SmsService {
    void sendSms(String phoneNumber, String message) throws MessagingException;
}
