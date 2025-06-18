package com.antiTheftTracker.antiTheftTrackerApp.data.repositories;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, String> {


}
