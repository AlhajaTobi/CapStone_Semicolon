package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public interface SettingService {

        void saveSetting(String key, String value);

        Optional<String> getSetting(String key);
    }


