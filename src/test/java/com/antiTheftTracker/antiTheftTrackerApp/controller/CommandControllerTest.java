package com.antiTheftTracker.antiTheftTrackerApp.controller;

import com.antiTheftTracker.antiTheftTrackerApp.controllers.command.CommandController;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.command.CommandRequest;
import com.antiTheftTracker.antiTheftTrackerApp.services.commandManagementServ.CommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @WebMvcTest(controllers = CommandController.class)
    public class CommandControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CommandService commandService;

        private ResultActions sendPost(String url, String content) throws Exception {
            return mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
        }


        @Test
        void testLockDevice_endpoint_triggersServiceCall() throws Exception {
            mockMvc.perform(post("/api/v1/commands/lock/device-123"))
                    .andExpect(status().isAccepted());

            verify(commandService).issueLockCommand("device-123");
        }

        @Test
        void testWipeDevice_endpoint_triggersServiceCall() throws Exception {
            mockMvc.perform(post("/api/v1/commands/wipe/device-123"))
                    .andExpect(status().isAccepted());

            verify(commandService).issueWipeCommand("device-123");
        }



}
