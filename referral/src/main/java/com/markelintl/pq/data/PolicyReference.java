package com.markelintl.pq.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public final class PolicyReference {
    public static final int POLICY_NUMBER_MISMATCH = 1;
    public static final int TIMEZONE_MISMATCH = 2;
    public static final int INCEPTION_MISMATCH = 4;
    public static final int EXPIRY_MISMATCH = 8;
    public static final int POLICY_REFERENCE_MISMATCH = 16;
    public static final int INSURED_MISMATCH = 32;
    public static final TimeZone WIRE_TIMEZONE = TimeZone.getTimeZone("GMT");
    static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FMT.setTimeZone(WIRE_TIMEZONE);
    }

    public final String number;
    public final String timezone;
    public final String reference;
    public final Date expiry;
    public final Date inception;
    public final Insured insured;

    @JsonCreator
    public PolicyReference(Map<String, Object> props) throws ParseException {
        this((String) props.get("number"),
                (String) props.get("timezone"),
                parseDate(props.get("inception")),
                parseDate(props.get("expiry")),
                new Insured(Optional.fromNullable((Map<String, Object>) props.get("insured"))
                     .or(new LinkedHashMap<String, Object>())),
                (String) props.get("reference"));
    }

    public PolicyReference() throws ParseException {
       this(new LinkedHashMap<String, Object>());
    }

    public PolicyReference(final String number,
                           final String timezone,
                           final Date inception,
                           final Date expiry,
                           final Insured insured,
                           final String reference) {
        this.number = Optional.fromNullable(number).or("");
        this.inception = Optional.fromNullable(inception).or(new Date());
        this.expiry = Optional.fromNullable(expiry).or(new Date());
        this.timezone = Optional.fromNullable(timezone).or(WIRE_TIMEZONE.getID());
        this.insured = Optional.fromNullable(insured).or(new Insured());
        this.reference = Optional.fromNullable(reference).or("");
    }

    public static Date parseDate(final Object dateObj)
            throws ParseException, NumberFormatException {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeZone(WIRE_TIMEZONE);

        if (String.class.isInstance(dateObj)) {
            final String date = (String) dateObj;
            if (date.matches("^\\d+$")) {
                return new Date(Long.parseLong(date));
            } else {
                cal.setTime(DATE_FMT.parse(date));
                return cal.getTime();
            }
        } else if (Long.class.isInstance(dateObj)) {
            return new Date((Long) dateObj);
        }

        return new Date(0);
    }

    public static Date parseDate(final String dateStr, final String timezone)
            throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(dateStr);
    }

    public int compareTo(final PolicyReference otherPolicyReference) {
        if (number.compareTo(otherPolicyReference.number) != 0) {
            return POLICY_NUMBER_MISMATCH;
        }
        
        if (timezone.compareTo(otherPolicyReference.timezone) != 0) {
            return TIMEZONE_MISMATCH;
        }
        
        if (inception.compareTo(otherPolicyReference.inception) != 0) {
            return INCEPTION_MISMATCH;
        }

        if (expiry.compareTo(otherPolicyReference.expiry) != 0) {
            return EXPIRY_MISMATCH;
        }

        if (reference.compareTo(otherPolicyReference.reference) != 0) {
            return POLICY_REFERENCE_MISMATCH;
        }

        if (insured.compareTo(otherPolicyReference.insured) != 0) {
            return INSURED_MISMATCH;
        }

        return 0;
    }

    public static String toJson(final PolicyReference policyReference)
            throws JsonProcessingException {
        final ObjectMapper om = new ObjectMapper();
        om.setDateFormat(DATE_FMT);
        return om.writeValueAsString(policyReference);
    }

    public static PolicyReference fromJson(final String json) throws IOException {
        final ObjectMapper om = new ObjectMapper();
        om.setDateFormat(DATE_FMT);
        return om.readValue(json, PolicyReference.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PolicyReference that = (PolicyReference) obj;

        if (!number.equals(that.number)) {
            return false;
        }
        if (!timezone.equals(that.timezone)) {
            return false;
        }
        if (!reference.equals(that.reference)) {
            return false;
        }
        if (!expiry.equals(that.expiry)) {
            return false;
        }
        if (!inception.equals(that.inception)) {
            return false;
        }
        return insured.equals(that.insured);

    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + timezone.hashCode();
        result = 31 * result + reference.hashCode();
        result = 31 * result + expiry.hashCode();
        result = 31 * result + inception.hashCode();
        result = 31 * result + insured.hashCode();
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(insured.mailingAddress.city);
        sb.append(insured.mailingAddress.country);
        sb.append(insured.reference);
        sb.append(DATE_FMT.format(expiry));
        sb.append(DATE_FMT.format(inception));
        sb.append(insured.email);
        sb.append(insured.fullname);

        for (int i = 0; i < insured.mailingAddress.lines.size(); i++) {
            sb.append(insured.mailingAddress.lines.get(i));
        }

        sb.append(number);
        sb.append(reference);
        sb.append(insured.mailingAddress.postcode);
        sb.append(insured.mailingAddress.province);

        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        final Map<String, Object> map = new HashMap<String, Object>();

        public Builder withExpiry(final String expiry) {
            map.put("expiry", expiry);
            return this;
        }

        public Builder withInception(final String inception) {
            map.put("inception", inception);
            return this;
        }

        public Builder withInsured(final Map<String, Object> insured) {
            map.put("insured", insured);
            return this;
        }

        public Builder withPolicyNumber(final String policyNumber) {
            map.put("number", policyNumber);
            return this;
        }

        public Builder withPolicyReference(final String policyReference) {
            map.put("reference", policyReference);
            return this;
        }

        public Builder withTimezone(final String timezone) {
            map.put("timezone", timezone);
            return this;
        }

        public PolicyReference build() throws ParseException {
            return new PolicyReference(map);
        }
    }
}