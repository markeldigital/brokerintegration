package com.markelintl.pq.data;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class PolicySignatureTest {
    @Test
    public void verifyFromBase64_should_pass_with_valid_signature_and_default_key() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {
        final PolicyReference policy = DataFixtures.policyFixture();
        final PolicySignature signer = new PolicySignature("qwerty1234");

        assertThat("unexpected signature", signer.signToBase64(1234, policy), is("PQ0wxKHDQZCvVkYY1xQjz7MjyTC5puYofJ3Ng4Lz8qg="));
        assertThat("verification failed", signer.verifyFromBase64(1234, policy, "PQ0wxKHDQZCvVkYY1xQjz7MjyTC5puYofJ3Ng4Lz8qg="), is(true));
    }

    @Test
    public void verifyFromBase64_should_fail_when_invalid_signature() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {
        final PolicyReference policy = new PolicyReference();
        final PolicySignature signer = new PolicySignature("antsInYourPants12345!&643211");

        assertThat("signatures should match", signer.signToBase64(1234, policy), is("fXa+gvz81lJcIWUqhSbDe2+xVVvxU6FIkQC2moFdv0k="));
        assertThat("verification not should match", signer.verifyFromBase64(1234, policy, "NeUIQTARTbfEJO1h3U9Am/hLXxvrAbK+yehpl0d1iZu="), is(false));
    }

    @Test
    public void sign_should_return_different_signature_with_same_policy_and_a_new_timestamp() throws IOException, InvalidKeyException, NoSuchAlgorithmException, ParseException {
        final PolicyReference policy = DataFixtures.policyFixture();
        final PolicySignature signer = new PolicySignature("antsInYourPants12345!&643211");
        byte[] signature = signer.signPolicy(0, policy);
        byte[] signature2 = signer.signPolicy(10, policy);

        assertThat(signature.length, is(32));
        assertThat(signature, not(equalTo(signature2)));
    }
}