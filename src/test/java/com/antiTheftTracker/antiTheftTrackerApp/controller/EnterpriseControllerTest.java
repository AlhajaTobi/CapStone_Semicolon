package com.antiTheftTracker.antiTheftTrackerApp.controller;

import com.antiTheftTracker.antiTheftTrackerApp.controllers.itAdmin.EnterpriseController;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.EnterpriseListResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.SignupUrlResponse;
import com.antiTheftTracker.antiTheftTrackerApp.services.enterpriseManagementServ.EnterpriseService;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.ServiceAccountKeyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnterpriseController.class)
@AutoConfigureMockMvc

public class EnterpriseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnterpriseService enterpriseService;

    @MockBean
    private ServiceAccountKeyResolver serviceAccountKeyResolver;



    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetSignupUrl_ValidKey_ReturnsSuccess() throws Exception {
        String keyId = "service_account";
        String projectId = "something";
        String callbackUrl = "anything";

        InputStream mockStream = getClass().getResourceAsStream("/keys/service_account_config.json");

        SignupUrlResponse mockResponse = new SignupUrlResponse("signupUrls/111", "https://safegad.com/signup");

        when(serviceAccountKeyResolver.resolveAsStream(keyId)).thenReturn(mockStream);
        when(enterpriseService.generateSignupUrl(keyId, callbackUrl)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/enterprise/signup-url")
                        .param("serviceAccountKeyId", keyId)
                        .param("projectId", projectId)
                        .param("callbackUrl", callbackUrl))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }



@Test
    void testListEnterprises_ReturnsSuccess() throws Exception {
        String name ="anti-theft tracker";
        String displayName = "anti-theft tracker enterprise";
        EnterpriseListResponse dto = new EnterpriseListResponse(name, displayName);
        dto.setName("enterprises/1234567890abcdef");
        dto.setDisplayName("Anti-Theft Tracker Enterprise");

        List<EnterpriseListResponse> mockResponse = Collections.singletonList(dto);

        when(enterpriseService.listEnterprises("dev")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/enterprise/list")
                        .param("serviceAccountKeyId", "dev"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("enterprises/1234567890abcdef"))
                .andExpect(jsonPath("$[0].displayName").value("Anti-Theft Tracker Enterprise"));
    }

    @Test
    void testListEnterprises_InvalidKeyId_ReturnsBadRequest() throws Exception {
        when(enterpriseService.listEnterprises("invalidId"))
                .thenThrow(new IllegalArgumentException("Invalid service account key ID"));

        mockMvc.perform(get("/api/enterprise/list")
                        .param("serviceAccountKeyId", "invalidId"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid service account key ID"));

    }

}