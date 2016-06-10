package com.markelintl.pq.devserver.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.markelintl.pq.data.PolicyReference;
import com.markelintl.pq.data.PolicySignature;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path(PolicyReferenceResource.PATH)
@Produces(APPLICATION_JSON)
public class PolicyReferenceResource {
    public static final String UNKNOWN_VERSION = "Version";
    public static final String SIGNATURE_OK = "OK.";
    public static final String SIGNATURE_INVALID = "Invalid.";
    public static final String PATH = "/policy-reference";

    private final PolicySignature signer;

    public PolicyReferenceResource(final String key) {
        this.signer = new PolicySignature(key);
    }

    @GET
    @Timed
    public PolicyReference getPolicyReference() {
        try {
            return new PolicyReference();
        } catch (ParseException pex) {
            return null;
        }
    }

    @POST
    @Timed
    @Consumes(APPLICATION_JSON)
    public String createPolicyReference(@QueryParam("ts") Optional<Long> timestamp,
                                        @QueryParam("sig") Optional<String> signature,
                                        @QueryParam("v") Optional<Integer> version,
                                        PolicyReference policy) {
        boolean valid;

        if (!version.or(0).equals(1)) {
            return UNKNOWN_VERSION;
        }

        try {
            valid = signer.verifyFromBase64(timestamp.or(0L), policy, signature.or("1234"));
        } catch (final NoSuchAlgorithmException nsae) {
            return SIGNATURE_INVALID;
        } catch (final InvalidKeyException ike) {
            return SIGNATURE_INVALID;
        }

        if (valid) {
            return SIGNATURE_OK;
        }

        return SIGNATURE_INVALID;
    }
}