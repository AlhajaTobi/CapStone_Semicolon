package com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import jakarta.mail.MessagingException;

public interface NotificationService {
    void sendTheftAlert(TrustedContact contact, DeviceEntity device) throws MessagingException;
}
