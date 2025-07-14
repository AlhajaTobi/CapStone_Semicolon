package com.antiTheftTracker.antiTheftTrackerApp.utils.mapper;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceLocation;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceEntityResponse;
import com.google.api.services.androidmanagement.v1.model.ApplicationReport;
import com.google.api.services.androidmanagement.v1.model.Device;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class DeviceEntityMapper {

    public static DeviceEntityResponse buildDeviceEntityResponse(DeviceEntity device) {
        DeviceLocation location = device.getCurrentLocation();
        String deviceModel = null;
        if (device.getDeviceDetail() != null && !device.getDeviceDetail().isEmpty()) {
            deviceModel = device.getDeviceDetail().get(0).getDeviceModel();
        }

        String imei = null;
        if (device.getDeviceDetail() != null && !device.getDeviceDetail().isEmpty()) {
            imei = device.getDeviceDetail().get(0).getImei();
        }

        Double latitude = null;
        Double longitude = null;
        LocalDateTime locationTimestamp = null;
        
        if (location != null && location.getLatitude() != null && location.getLongitude() != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationTimestamp = location.getTimestamp();
        } else if (device.getLatitude() != null && device.getLongitude() != null) {
            latitude = device.getLatitude();
            longitude = device.getLongitude();
            locationTimestamp = device.getEnrollmentTime();
        } else {
            latitude = 0.0;
            longitude = 0.0;
            locationTimestamp = device.getEnrollmentTime();
        }

        return new DeviceEntityResponse(
                device.getId(),
                device.getPolicyName(),
                device.getEnrollmentTime().toString(),
                device.isStolen(),
                device.getLastSeen(),
                latitude,
                longitude,
                locationTimestamp,
                deviceModel,
                imei
        );
    }

    public static DeviceEntity toEntity(Device device) {
        DeviceEntity entity = new DeviceEntity();
        entity.setId(extractDeviceId(device));
        entity.setPolicyName(device.getPolicyName());
        entity.setEnrollmentTime(LocalDateTime.parse(device.getEnrollmentTime()));
        entity.setLastSeen(extractLastSeen(device));

        DeviceLocation location = extractLocationFromReports(device.getApplicationReports());
        if (location != null) {
            location.setDevice(entity);
            entity.setCurrentLocation(location);
            entity.setLatitude(location.getLatitude());
            entity.setLongitude(location.getLongitude());
        } else {
            entity.setLatitude(0.0);
            entity.setLongitude(0.0);
        }

        return entity;
    }

    private static String extractDeviceId(Device device) {
        return device.getName();
    }

    private static String extractLastSeen(Device device) {
        return device.getLastStatusReportTime();
    }

    private static DeviceLocation extractLocationFromReports(List<ApplicationReport> reports) {
        if (reports == null || reports.isEmpty()) {
            return null;
        }

        ApplicationReport latestReport = reports.get(0);

        Object rawLocation = latestReport.get("location");
        if (!(rawLocation instanceof Map)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> locationMap = (Map<String, Object>) rawLocation;

        Object latObj = locationMap.get("latitude");
        Object lngObj = locationMap.get("longitude");

        if (!(latObj instanceof Number) || !(lngObj instanceof Number)) {
            return null;
        }

        DeviceLocation deviceLocation = new DeviceLocation();
        deviceLocation.setLatitude(((Number) latObj).doubleValue());
        deviceLocation.setLongitude(((Number) lngObj).doubleValue());
        deviceLocation.setTimestamp(LocalDateTime.now());

        return deviceLocation;
    }


}
