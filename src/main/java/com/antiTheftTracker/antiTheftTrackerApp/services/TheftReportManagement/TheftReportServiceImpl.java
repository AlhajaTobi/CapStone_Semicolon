package com.antiTheftTracker.antiTheftTrackerApp.services.TheftReportManagement;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.command.Command;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceLocation;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.command.CommandRepository;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.report.TheftReportRequest;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.report.TheftReportResponse;
import com.antiTheftTracker.antiTheftTrackerApp.exceptions.TokenNotFoundException;
import com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement.FirebaseMessagingService;
import com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.lang.Double.parseDouble;

@Service
@RequiredArgsConstructor
public class TheftReportServiceImpl implements TheftReportService {

    private final DeviceEntityRepository deviceRepository;
    private final CommandRepository commandRepository;
    private final NotificationService notificationService;
    private final FirebaseMessagingService firebaseMessagingService;

    @Override
    public TheftReportResponse handleTheftReport(TheftReportRequest request) {
        DeviceEntity device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setStolen(true);
        device.setLastKnownSim(request.getSimSerial());
        device.setCurrentLocation(extractDeviceLocation(request));

        deviceRepository.save(device);

        Command command = new Command();
        setupBaseCommand(command, device, "theft_report");
        command.setSimSerial(request.getSimSerial());

        commandRepository.save(command);

        notifyTrustedContacts(device);
        sendTheftAlertToDevice(device);

        TheftReportResponse response = new TheftReportResponse();
        response.setMessage("Theft report successfully sent");

        return response;
    }

    private void sendTheftAlertToDevice(DeviceEntity device){
        if(device.getFcmToken() == null || device.getFcmToken().isEmpty()){
            throw new TokenNotFoundException("No FCM token for device: " + device.getId());
        }

        firebaseMessagingService.sendToDevice(
                device.getFcmToken(),
                "Device Stolen",
                "Your device mau be compromised.",
                "theft_report"
        );
    }

    private void notifyTrustedContacts(DeviceEntity device) {
        if (device.getTrustedContacts() != null && !device.getTrustedContacts().isEmpty()) {
            device.getTrustedContacts().forEach(contact -> {
                try {
                    notificationService.sendTheftAlert(contact, device);
                } catch (Exception e) {
                    System.err.println("Failed to notify contact: " + contact.getName());
                    e.printStackTrace();
                }
            });
        }
    }

    private DeviceLocation extractDeviceLocation(TheftReportRequest request) {
        DeviceLocation location = new DeviceLocation();

        if(request.getLatitude() != null && request.getLongitude() != null) {
            location.setLatitude(parseDouble(request.getLatitude()));
            location.setLongitude(parseDouble(request.getLongitude()));
            location.setTimestamp(LocalDateTime.now());
        }
        return location;

    }

    private void setupBaseCommand(Command command, DeviceEntity device, String commandType) {
        command.setType(commandType);
        command.setDevice(device);
        command.setIssuedAt(LocalDateTime.now());
        command.setExecuted(false);
    }
}