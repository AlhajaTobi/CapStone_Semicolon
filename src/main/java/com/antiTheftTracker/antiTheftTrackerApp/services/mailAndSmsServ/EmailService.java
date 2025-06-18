package com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ;

public interface EmailService {
    void sendEmail(String recipient, String subject, String body);
}
