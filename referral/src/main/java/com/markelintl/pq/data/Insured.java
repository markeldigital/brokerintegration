package com.markelintl.pq.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class Insured {
    public final String reference;
    public final String fullname;
    public final String email;
    public final Address mailingAddress;

    public static final int ADDRESS_MISMATCH = 1;
    public static final int EMAIL_MISMATCH = 2;
    public static final int FULLNAME_MISMATCH = 4;
    public static final int REFERENCE_MISMATCH = 8;

    public Insured(final String reference, final String fullname, final String email, final Address mailingAddress) {
        this.reference = Optional.fromNullable(reference).or("");
        this.fullname = Optional.fromNullable(fullname).or("");
        this.email = Optional.fromNullable(email).or("");
        this.mailingAddress = Optional.fromNullable(mailingAddress).or(new Address());
    }

    @JsonCreator
    public Insured(Map<String, Object> props) {
       this((String) props.get("reference"),
            (String) props.get("fullname"),
            (String) props.get("email"),
            new Address(Optional.fromNullable((Map<String, Object>)props.get("mailingAddress")).or(new LinkedHashMap<String,Object>())));
    }

    public Insured() {
       this(new HashMap<String, Object>());
    }

    public int compareTo(final Insured o) {
        int r = 0;

        r = mailingAddress.compareTo(o.mailingAddress);
        if (r != 0) return ADDRESS_MISMATCH;

        r = email.compareTo(o.email);
        if (r != 0) return EMAIL_MISMATCH;

        r = fullname.compareTo(o.fullname);
        if (r != 0) return FULLNAME_MISMATCH;

        r = reference.compareTo(o.reference);
        if (r != 0) return REFERENCE_MISMATCH;

        return r;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(email);
        sb.append(fullname);
        sb.append(mailingAddress);
        sb.append(reference);

        return sb.toString();
    }
}
