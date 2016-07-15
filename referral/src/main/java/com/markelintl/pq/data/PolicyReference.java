package com.markelintl.pq.data;

import com.google.common.base.Optional;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PolicyReference {
    public static final int POLICY_NUMBER_MISMATCH = 1;
    public static final int TIMEZONE_MISMATCH = 2;
    public static final int INCEPTION_MISMATCH = 4;
    public static final int EXPIRY_MISMATCH = 8;
    public static final int POLICY_REFERENCE_MISMATCH = 16;
    public static final int INSURED_MISMATCH = 32;

    public final String number;
    public final String timezone;
    public final String reference;
    public final LocalDate expiry;
    public final LocalDate inception;
    public final Insured insured;

    public PolicyReference(Map<String, Object> props) throws ParseException {
        this((String) props.get("number"),
                (String) props.get("timezone"),
                parseDate(props.get("inception"), Optional
                        .fromNullable((String)props.get("timezone")).or("GMT")),
                parseDate(props.get("expiry"), Optional
                        .fromNullable((String)props.get("timezone")).or("GMT")),
                new Insured((Map<String, Object>) Optional
                        .fromNullable(props.get("insured"))
                        .or(new HashMap<String, Object>())),
                (String) props.get("reference"));
    }

    public PolicyReference() throws ParseException {
       this(new LinkedHashMap<String, Object>());
    }

    public PolicyReference(final String number,
                           final String timezone,
                           final LocalDate inception,
                           final LocalDate expiry,
                           final Insured insured,
                           final String reference) {

        this.number = Optional.fromNullable(number).or("");
        this.timezone = Optional.fromNullable(timezone).or("GMT");
        this.inception = Optional.fromNullable(inception).or(new LocalDate());
        this.expiry = Optional.fromNullable(expiry).or(new LocalDate());
        this.insured = Optional.fromNullable(insured).or(new Insured());
        this.reference = Optional.fromNullable(reference).or("");
    }

    public static LocalDate parseDate(final Object dateObj, final String timezone)
            throws ParseException {
        final DateTimeZone dtz = DateTimeZone.forID(timezone == "" ? "GMT" : timezone);
        final DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd").withZone(dtz);

        if (String.class.isInstance(dateObj)) {
            return parseDate((String) dateObj, dateFmt);
        } else if (Long.class.isInstance(dateObj)) {
            return parseDate((Long) dateObj, dtz);
        }

        return new LocalDate(0L, dtz);
    }

    private static LocalDate parseDate(final Long dateObj, final DateTimeZone dtz) {
        final LocalDate dt = new LocalDate(dateObj, dtz);
        return dt;
    }

    private static LocalDate parseDate(String dateObj, DateTimeFormatter dateFmt) {
        final String dateStr = dateObj;
        final LocalDate date = dateFmt.parseLocalDate(dateStr);

        return date;
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

        sb.append(insured.mailingAddress.city)
                .append(insured.mailingAddress.country)
                .append(insured.reference)
                .append(timezone)
                .append(expiry)
                .append(inception)
                .append(insured.email)
                .append(insured.fullname);

        for (int i = 0; i < insured.mailingAddress.lines.size(); i++) {
            sb.append(insured.mailingAddress.lines.get(i));
        }

        sb.append(number)
                .append(reference)
                .append(insured.mailingAddress.postcode)
                .append(insured.mailingAddress.province);

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