package com.antiTheftTracker.antiTheftTrackerApp.data.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name="settings")
public class Setting {
        @Id
        private String key;

        private String value;

        public Setting(String key, String value) {
            this.key = key;
            this.value = value;
        }
}
