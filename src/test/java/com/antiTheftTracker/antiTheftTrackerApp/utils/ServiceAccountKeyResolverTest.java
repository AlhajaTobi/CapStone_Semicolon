package com.antiTheftTracker.antiTheftTrackerApp.utils;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.ServiceAccountKeyResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest
    public class ServiceAccountKeyResolverTest {

        @Autowired
        private ServiceAccountKeyResolver resolver;

        private static final String VALID_KEY_ID = "service_account_config";

        private static final String INVALID_KEY_ID = "../wrong/path";

        @Test
        void testResolve_ValidKeyId_ReturnsValidPath() throws Exception {
            InputStream inputStream = resolver.resolveAsStream(VALID_KEY_ID);

            assertNotNull(inputStream);
        }

        @Test
        void testResolve_InvalidKeyId_ThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                resolver.resolveAsStream(INVALID_KEY_ID);
            });
        }

    }
