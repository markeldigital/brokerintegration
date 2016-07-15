package com.markelintl.pq.data;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static com.markelintl.pq.data.PolicyReference.parseDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class PolicyReferenceTest {
    private static final int CITY = 0;
    private static final int COUNTRY = CITY + 1;
    private static final int LINE0 = CITY + 2;
    private static final int LINE1 = CITY + 3;
    private static final int PROVINCE = CITY + 4;
    private static final int POSTCODE = CITY + 5;
    private static final int CUSTREF = CITY + 6;
    private static final int NAME = CITY + 7;
    private static final int EMAIL = CITY + 8;
    private static final int POLICYREF = CITY + 9;
    private static final int TIMEZONE = CITY + 10;
    private static final int INCEPTION = CITY + 11;
    private static final int EXPIRY = CITY + 12;
    private static final int EXTREF = CITY + 13;

    @Test
    public void compareTo_should_fail_on_mismatch() throws IOException, ParseException {
        final String[][] testData = new String[][]{
           // 0       , 1       , 2                           , 3          , 4        , 5        , 6         , 7            , 8                    , 9        , 10   , 11          , 12          , 13
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PRI1234", "EST", "2016-01-01", "2017-01-01", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "PST8PDT", "2016-01-01", "2017-01-01", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-02", "2017-01-01", "BR1234"},
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-01", "2017-01-02", "BR1234"},
                // External reference
            {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-01", "2017-01-01", "BR123"},
                // Mailing address
            {"Toront",  "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net", "PR1234", "EST", "2016-01-01", "2017-01-01", "BR1234"},
        };

        final int[] testResult = new int[] {
            1, 2, 4, 8, 16, 32,
        };

        final PolicyReference expected = DataFixtures.policyFixture();

        for (int i = 0; i < testData.length; i++) {
            final String[] d = testData[i];
            final Address address = new Address(d[CITY], d[COUNTRY], new String[]{d[LINE0], d[LINE1]}, d[PROVINCE], d[POSTCODE]);
            final Insured insured = new Insured(d[CUSTREF], d[NAME], d[EMAIL], address);
            final PolicyReference policyReference = new PolicyReference(d[POLICYREF], d[TIMEZONE],  parseDate(d[INCEPTION], d[TIMEZONE]), parseDate(d[EXPIRY], d[TIMEZONE]), insured, d[EXTREF]);

            assertThat("expected " + i + " to not equal fixture but did " + policyReference.timezone, policyReference.compareTo(expected), is(testResult[i]));
        }
    }

    @Test
    public void toString_should_concatenate_all_fields_in_ascending_order_by_field_name() throws IOException, ParseException {
        final PolicyReference policy = DataFixtures.policyFixture();

        assertThat(policy.toString(), is("TorontoCanadaCUST1234EST2017-01-012016-01-01noreply@example.netNate Fisher200 Wellington Street WestSuite 400PR1234BR1234M5V 3C7Ontario"));
    }

    @Test
    public void parseDate_should_apply_timezone_correctly() throws ParseException {
        final Object[][] testData = {
                {"2016-01-01",   "GMT", new LocalDate(1451606400000L)},
                {"2016-01-01",   "",    new LocalDate(1451606400000L)},
                {"2016-01-01",   "EST", new LocalDate(1451624400000L)},
                {1451624400000L, "EST", new LocalDate(1451624400000L)}
        };

        int i = 0;
        for (final Object[] td: testData) {
            i++;
            final String tz = (String) td[1];
            assertThat("td["+ i +"]", parseDate(td[0], tz), is(equalTo(td[2])));
        }
    }
}