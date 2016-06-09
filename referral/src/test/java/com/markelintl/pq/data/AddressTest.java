package com.markelintl.pq.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AddressTest {
    @Test
    public void compareTo_should_fail_on_mismatch() throws IOException {
        final String[][] testData = new String[][]{
                {"Toront", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7"},
                {"Toronto", "Canad", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C7"},
                {"Toronto", "Canada", "200 Wellington Street Wes", "Suite 400", "Ontario", "M5V 3C7"},
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 40", "Ontario", "M5V 3C7"},
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontari", "M5V 3C7"},
                {"Toronto", "Canada", "200 Wellington Street West", "Suite 400", "Ontario", "M5V 3C"},
        };

        final Address actual = DataFixtures.addressFixture();
        for (int i = 0; i < testData.length; i++) {
            final String[] d = testData[i];
            final Address expected = new Address(d[0], d[1], new String[]{d[2], d[3]}, d[4], d[5]);

            assertThat("expected " + i + " to not equal fixture but did", actual.compareTo(expected), is(not(0)));
        }
    }

    @Test
    public void compareTo_should_fail_on_line_mismatch() {
        final Address expected = new Address("Toronto", "Canada", new String[]{"200 Wellington Street West", "Suite 40"}, "Ontario", "M5V 3C7");
        final Address actual = new Address("Toronto", "Canada", new String[]{"200 Wellington Street West"}, "Ontario", "M5V 3C7");

        assertThat(actual.compareTo(expected), is(1));
    }

    @Test
    public void compareTo_should_succeed_with_valid_address() throws IOException {
        final Address expected = new Address("Toronto", "Canada", new String[]{"200 Wellington Street West", "Suite 400"}, "Ontario", "M5V 3C7");
        final Address actual = DataFixtures.addressFixture();

        assertThat(actual.compareTo(expected), is(0));
    }

    @Test
    public void toString_should_concatenate_fields_in_ascending_order() {
        final Address address = new Address("A", "B", new String[]{"C", "D"}, "E", "F");

        assertThat(address.toString(), is("ABCDEF"));
    }

    @Test
    public void mapper_should_serialise_empty_json() throws IOException {
        final ObjectMapper om = new ObjectMapper();
        final Address address = om.readValue("{}", Address.class);

        assertThat(address.toString(), is(""));
    }
}