package com.antiTheftTracker.antiTheftTrackerApp.data.repositories.itAdmin;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.itAdmin.EnterpriseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnterpriseRegistrationRepository extends JpaRepository<EnterpriseRegistration, Long> {
    Optional<EnterpriseRegistration> findByEnterpriseName(String enterpriseName);
    Optional<EnterpriseRegistration> findByProjectIdAndServiceAccountKeyId(String projectId, String serviceAccountId);
}
