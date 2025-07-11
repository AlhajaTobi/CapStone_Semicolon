package com.antiTheftTracker.antiTheftTrackerApp.controllers.fcm;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final DeviceEntityRepository deviceRepository;

    @PostMapping("/token")
    public ResponseEntity<String> updateFcmToken(@RequestBody Map<String, String> request) {
        String deviceId = request.get("deviceId");
        String fcmToken = request.get("fcmToken");
        
        if (deviceId == null || fcmToken == null) {
            return ResponseEntity.badRequest().body("Device ID and FCM token are required");
        }
        
        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        
        device.setFcmToken(fcmToken);
        deviceRepository.save(device);
        
        return ResponseEntity.ok("FCM token updated successfully");
    }
}