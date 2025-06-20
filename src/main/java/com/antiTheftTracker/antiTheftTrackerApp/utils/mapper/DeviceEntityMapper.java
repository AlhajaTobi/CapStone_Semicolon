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
        return new DeviceEntityResponse(
                device.getId(),
                device.getPolicyName(),
                device.getEnrollmentTime().toString(),
                device.isStolen(),
                device.getLastSeen(),
                (location != null) ? location.getLatitude() : null,
                (location != null) ? location.getLongitude() : null,
                (location != null) ? location.getTimestamp() : null
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
