package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceEntityResponse;
import com.antiTheftTracker.antiTheftTrackerApp.utils.mapper.DeviceEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class DeviceEntityServiceImpl implements DeviceEntityService {

    private final DeviceEntityRepository deviceEntityRepo;
    
    @Override
    public DeviceEntityResponse getDeviceMetaData(String deviceId) {
        DeviceEntity deviceEntity = deviceEntityRepo.findById(deviceId)
                .orElseThrow(()-> new IllegalArgumentException("Device not found"));
        return DeviceEntityMapper.buildDeviceEntityResponse(deviceEntity);
    }

    @Override
    public List<DeviceEntityResponse> getAllDevices() {
        List<DeviceEntity> devices = deviceEntityRepo.findAll();
        return devices.stream()
                .map(DeviceEntityMapper::buildDeviceEntityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getDeviceLocation(String deviceId) {
        DeviceEntity device = deviceEntityRepo.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        
        return Map.of(
            "deviceId", device.getId(),
            "latitude", device.getLatitude(),
            "longitude", device.getLongitude(),
            "lastSeen", device.getLastSeen()
        );
    }

    @Override
    public void updateDeviceLocation(String deviceId, Double latitude, Double longitude) {
        DeviceEntity device = deviceEntityRepo.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        
        device.setLatitude(latitude);
        device.setLongitude(longitude);
        device.setLastSeen(java.time.LocalDateTime.now().toString());
        
        deviceEntityRepo.save(device);
    }
}
