package com.markelintl.pq.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PolicyReference {
    public static final int POLICY_NUMBER_MISMATCH = 1;
    public static final int TIMEZONE_MISMATCH = 2;
    public static final int INCEPTION_MISMATCH = 4;
    public static final int EXPIRY_MISMATCH = 8;
    public static final int POLICY_REFERENCE_MISMATCH = 16;
    public static final int INSURED_MISMATCH = 32;
    static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

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
        this.timezone = Optional.fromNullable(timezone).or("UTC");
        this.inception = Optional.fromNullable(inception).or(new Date());
        this.expiry = Optional.fromNullable(expiry).or(new Date());
        this.insured = Optional.fromNullable(insured).or(new Insured());
        this.reference = Optional.fromNullable(reference).or("");
    }

    public static Date parseDate(final Object dateObj)
            throws ParseException, NumberFormatException {
        final Calendar cal = Calendar.getInstance();
        final String re = "^20[0-9][0-9]-[0-1][0-9]-[0-3][0-9]$";

        if (String.class.isInstance(dateObj)) {
            cal.setTime(DATE_FMT.parse((String) dateObj));
            return cal.getTime();
        } else if (Long.class.isInstance(dateObj)) {
            return new Date((Long) dateObj);
        }

        return new Date(0);
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

    public String toString() {
        final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sb = new StringBuilder();

        sb.append(insured.mailingAddress.city);
        sb.append(insured.mailingAddress.country);
        sb.append(insured.reference);
        sb.append(fmt.format(expiry));
        sb.append(fmt.format(inception));
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
}
