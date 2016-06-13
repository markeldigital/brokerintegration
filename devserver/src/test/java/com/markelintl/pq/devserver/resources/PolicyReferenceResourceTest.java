package com.markelintl.pq.devserver.resources;

import com.markelintl.pq.data.PolicyReference;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PolicyReferenceResourceTest {
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PolicyReferenceResource("antsInYourPants12345!&643211"))
            .build();

    Response postRequest(final PolicyReference pr,
                         final String timestamp,
                         final String version,
                         final String signature) {
        Response resp = resources.client()
                .target(PolicyReferenceResource.PATH)
                .queryParam("ts", timestamp)
                .queryParam("v", version)
                .queryParam("sig", signature)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(pr));

        assertThat("ts = " + timestamp + ", v = " + version + ", sig = " + timestamp,
                resp.getStatus(),
                is(200));

        return resp;
    }

    @Test
    public void valid_signed_policy_reference_should_succeed() throws ParseException {
        final PolicyReference pr = new PolicyReference();
        final Response resp = postRequest(pr, "1451606400000", "1", "op3v+JX4c0z+W5yVg/KRvqJiwpbQCipuOCNO8LsP9/0=");
        final String body = resp.readEntity(String.class);

        assertThat(body, is(PolicyReferenceResource.SIGNATURE_OK));
    }

    @Test
    public void invalid_signed_policy_reference_should_fail() throws ParseException {
        String[][] testData = {
                // ts, version, signature, result
                // incorrect timestamp
                {"145160640000", "1", "op3v+JX4c0z+W5yVg/KRvqJiwpbQCipuOCNO8LsP9/0=", PolicyReferenceResource.SIGNATURE_INVALID},
                // invalid version
                {"1451606400000", "2", "op3v+JX4c0z+W5yVg/KRvqJiwpbQCipuOCNO8LsP9/0=", PolicyReferenceResource.UNKNOWN_VERSION},
                // incorrect key
                {"1451606200000", "1", "Crapop3v+JX4c0z+W5yVg/KRvqJiwpbQCipuOCNO8LsP9/0=", PolicyReferenceResource.SIGNATURE_INVALID},
        };

        final PolicyReference pr = new PolicyReference();

        int i = 0;
        for (String[] td : testData) {
            final Response resp = postRequest(pr, td[0], td[1], td[2]);
            final String body = resp.readEntity(String.class);

            assertThat("td[" + i + "]", body, is(td[3]));
            i++;
        }
    }
}