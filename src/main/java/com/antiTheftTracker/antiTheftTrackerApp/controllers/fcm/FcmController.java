package com.antiTheftTracker.antiTheftTrackerApp.controllers.fcm;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.fcm.FcmTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final DeviceEntityRepository deviceRepository;


@PostMapping("/token")
    public ResponseEntity<?> registerFcmToken(@RequestBody FcmTokenRequest request) {
        DeviceEntity device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        if (!request.getFcmToken().equals(device.getFcmToken())) {
            device.setFcmToken(request.getFcmToken());
            deviceRepository.save(device);
        }

        return ResponseEntity.ok("FCM token updated successfully");
    }

}