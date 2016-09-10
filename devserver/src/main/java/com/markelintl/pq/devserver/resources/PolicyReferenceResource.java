package com.markelintl.pq.devserver.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.markelintl.pq.data.Address;
import com.markelintl.pq.data.Insured;
import com.markelintl.pq.data.PolicyReference;
import com.markelintl.pq.data.PolicySignature;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static com.markelintl.pq.data.PolicySignature.*;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Path(PolicyReferenceResource.PATH)
public class PolicyReferenceResource {
    public static final String UNKNOWN_VERSION = "Version";
    public static final String SIGNATURE_OK = "OK";
    public static final String SIGNATURE_INVALID = "Signature Invalid";
    public static final String DATE_INVALID = "Date Invalid";
    public static final String PATH = "/policy-reference";
    public static final String UNKNOWN_ENCODING = "UTF-8 is an Unknown Encoding";

    private final PolicySignature signer;

    public PolicyReferenceResource(final String key) {
        this.signer = new PolicySignature(key);
    }

    @POST
    @Timed
    @Consumes(APPLICATION_FORM_URLENCODED)
    public String createPolicyReference(
            @QueryParam("ts") final Optional<Long> timestamp,
            @QueryParam("sig") final Optional<String> signature,
            @QueryParam("v") final Optional<Integer> version,
            @FormParam("policyNumber") final Optional<String> policyNumber,
            @FormParam("policyReference") final Optional<String> policyReference,
            @FormParam("timeZone") final Optional<String> timeZone,
            @FormParam("inception") final Optional<String> inception,
            @FormParam("expiry") final Optional<String> expiry,
            @FormParam("insuredName") final Optional<String> insuredName,
            @FormParam("insuredReference") final Optional<String> insuredReference,
            @FormParam("insuredEmail") final Optional<String> insuredEmail,
            @FormParam("insuredMailingCity") final Optional<String> insuredMailingCity,
            @FormParam("insuredMailingProvince") final Optional<String> insuredMailingProvince,
            @FormParam("insuredMailingPostcode") final Optional<String> insuredMailingPostcode,
            @FormParam("insuredMailingCountry") final Optional<String> insuredMailingCountry,
            @FormParam("insuredMailingLines") final List<String> insuredMailingLines) {


        final PolicyReference policy;
        try {
            final Map<String,Object> mailingAddress = Address.builder()
                    .withCity(insuredMailingCity.or(""))
                    .withCountry(insuredMailingCountry.or(""))
                    .withLines(insuredMailingLines)
                    .withPostcode(insuredMailingPostcode.or(""))
                    .withProvince(insuredMailingProvince.or(""))
                    .buildMap();

            final Map<String,Object> insured = Insured.builder()
                    .withEmail(insuredEmail.or(""))
                    .withFullname(insuredName.or(""))
                    .withMailingAddress(mailingAddress)
                    .withReference(insuredReference.or(""))
                    .buildMap();

            policy = PolicyReference.builder()
                    .withExpiry(expiry.or(""))
                    .withInception(inception.or(""))
                    .withInsured(insured)
                    .withPolicyNumber(policyNumber.or(""))
                    .withPolicyReference(policyReference.or(""))
                    .withTimezone(timeZone.or(""))
                    .build();
        } catch (final ParseException pex) {
            return DATE_INVALID;
        }

        boolean valid;
        String b64;

        if (!version.or(0).equals(1)) {
            return UNKNOWN_VERSION;
        }

        try {
            b64 = signer.signToBase64(timestamp.or(0L), policy);
            valid = signer.verifyFromBase64(timestamp.or(0L), policy, signature.or("1234"));
        } catch (final NoSuchAlgorithmException nsae) {
            return SIGNATURE_INVALID;
        } catch (final InvalidKeyException ike) {
            return SIGNATURE_INVALID;
        }

        if (valid) {
            return SIGNATURE_OK;
        }

        try {
            return SIGNATURE_INVALID
                    + ",<br/> ref=" + policy.toString()
                    + ",<br/> base64sig=" + b64
                    + ",<br/> urlencodesig=" + URLEncoder.encode(b64, "UTF-8")
                    + ",<br/> ts=" + timestamp.or(0L)
                    + ",<br/> input=" + payload(timestamp.or(0L), policy);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "Unknown Error";
    }
}