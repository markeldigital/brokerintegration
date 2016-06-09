package com.markelintl.pq.devserver.resources;

import com.markelintl.pq.data.PolicyReference;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PolicyReferenceResourceTest {
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PolicyReferenceResource())
            .build();

    /*
    @Test
    public void valid_signed_policy_reference_should_succeed() throws ParseException {
        PolicyReference pr = new PolicyReference();
        String resp = resources.client().target("/policy-reference")
                .request()
                .post(Entity.json(pr), String.class);

        assertThat(resp, is(PolicyReferenceResource.SIGNATURE_OK));
    }
    */

    @Test
    public void invalid_signed_policy_reference_should_fail() throws ParseException {
        PolicyReference pr = new PolicyReference();
        String resp = resources.client().target("/policy-reference").request().post(Entity.json(pr), String.class);

        assertThat(resp, is(PolicyReferenceResource.SIGNATURE_INVALID));
    }
}