package com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceEntityRepository extends JpaRepository<DeviceEntity, String> {

}
