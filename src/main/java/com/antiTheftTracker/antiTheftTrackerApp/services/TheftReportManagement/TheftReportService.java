package com.antiTheftTracker.antiTheftTrackerApp.services.TheftReportManagement;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.report.TheftReportRequest;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.report.TheftReportResponse;

public interface TheftReportService {
    TheftReportResponse handleTheftReport(TheftReportRequest theftReportRequest);


}
