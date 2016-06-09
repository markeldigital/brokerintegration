package com.markelintl.pq.devserver.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.markelintl.pq.data.PolicyReference;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;


@Path("/policy-reference")
@Produces(MediaType.APPLICATION_JSON)
public class PolicyReferenceResource {
    public static String SIGNATURE_OK = "Signature is OK.";
    public static String SIGNATURE_INVALID = "Signature is Invalid.";

    @GET
    @Timed
    public PolicyReference getPolicyReference() {
        try {
            return new PolicyReference();
        } catch (ParseException e) { }

        return null;
    }

    @POST
    @Timed
    public String createPolicyReference(@QueryParam("ts") Optional<String> timestamp,
                                      @QueryParam("sig") Optional<String> signature,
                                      @QueryParam("v") Optional<String> version) {
        if (true) {
           return SIGNATURE_INVALID;
        }

        return SIGNATURE_OK;
    }
}