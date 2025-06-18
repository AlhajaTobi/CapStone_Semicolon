package com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.EmailService;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.SmsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor

public class NotificationServiceImpl implements NotificationService {

    @Value("${app.map_url}")
    private String mapUrl;

    private final SmsService smsService;
    private final EmailService emailService;
    @Override
    public void sendTheftAlert(TrustedContact contact, DeviceEntity device) throws MessagingException {
        String message = buildTheftMessage(device);

        if (contact.isSmsEnabled() && contact.getPhoneNumber() != null) {
            smsService.sendSms(contact.getPhoneNumber(), message);
        }

        if (contact.isEmailEnabled() && contact.getEmail() != null) {
            emailService.sendEmail(contact.getEmail(), "The Device Was Reported as Stolen", message);
        }
    }

    private String buildTheftMessage(DeviceEntity device) {

        StringBuilder message = new StringBuilder();

        message.append("DEVICE STOLEN ALERT\n\n");
        message.append("Device ID: ").append(device.getId()).append("\n");

        List<DeviceDetail> details = device.getDeviceDetail();

        if (details != null && !details.isEmpty()) {
            DeviceDetail primaryDetail = details.get(0);
            message.append("Model: ").append(primaryDetail.getDeviceModel()).append("\n");
            message.append("IMEI: ").append(primaryDetail.getImei()).append("\n");
            message.append("Primary SIM ICCID: ").append(primaryDetail.getSimIccidSlot0()).append("\n");
        } else {
            message.append("Model: N/A\n");
            message.append("IMEI: N/A\n");
            message.append("Primary SIM ICCID: N/A\n");
        }

        if (device.getCurrentLocation() != null) {
            double lat = device.getCurrentLocation().getLatitude();
            double lng = device.getCurrentLocation().getLongitude();
            String mapLink = String.format("%s/map?lat=%.5f&lng=%.5f", mapUrl, lat, lng);
            message.append("Last Known Location: ")
                    .append(lat).append(", ").append(lng).append("\n")
                    .append("Map Link: ").append(mapLink).append("\n");
        }

        message.append("Time of theft report: ").append(java.time.LocalDateTime.now()).append("\n");

        return message.toString();
    }

}
