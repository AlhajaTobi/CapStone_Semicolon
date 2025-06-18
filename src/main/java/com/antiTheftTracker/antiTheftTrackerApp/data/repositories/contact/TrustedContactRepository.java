package com.antiTheftTracker.antiTheftTrackerApp.data.repositories.contact;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrustedContactRepository extends JpaRepository<TrustedContact, String> {
    List<TrustedContact> findByDeviceId(String deviceId);
}
