package com.markelintl.pq.devserver.resources;

import com.markelintl.pq.data.PolicyReference;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import java.text.ParseException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
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
        Form form = new Form();

        form.param("policyNumber", pr.number)
            .param("policyReference", pr.reference)
            .param("timeZone", pr.timezone)
            .param("insuredName", pr.insured.fullname)
            .param("insuredReference", pr.insured.reference)
            .param("insuredEmail", pr.insured.email)
            .param("insuredMailingCity", pr.insured.mailingAddress.city)
            .param("insuredMailingProvince", pr.insured.mailingAddress.province)
            .param("insuredMailingPostcode", pr.insured.mailingAddress.postcode)
            .param("insuredMailingCountry", pr.insured.mailingAddress.country)
        ;

        form.param("inception", String.valueOf(pr.inception.getTime()))
            .param("expiry", String.valueOf(pr.expiry.getTime()));

        for (String line : pr.insured.mailingAddress.lines) {
            form.param("insuredMailingLines", line);
        }

        Response resp = resources.client()
                .target(PolicyReferenceResource.PATH)
                .queryParam("ts", timestamp)
                .queryParam("v", version)
                .queryParam("sig", signature)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .post(Entity.form(form));

        assertThat("ts = " + timestamp + ", v = " + version + ", sig = " + signature,
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

            assertThat("td[" + i + "]", body, startsWith(td[3]));
            i++;
        }
    }
}