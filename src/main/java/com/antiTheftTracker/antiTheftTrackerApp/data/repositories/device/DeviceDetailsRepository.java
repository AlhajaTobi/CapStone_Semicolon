package com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceDetailsRepository extends JpaRepository<DeviceDetail, String> {
    Optional<DeviceDetail> findByImei(String imei);
    Optional<DeviceDetail> findBySerialNumber(String serialNumber);

}
