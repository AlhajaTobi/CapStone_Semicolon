package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceLocation;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceLocationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class DeviceLocationServiceImpl implements DeviceLocationService {

    private final DeviceEntityRepository deviceEntityRepository;
    @Override
    public void updateDeviceLocation(String deviceId, DeviceLocationRequest request) {
        DeviceEntity device = deviceEntityRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceLocation location = device.getCurrentLocation();
        if (location == null) {
            location = new DeviceLocation();
            location.setDevice(device);
        }

        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setTimestamp(LocalDateTime.now());

        device.setCurrentLocation(location);
        deviceEntityRepository.save(device);
    }
}
