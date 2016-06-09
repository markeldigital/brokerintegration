package com.markelintl.pq.devserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class DevServerConfiguration extends Configuration {
    @NotEmpty
    private String referralToken = "qwerty1234";

    @JsonProperty
    public String getReferralToken() {
        return referralToken;
    }

    @JsonProperty
    public void setReferralToken(String referralToken) {
        this.referralToken = referralToken;
    }
}