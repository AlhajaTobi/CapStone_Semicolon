package com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    private final DeviceEntityRepository deviceEntityRepository;
    @Override
    public void sendToDevice(String fcmToken, String title, String body, String commandType) {
        var message = Message.builder()
                .setToken(fcmToken)
                .putData("title", title)
                .putData("body", body)
                .putData("command", commandType)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        }catch(FirebaseMessagingException error){
            throw new RuntimeException("Failed tp send FCM message",
                    error);
        }
    }

    @Override
    public void broadcastToAll(String title, String body, String commandType) {
        List<DeviceEntity> devices = deviceEntityRepository.findAll();
        devices.forEach(device -> {
            if(device.getFcmToken() != null && !device.getFcmToken().isBlank()) {
                sendToDevice(device.getFcmToken(), title, body, commandType);
            }
        });
    }
}
