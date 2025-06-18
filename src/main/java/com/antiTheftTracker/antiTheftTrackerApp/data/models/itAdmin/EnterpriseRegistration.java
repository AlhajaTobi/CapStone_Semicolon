package com.antiTheftTracker.antiTheftTrackerApp.data.models.itAdmin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "enterprise_registrations")
public class EnterpriseRegistration {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "service_account_key_id", nullable = false)
        private String serviceAccountKeyId;

        @Column(name = "project_id", nullable = false)
        private String projectId;

        @Column(name = "signup_url_name", nullable = false)
        private String signupUrlName;

        @Column(name = "enterprise_token", nullable = false)
        private String enterpriseToken;

        @Column(name = "enterprise_name", unique = true, nullable = false)
        private String enterpriseName;

        @Column(name = "display_name")
        private String displayName;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

}
