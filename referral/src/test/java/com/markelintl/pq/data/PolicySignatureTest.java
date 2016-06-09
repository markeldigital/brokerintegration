package com.markelintl.pq.data;

import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class PolicySignatureTest {
    @Test
    public void verifyFromBase64_should_pass_when_valid_signature() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        final PolicyReference policy = DataFixtures.policyFixture();
        final PolicySignature signer = new PolicySignature("antsInYourPants12345!&643211");

        assertThat(signer.verifyFromBase64(1234, policy, "nEuiqtartBFejo1H3u9aM/HlxXVRaBk+YEHPL0D1IzU="), is(true));
    }

    @Test
    public void verifyFromBase64_should_fail_when_invalid_signature() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        final PolicyReference policy = DataFixtures.policyFixture();
        final PolicySignature signer = new PolicySignature("antsInYourPants12345!&643211");

        assertThat(signer.verifyFromBase64(1234, policy, "NeUIQTARTbfEJO1h3U9Am/hLXxvrAbK+yehpl0d1iZu="), is(false));
    }

    @Test
    public void signToBase64_should_return_an_encoded_signature() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final PolicyReference policy = DataFixtures.policyFixture();
        final PolicySignature signer = new PolicySignature("antsInYourPants12345!&643211");
        final String encodedSignature = signer.signToBase64(1234, policy);

        assertThat(encodedSignature, is(equalTo("nEuiqtartBFejo1H3u9aM/HlxXVRaBk+YEHPL0D1IzU=")));
    }

    @Test
    public void sign_should_return_different_signature_with_change_in_timestamp() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        final PolicyReference policy = DataFixtures.policyFixture();
        final PolicySignature signer = new PolicySignature("antsInYourPants12345!&643211");
        byte[] signature = signer.signPolicy(0, policy);
        byte[] signature2 = signer.signPolicy(10, policy);

        assertThat(signature.length, is(32));
        assertThat(signature, is(equalTo(signature)));
        assertThat(signature, not(equalTo(signature2)));
    }
}