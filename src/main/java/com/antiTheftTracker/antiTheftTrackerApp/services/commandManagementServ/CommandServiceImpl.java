package com.antiTheftTracker.antiTheftTrackerApp.services.commandManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.command.Command;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.command.CommandRepository;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.exceptions.CommandNotExecutedException;
import com.antiTheftTracker.antiTheftTrackerApp.exceptions.DeviceNotFoundException;
import com.antiTheftTracker.antiTheftTrackerApp.services.contactManagement.TrustedContactService;
import com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement.FirebaseMessagingService;
import com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement.NotificationService;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    
    private final DeviceEntityRepository deviceRepository;
    private final CommandRepository commandRepository;
    private final AndroidManagementFactory androidManagementFactory;
    private final NotificationService notificationService;
    private final TrustedContactService contactService;
    private final FirebaseMessagingService firebaseMessagingService;



    @Override
    public void issueLockCommand(String deviceId) {
        try {
            var device = deviceRepository.findById(deviceId).orElseThrow(()->new DeviceNotFoundException("Device not found: " + deviceId));
            var command = new Command();
            setupBaseCommand(command, device, "lock");
            commandRepository.save(command);
            sendFcmMessageToDevice(device, "lock");
            notifyTrustedContacts(deviceId, device);
            command.setExecuted(true);
        } catch (CommandNotExecutedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void issueWipeCommand(String deviceId) {
        try {
            var device = deviceRepository.findById(deviceId).orElseThrow(()->new DeviceNotFoundException("Device not found: " + deviceId));
            var command = new Command();
            setupBaseCommand(command, device, "wipe");

            commandRepository.save(command);
            sendFcmMessageToDevice(device, "wipe");
            notifyTrustedContacts(deviceId, device);
            command.setExecuted(true);
        } catch (CommandNotExecutedException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendFcmMessageToDevice(DeviceEntity device, String commandType) {
        String title = "SafeGad";
        String body = COMMAND_MESSAGES.getOrDefault(commandType, "Unknown command");

        if(device.getFcmToken() == null || device.getFcmToken().isEmpty()) return;

        firebaseMessagingService.sendToDevice(
                device.getFcmToken(),
                title,
                body,
                commandType
        );
    }

    private static final Map<String, String> COMMAND_MESSAGES = Map.of(
            "lock", "Your device has been locked due to theft report",
            "wipe", "All data will be wiped from your device",
            "getLocation", "Current location reported"
    );


    private void notifyTrustedContacts(String deviceId, DeviceEntity device) {
               contactService.getContactsForDevice(deviceId).forEach(contact ->
                       {
                           try {
                               notificationService.sendTheftAlert(contact, device);
                           } catch (MessagingException e) {
                                   throw new RuntimeException(e);
                           }
                       }
               );
    }

    private void sendRemoteCommandToDevice(String deviceId, String commandType) {
        try {
            var client = androidManagementFactory.getClient();
            var googleCommand = new com.google.api.services.androidmanagement.v1.model.Command();
            googleCommand.setType(commandType);

            client.enterprises().devices().issueCommand(deviceId, googleCommand).execute();
        } catch (Exception error) {
            throw new RuntimeException("Failed to send remote command", error);
        }
    }

    private void setupBaseCommand(Command command, DeviceEntity device, String commandType) {
        command.setType(commandType);
        command.setDevice(device);
        command.setIssuedAt(LocalDateTime.now());
        command.setExecuted(false);
        command.setLatitude(device.getLatitude());
        command.setLongitude(device.getLongitude());
    }

 


}
