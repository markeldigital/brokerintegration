package com.markelintl.pq.data;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Address {
    public final String city;
    public final String country;
    public final List<String> lines;
    public final String province;
    public final String postcode;

    @JsonCreator
    public Address(final Map<String, Object> props) {
        this((String) props.get("city"),
                (String) props.get("country"),
                (ArrayList<String>) props.get("lines"),
                (String) props.get("province"),
                (String) props.get("postcode"));
    }

    public Address() {
        this(new HashMap<String, Object>());
    }

    public Address(final String city, final String country,
                   final ArrayList<String> lines,
                   final String province,
                   final String postcode) {
        this.city = Optional.fromNullable(city).or("");
        this.country = Optional.fromNullable(country).or("");
        this.lines = Collections.unmodifiableList(Optional.fromNullable(lines)
                .or(new ArrayList<String>()));
        this.province = Optional.fromNullable(province).or("");
        this.postcode = Optional.fromNullable(postcode).or("");

    }

    public Address(final String city,
                   final String country,
                   final String []lines,
                   final String province,
                   final String postcode) {
        this(city, country, new ArrayList<>(Arrays.asList(lines)), province, postcode);
    }

    public int compareTo(final Address otherAddress) {
        int result;

        result = city.compareTo(otherAddress.city);
        if (result != 0) {
            return result;
        }

        result = country.compareTo(otherAddress.country);
        if (result != 0) {
            return result;
        }

        result = province.compareTo(otherAddress.province);
        if (result != 0) {
            return result;
        }

        result = postcode.compareTo(otherAddress.postcode);
        if (result != 0) {
            return result;
        }

        final int len = otherAddress.lines.size() - lines.size();
        if (otherAddress.lines.size() != lines.size()) {
            return len;
        }

        for (int i = 0; i < otherAddress.lines.size(); i++) {
            result = lines.get(i).compareTo(otherAddress.lines.get(i));
            if (result != 0) {
                return result;
            }
        }

        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.city);
        sb.append(this.country);
        for (String s : this.lines) {
            sb.append(s);
        }
        sb.append(this.province);
        sb.append(this.postcode);

        return sb.toString();
    }
}
