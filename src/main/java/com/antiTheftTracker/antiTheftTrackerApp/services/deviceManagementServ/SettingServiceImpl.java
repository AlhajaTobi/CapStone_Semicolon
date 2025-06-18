package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.Setting;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public void saveSetting(String key, String value) {
        settingRepository.save(new Setting(key, value));
    }

    @Override
    public Optional<String> getSetting(String key) {
        return settingRepository.findById(key).map(Setting::getValue);
    }
}
