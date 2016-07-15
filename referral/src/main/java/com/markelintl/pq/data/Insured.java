package com.markelintl.pq.data;

import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Insured {
    public final String reference;
    public final String fullname;
    public final String email;
    public final Address mailingAddress;

    public static final int ADDRESS_MISMATCH = 1;
    public static final int EMAIL_MISMATCH = 2;
    public static final int FULLNAME_MISMATCH = 4;
    public static final int REFERENCE_MISMATCH = 8;

    public Insured(final String reference,
                   final String fullname,
                   final String email,
                   final Address mailingAddress) {
        this.reference = Optional.fromNullable(reference).or("");
        this.fullname = Optional.fromNullable(fullname).or("");
        this.email = Optional.fromNullable(email).or("");
        this.mailingAddress = Optional.fromNullable(mailingAddress).or(new Address());
    }

    public Insured(Map<String, Object> props) {
       this((String) props.get("reference"),
            (String) props.get("fullname"),
            (String) props.get("email"),
            new Address(Optional.fromNullable((Map<String, Object>)props.get("mailingAddress"))
                    .or(new LinkedHashMap<String,Object>())));
    }

    public Insured() {
       this(new HashMap<String, Object>());
    }

    public int compareTo(final Insured otherInsured) {
        int result;

        result = mailingAddress.compareTo(otherInsured.mailingAddress);
        if (result != 0) {
            return ADDRESS_MISMATCH;
        }

        result = email.compareTo(otherInsured.email);
        if (result != 0) {
            return EMAIL_MISMATCH;
        }

        result = fullname.compareTo(otherInsured.fullname);
        if (result != 0) {
            return FULLNAME_MISMATCH;
        }

        result = reference.compareTo(otherInsured.reference);
        if (result != 0) {
            return REFERENCE_MISMATCH;
        }

        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(email);
        sb.append(fullname);
        sb.append(mailingAddress);
        sb.append(reference);

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Insured insured = (Insured) obj;

        if (!reference.equals(insured.reference)) {
            return false;
        }
        if (!fullname.equals(insured.fullname)) {
            return false;
        }
        if (!email.equals(insured.email)) {
            return false;
        }
        return mailingAddress.equals(insured.mailingAddress);
    }

    @Override
    public int hashCode() {
        int result = reference.hashCode();
        result = 31 * result + fullname.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + mailingAddress.hashCode();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        final Map<String, Object> map = new HashMap<>();

        public Builder withFullname(final String fullname) {
            map.put("fullname", fullname);
            return this;
        }

        public Builder withReference(final String reference) {
            map.put("reference", reference);
            return this;
        }

        public Builder withEmail(final String email) {
            map.put("email", email);
            return this;
        }

        public Builder withMailingAddress(final Map<String, Object> mailingAddress) {
            map.put("mailingAddress", mailingAddress);
            return this;
        }

        public Insured build() {
            return new Insured(map);
        }

        public Map<String, Object> buildMap() {
            return map;
        }
    }
}
