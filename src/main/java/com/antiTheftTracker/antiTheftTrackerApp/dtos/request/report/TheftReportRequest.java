package com.antiTheftTracker.antiTheftTrackerApp.dtos.request.report;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheftReportRequest {
    private String deviceId;
    private String simSerial;
    private String latitude;
    private String longitude;
}
