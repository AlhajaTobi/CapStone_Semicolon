package com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.EmailService;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.SmsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    @Override
    public void sendTheftAlert(TrustedContact contact, DeviceEntity device) throws MessagingException {
        String message = buildTheftMessage(device);


        if (contact.isSmsEnabled() && contact.getPhoneNumber() != null) {
            logger.info("Sending SMS to: " + contact.getPhoneNumber());
            try {
                smsService.sendSms(contact.getPhoneNumber(), message);
                logger.info("SMS sent successfully");
            } catch (Exception e) {
                logger.error("SMS service failed: " + e.getMessage());
            }
        }

        if (contact.isEmailEnabled() && contact.getEmail() != null) {
            logger.info("Sending email to: " + contact.getEmail());
            try {
                emailService.sendEmail(contact.getEmail(), "The Device Was Reported as Stolen", message);
                logger.info("Email service call completed successfully");
            } catch (Exception e) {
                logger.error("Email service failed: " + e.getMessage());
            }
        } else {
            logger.error("Email not sent - enabled: " + contact.isEmailEnabled() + ", email: " + contact.getEmail());
        }
    }

    private String buildTheftMessage(DeviceEntity device) {

        StringBuilder message = new StringBuilder();

        message.append("DEVICE STOLEN ALERT <br><br>");
        message.append("Device ID: ").append(device.getId()).append("<br><br>");

        List<DeviceDetail> details = device.getDeviceDetail();

        if (details != null && !details.isEmpty()) {
            DeviceDetail primaryDetail = details.get(0);
            message.append("Model: ").append(primaryDetail.getDeviceModel() != null ? primaryDetail.getDeviceModel() : "N/A").append("<br>");
            message.append("IMEI: ").append(primaryDetail.getImei() != null ? primaryDetail.getImei() : "N/A").append("<br>");
            message.append("Primary SIM ICCID: ").append(primaryDetail.getSimIccidSlot0() != null ? primaryDetail.getSimIccidSlot0() : "N/A").append("<br><br>");
        } else {
            message.append("Model: N/A<br>");
            message.append("IMEI: N/A<br>");
            message.append("Primary SIM ICCID: N/A<br><br>");
        }

        Double latitude = null;
        Double longitude = null;
        
        if (device.getCurrentLocation() != null &&
            device.getCurrentLocation().getLatitude() != null && 
            device.getCurrentLocation().getLongitude() != null) {
            latitude = device.getCurrentLocation().getLatitude();
            longitude = device.getCurrentLocation().getLongitude();
        }
        else if (device.getLatitude() != null && device.getLongitude() != null) {
            latitude = device.getLatitude();
            longitude = device.getLongitude();
        }

        if (latitude != null && longitude != null) {
            String mapLink = String.format("%s/search/?api=1&query=%.5f,%.5f", mapUrl, latitude, longitude);
            message.append("📍 Last Known Location:<br>");
            message.append("   Latitude: ").append(latitude).append("<br>");
            message.append("   Longitude: ").append(longitude).append("<br>");
            message.append("   Map Link: <a href=\"").append(mapLink).append("\">").append(mapLink).append("</a><br><br>");
        } else {
            message.append("📍 Last Known Location: N/A<br><br>");
        }

        message.append("Time of theft report: ").append(java.time.LocalDateTime.now()).append("<br><br>");
        message.append("⚠️  Please take immediate action to secure your trusted contact's device!");

        return message.toString();
    }

}
