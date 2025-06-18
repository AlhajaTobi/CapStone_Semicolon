package com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement;

public interface FirebaseMessagingService {
    void sendToDevice(String fcmToken, String title, String body, String commandType);
    void broadcastToAll(String title, String body, String commandType);
}
