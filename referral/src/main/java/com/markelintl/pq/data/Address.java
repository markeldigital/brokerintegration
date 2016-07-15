package com.markelintl.pq.data;

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
        this(city, country, new ArrayList<String>(Arrays.asList(lines)), province, postcode);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Address address = (Address) obj;

        if (!city.equals(address.city)) {
            return false;
        }
        if (!country.equals(address.country)) {
            return false;
        }
        if (!lines.equals(address.lines)) {
            return false;
        }
        if (!province.equals(address.province)) {
            return false;
        }
        return postcode.equals(address.postcode);

    }

    @Override
    public int hashCode() {
        int result = city.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + lines.hashCode();
        result = 31 * result + province.hashCode();
        result = 31 * result + postcode.hashCode();
        return result;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        final HashMap<String, Object> map = new HashMap<String, Object>();

        public Builder withCity(final String city) {
            map.put("city", city);
            return this;
        }

        public Builder withCountry(final String country) {
            map.put("country", country);
            return this;
        }

        public Builder withLines(final List<String> lines) {
            map.put("lines", lines);
            return this;
        }

        public Builder withPostcode(final String postcode) {
            map.put("postcode", postcode);
            return this;
        }

        public Builder withProvince(final String province) {
            map.put("province", province);
            return this;
        }

        public Address build() {
            return new Address(map);
        }

        public Map<String,Object> buildMap() {
            return map;
        }
    }
}
