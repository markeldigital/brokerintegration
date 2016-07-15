package com.markelintl.pq.data;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class InsuredTest {
    @Test
    public void compareTo_should_fail_on_mismatch() throws IOException {
        final String[][] testData = new String[][]{
                {"oronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net" },
                {"Toronto", "anada", "00 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7",  "CUST1234", "Nate Fisher", "noreply@example.net" },
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 00", "Ontario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net" },
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "ntario", "M5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net" },
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "5V 3C7", "CUST1234", "Nate Fisher", "noreply@example.net" },
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7","CUST123", "Nate Fisher", "noreply@example.net"  },
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7","CUST1234", "Nate Fishie", "noreply@example.net" },
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7","CUST1234", "Nate Fisher", "oreploy@example.net" },
        };

        final Insured expected = DataFixtures.insuredFixture();
        for (int i = 0; i < testData.length; i++) {
            final String[] d = testData[i];
            final Address address = new Address(d[0], d[1], new String[]{d[2], d[3]}, d[4], d[5]);
            final Insured insured = new Insured(d[6], d[7], d[8], address);

            assertThat("expected " + i + " to not equal fixture but did", expected.compareTo(insured), is(not(0)));
        }
    }

    @Test
    public void toString_should_concatenate_fields_in_ascending_order() {
        final Insured insured = new Insured("A", "B", "C", null);

        assertThat(insured.toString(), is("CBA"));
    }
}