package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;
import java.io.IOException;


public interface PolicyApplicationService {

    void applyVaultPolicy(String policyName) throws IOException;


    void assignPolicyToDevice(String deviceId, String policyName) throws IOException;


}
