package com.markelintl.pq.data;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class PolicyReferenceTest {
    @Test
    public void compareTo_should_serialize_valid_insured() throws IOException, ParseException {
        final PolicyReference expected = new PolicyReference("PR1234", "EST", PolicyReference.parseDate("2016-01-01"), PolicyReference.parseDate("2017-01-01"), DataFixtures.insuredFixture(), "BR1234");
        final PolicyReference fixture = DataFixtures.policyFixture();

        assertThat(fixture.compareTo(expected), is(0));
    }

    @Test
    public void compareTo_should_fail_on_mismatch() throws IOException, ParseException {
        final String[][] testData = new String[][]{
                // 0      , 1       , 2                           , 3          , 4        , 5        , 6         , 5            , 6                    , 7        , 8    , 9           , 10          , 11
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PRI1234", "EST", "2016-01-01", "2017-01-01", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "PST", "2016-01-01", "2017-01-01", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-02", "2017-01-01", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-01", "2017-01-02", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-01", "2017-01-01", "BR123"},
            {"Toront", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-01", "2017-01-01", "BR1234"},
        };

        final int[] testResult = new int[] {
            1, 2, 4, 8, 16, 32,
        };

        final PolicyReference expected = DataFixtures.policyFixture();

        for (int i = 0; i < testData.length; i++) {
            final String[] d = testData[i];
            final Address address = new Address(d[0], d[1], new String[]{d[2], d[3]}, d[4], d[5]);
            final Insured insured = new Insured(d[6], d[7], d[8], address);
            final PolicyReference policyReference = new PolicyReference(d[9], d[10],  PolicyReference.parseDate(d[11]), PolicyReference.parseDate(d[12]), insured, d[13]);

            assertThat("expected " + i + " to not equal fixture but did " + policyReference.timezone, policyReference.compareTo(expected), is(testResult[i]));
        }
    }

    @Test
    public void toString_should_concatenate_all_fields_in_ascending_order_by_field_name() throws IOException, ParseException {
        PolicyReference policy = DataFixtures.policyFixture();

        assertThat(policy.toString(), is("TorontoCanadaCUST12342017-01-012016-01-01noreply@example.netNate Fisher200 Wellington Street WestSuite 400PR1234BR1234M5V 3C7Ontario"));
    }

    @Test
    public void serialisation_should_be_loss_less() throws IOException, ParseException {
        final Address address = new Address("Toronto", "Canada", new String[]{"200 Wellington Street West", "Suite 400"}, "Ontario", "M5V 3C7");
        final Insured insured = new Insured("CUST1234", "Nate Fisher", "noreply@example.net", address);
        final PolicyReference policy = new PolicyReference("PR1234", "EST", PolicyReference.parseDate("2016-01-01"), PolicyReference.parseDate("2017-01-01"), insured, "BR1234");
        final String json = PolicyReference.toJson(policy);
        final PolicyReference policy2 = PolicyReference.fromJson(json);

        assertThat(policy.reference, is(equalTo("BR1234")));
        assertThat(policy2.reference, is(equalTo("BR1234")));
        assertThat(policy2.toString(), is(equalTo(policy.toString())));
    }

    @Test
    public void serialisation_should_work_with_all_fields_missing() throws IOException {
        final PolicyReference pr = PolicyReference.fromJson("{}");

        assertThat(pr.toString(), is("1970-01-011970-01-01"));
    }

    @Test
    public void parseDate_should_work_with_both_numbers_and_dates() throws ParseException {
        Object[] testData = {
                "2016-01-01",
                1451606400000L
        };

        for (Object td: testData) {
           assertThat(PolicyReference.parseDate(td).getTime(), is(1451606400000L));
        }
    }

    @Test //(expected = NumberFormatException.class)
    public void parseDate_should_throw_a_parsing_exception_on_an_invalid_date() throws ParseException {
        PolicyReference.parseDate("30000-04-01");
    }
}