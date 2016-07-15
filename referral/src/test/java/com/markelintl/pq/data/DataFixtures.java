package com.markelintl.pq.data;

import java.io.IOException;
import java.text.ParseException;

import static com.markelintl.pq.data.PolicyReference.parseDate;

public class DataFixtures {

    public static Address addressFixture() throws IOException {
        final Address address = new Address("Toronto", "Canada", new String[]{ "200 Wellington Street West", "Suite 400" }, "Ontario", "M5V 3C7");
        return address;
    }

    public static Insured insuredFixture() throws IOException {
        final Insured insured = new Insured("CUST1234", "Nate Fisher", "noreply@example.net", addressFixture());
        return insured;
    }

    public static PolicyReference policyFixture() throws IOException, ParseException {
        final PolicyReference policyReference = new PolicyReference("PR1234",
                "EST",
                parseDate("2016-01-01", "EST"),
                parseDate("2017-01-01", "EST"),
                insuredFixture(),
                "BR1234");
        return policyReference;
    }
}
