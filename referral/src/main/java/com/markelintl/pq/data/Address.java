package com.markelintl.pq.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.sun.javafx.collections.MappingChange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Address {
    public final String city;
    public final String country;
    public final ArrayList<String> lines;
    public final String province;
    public final String postcode;

    @JsonCreator
    public Address(Map<String,Object> props) {
        this((String) props.get("city"),
                (String) props.get("country"),
                (ArrayList<String>) props.get("lines"),
                (String) props.get("province"),
                (String) props.get("postcode"));
    }


    public Address() {
        this(new HashMap<String, Object>());
    }

    public Address(final String city, final String country, final ArrayList<String> lines, final String province, final String postcode) {
        this.city = Optional.fromNullable(city).or("");
        this.country = Optional.fromNullable(country).or("");
        this.lines = Optional.fromNullable(lines).or(new ArrayList<String>());
        this.province = Optional.fromNullable(province).or("");
        this.postcode = Optional.fromNullable(postcode).or("");

    }

    public Address(final String city, final String country, final String []lines, final String province, final String postcode) {
        this(city, country, new ArrayList<>(Arrays.asList(lines)), province, postcode);
    }

    public int compareTo(final Address o) {
        int r = 0;
        r = city.compareTo(o.city);
        if (r != 0) return r;

        r = country.compareTo(o.country);
        if (r != 0) return r;

        r = province.compareTo(o.province);
        if (r != 0) return r;

        r = postcode.compareTo(o.postcode);
        if (r != 0) return r;

        final int len = o.lines.size() - lines.size();
        if (o.lines.size() != lines.size()) {
            return len;
        }

        for (int i = 0; i < o.lines.size(); i++) {
            r = lines.get(i).compareTo(o.lines.get(i));
            if (r != 0) return r;
        }

        return r;
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
