package com.markelintl.pq.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class DataFixtures {
    public static final String VALID_ADDRESS_FIXTURE = "{" +
                "\"city\": \"Toronto\"," +
                "\"lines\": [\"200 Wellington Street West\", \"Suite 400\"]," +
                "\"province\": \"Ontario\"," +
                "\"postcode\": \"M5V 3C7\"," +
                "\"country\": \"Canada\"" +
            "}";

    public static final String VALID_INSURED_FIXTURE = "{" +
                "\"fullname\": \"Nate Fisher\"," +
                "\"reference\": \"CUST1234\"," +
                "\"email\": \"noreply@example.net\"," +
                "\"mailingAddress\": " + VALID_ADDRESS_FIXTURE +
            "}";

    public static final String VALID_POLICY_FIXTURE = "{" +
                "\"number\": \"PR1234\"," +
                "\"reference\": \"BR1234\"," +
                "\"timezone\": \"EST\"," +
                "\"inception\": \"2016-01-01\"," +
                "\"expiry\": \"2017-01-01\"," +
                "\"insured\": " + VALID_INSURED_FIXTURE +
            "}";

    public static Address addressFixture() throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(VALID_ADDRESS_FIXTURE, Address.class);
    }

    public static Insured insuredFixture() throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(VALID_INSURED_FIXTURE, Insured.class);
    }

    public static PolicyReference policyFixture() throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(VALID_POLICY_FIXTURE, PolicyReference.class);
    }
}
