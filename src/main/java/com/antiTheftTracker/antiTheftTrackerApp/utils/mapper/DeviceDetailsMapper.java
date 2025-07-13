package com.antiTheftTracker.antiTheftTrackerApp.utils.mapper;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceDetailsResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeviceDetailsMapper {

    public static DeviceDetailsResponse buildReturnResponse(DeviceDetail deviceDetail, DeviceEntity deviceEntity) {
        // Get location data from either currentLocation or direct fields
        Double latitude = null;
        Double longitude = null;
        LocalDateTime locationTimestamp = null;
        
        if (deviceEntity.getCurrentLocation() != null) {
            latitude = deviceEntity.getCurrentLocation().getLatitude();
            longitude = deviceEntity.getCurrentLocation().getLongitude();
            locationTimestamp = deviceEntity.getCurrentLocation().getTimestamp();
        } else {
            latitude = deviceEntity.getLatitude();
            longitude = deviceEntity.getLongitude();
            locationTimestamp = deviceEntity.getEnrollmentTime(); // Use enrollment time as fallback
        }
        
        return new DeviceDetailsResponse(
                deviceDetail.getId(),
                deviceDetail.getManufacturer(),
                deviceDetail.getDeviceModel(),
                deviceDetail.getSerialNumber(),
                deviceDetail.getImei(),
                deviceDetail.getSimIccidSlot0(),
                deviceDetail.getSimIccidSlot1(),
                deviceDetail.getCarrierNameSlot0(),
                deviceDetail.getCarrierNameSlot1(),
                deviceDetail.getPhoneNumberSlot0(),
                deviceDetail.getPhoneNumberSlot1(),
                deviceEntity.getPolicyName(),
                latitude,
                longitude,
                locationTimestamp,
                deviceEntity.getLastSeen(),
                deviceEntity.isStolen()
        );
    }
}
