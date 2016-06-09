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

public class PolicyReference {
    public static final int POLICY_NUMBER_MISMATCH = 1;
    public static final int TIMEZONE_MISMATCH = 2;
    public static final int INCEPTION_MISMATCH = 4;
    public static final int EXPIRY_MISMATCH = 8;
    public static final int POLICY_REFERENCE_MISMATCH = 16;
    public static final int INSURED_MISMATCH = 32;
    static final DateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

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
             parseDate(Optional.fromNullable((String) props.get("inception")).or("2016-01-01")),
             parseDate(Optional.fromNullable((String) props.get("expiry")).or("2017-01-01")),
             new Insured(Optional.fromNullable((Map<String, Object>) props.get("insured")).or(new LinkedHashMap<String, Object>())),
             (String) props.get("reference"));
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

    public static Date parseDate(final String s) throws ParseException {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(FMT.parse(s));

        return cal.getTime();
    }

    public int compareTo(final PolicyReference o) {
        if (number.compareTo(o.number) != 0) return POLICY_NUMBER_MISMATCH;
        
        if (timezone.compareTo(o.timezone) != 0) return TIMEZONE_MISMATCH;
        
        if (inception.compareTo(o.inception) != 0) return INCEPTION_MISMATCH;

        if (expiry.compareTo(o.expiry) != 0) return EXPIRY_MISMATCH;

        if (reference.compareTo(o.reference) != 0) return POLICY_REFERENCE_MISMATCH;

        if (insured.compareTo(o.insured) != 0) return INSURED_MISMATCH;

        return 0;
    }

    public static String toJson(final PolicyReference p) throws JsonProcessingException {
        final ObjectMapper om = new ObjectMapper();
        om.setDateFormat(FMT);
        return om.writeValueAsString(p);
    }

    public static PolicyReference fromJson(final String json) throws IOException {
        final ObjectMapper om = new ObjectMapper();
        om.setDateFormat(FMT);
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
