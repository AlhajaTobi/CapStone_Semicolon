package com.antiTheftTracker.antiTheftTrackerApp.utils.messaging;

import com.antiTheftTracker.antiTheftTrackerApp.exceptions.NoGatewayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PhoneNumberUtil {

    @Value("${app.sms.mock-enabled:false}")
    private boolean mockEnabled;
    public String normalize(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    public String addCountryCodeTo(String number) {
        String cleaned = normalize(number);
        if (cleaned.startsWith("0")) {
            return "234" + cleaned.substring(1);
        } else if (cleaned.length() == 10 && !cleaned.startsWith("234")) {
            return "234" + cleaned;
        } else {
            return cleaned;
        }
    }

        public String getCarrierGateway(String carrierName){
            return CARRIER_GATEWAYS.getOrDefault(carrierName.toLowerCase(), null);
        }

        public String getEmailAddressForSms(String phoneNumber, String carrierName){
            if (mockEnabled) {
                return "test+" + normalize(phoneNumber) + "@mock.local";
            }
            String cleanedNumber = addCountryCodeTo(phoneNumber);
            String gateway = getCarrierGateway(carrierName);
            if (gateway == null) throw new NoGatewayException("No carrier gateway found for " + carrierName);
            return String.format("%s@%s.com", cleanedNumber, gateway);
        }

    private static final Map<String, String> CARRIER_GATEWAYS = Map.of(
            "mtn", "mtnmsisdn.com",
            "airtel", "airtelmail.net",
            "glo", "gloworld.com.ng",
            "9mobile", "emtsgsm.com"
    );
    }


