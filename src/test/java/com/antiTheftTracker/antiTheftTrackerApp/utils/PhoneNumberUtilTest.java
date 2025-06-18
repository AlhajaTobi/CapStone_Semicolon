package com.antiTheftTracker.antiTheftTrackerApp.utils;

import com.antiTheftTracker.antiTheftTrackerApp.utils.messaging.PhoneNumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class PhoneNumberUtilTest {

    @Autowired
    PhoneNumberUtil phoneNumberUtil;

        @Test
        void testNormalize_removesNonDigits() {
            assertEquals("08012345678", phoneNumberUtil.normalize("0801-234-5678"));
            assertEquals("2348012345678", phoneNumberUtil.addCountryCodeTo("08012345678"));
            assertEquals("2348012345678", phoneNumberUtil.addCountryCodeTo("08012345678"));
        }

        @Test
        void testGetEmailAddressForSms_returnsValidFormat() {
            String recipient = phoneNumberUtil.getEmailAddressForSms("08012345678", "mtn");
            assertNotNull(recipient);
            assertEquals("2348012345678@mtnmsisdn.com", recipient);
        }

        @Test
        void testGetEmailAddressForAirtel_returnsValidFormat() {
            String recipient = phoneNumberUtil.getEmailAddressForSms("09012345678", "airtel");
            assertNotNull(recipient);
            assertEquals("2349012345678@airtelmail.net", recipient);
        }

        @Test
        void testGetEmailAddressForUnknownCarrier_returnsNull() {
            String recipient = phoneNumberUtil.getEmailAddressForSms("08012345678", "unknown");
            assertNull(recipient);
        }

}
